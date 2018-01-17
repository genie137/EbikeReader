package nl.easthome.ebikereader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;
import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGiven;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Implementations.RideRecordingServiceConnection;
import nl.easthome.ebikereader.Interfaces.IRideRecordingGuiUpdate;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class DashboardActivity extends BaseActivityWithMenu {
    static final String LOGTAG = "DashboardActivity";

    @BindView(R.id.dashboard_layout) ConstraintLayout mDashboardLayout;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.dashboard_statustext) TextView mStatusText;
    @BindView(R.id.realtime_cadance) TextView mRealtimeCadance;
    @BindView(R.id.realtime_distance) TextView mRealtimeDistance;
    @BindView(R.id.realtime_heartrate) TextView mRealtimeHeartrate;
    @BindView(R.id.realtime_power) TextView mRealtimePower;
    @BindView(R.id.realtime_speed_kmh) TextView mRealtimeSpeed;
    private RideRecordingGuiUpdater mRideRecordingGuiUpdater;
    private RideRecordingServiceConnection mRideRecordingServiceConnection;
    private Intent mRideRecordingIntent;
    private RideRecordingService mRideRecordingService;

    @OnClick(R.id.fab)
    public void onStartRecordingButtonPress() {
        mRideRecordingService = mRideRecordingServiceConnection.getRideRecordingService();
        if (mRideRecordingService.isRecording()) {
            mRideRecordingService.stopRecording();
            mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
        } else {
            final Activity activity = this;
            new MaterialDialog.Builder(this)
                    .title("Which sensors to record?\n(GPS is always recorded)")
                    .items(R.array.sensors)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            String selected = Arrays.toString(text);
                            try {
                                mRideRecordingService.startRecording(
                                        activity,
                                        mRideRecordingGuiUpdater,
                                        selected.contains("Power Sensor"),
                                        selected.contains("Cadance Sensor"),
                                        selected.contains("Heartrate Sensor"),
                                        selected.contains("Speed Sensor"));
                                mFloatingActionButton.setImageResource(R.drawable.ic_stop_white_36dp);
                            } catch (NotImplementedException | SecurityException nie) {
                                nie.printStackTrace();
                            } catch (NoDeviceConfiguredException ndce) {
                                showNoDeviceConfiguredExceptionSnackbar();
                                mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
                            } catch (LocationIsDisabledException lide) {
                                showLocationDisabledExceptionSnackbar();
                                mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
                            } catch (NoLocationPermissionGiven noLocationPermissionGiven) {
                                showLocationPermissionMissingExceptionSnackbar();
                                noLocationPermissionGiven.printStackTrace();
                            }
                            return true;
                        }
                    })
                    .positiveText("Start Recording")
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_dashboard_content, R.id.nav_dashboard);
		ButterKnife.bind(this);
		setTitle("Dashboard");
        mRideRecordingGuiUpdater = new RideRecordingGuiUpdater();
        mRideRecordingServiceConnection = new RideRecordingServiceConnection();
        mRideRecordingIntent = new Intent(this, RideRecordingService.class);
        bindService(mRideRecordingIntent, mRideRecordingServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        if (mRideRecordingService != null) {
            if (!mRideRecordingService.isRecording()) {
                super.onBackPressed();
            }
            else{
               startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
        else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        //1. Disconnect the RideRecording service
        if (mRideRecordingService != null) {
            if (!mRideRecordingService.isRecording()) {
                stopService(mRideRecordingIntent);
            }
        }
        unbindService(mRideRecordingServiceConnection);
        //2. Destroy Activity
        Log.d(LOGTAG, "onDestroy");
        super.onDestroy();
    }



    private void showNoDeviceConfiguredExceptionSnackbar() {
        //TODO move text to strings.xml
        Snackbar snackbar = Snackbar.make(mDashboardLayout, "Not all sensors have been configured.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Fix This", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(DashboardActivity.this, AntSensorActivity.class);
                startActivity(viewIntent);
            }
        });
        snackbar.show();
    }

    private void showLocationDisabledExceptionSnackbar() {
        //TODO move text to strings.xml
        Snackbar snackbar = Snackbar.make(mDashboardLayout, "Location is not enabled in settings.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Fix This", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        snackbar.show();
    }

    private void showLocationPermissionMissingExceptionSnackbar() {
        final Activity activity = this;
        //TODO move text to strings.xml
        Snackbar snackbar = Snackbar.make(mDashboardLayout, "No location permission.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Fix This", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(activity).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(activity, "Thanks! You can now use the recorder.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(activity, "You cant use the recorder until you give the permission.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        final PermissionToken token1 = token;
                        Snackbar snackbar1 = Snackbar.make(mDashboardLayout, "We need the permission to see where you go.", Snackbar.LENGTH_LONG);
                        snackbar1.setAction("Fix It.", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                token1.continuePermissionRequest();
                            }
                        }).show();
                    }
                }).check();
            }
        }).show();
    }


    public class RideRecordingGuiUpdater implements IRideRecordingGuiUpdate {
        @Override
        public void onNewRequestedGuiUpdate(final DashboardGuiUpdateStates updateState, final RideMeasurement rideMeasurement) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (updateState) {
                        case STARTED_RECORDING:
                            mStatusText.setText(R.string.dashboard_activity_status_started_recording);
                            break;
                        case STOPPED_RECORDING:
                            mStatusText.setText(R.string.dashboard_activity_status_stopped_recording);
                            break;
                        case NEW_MEASUREMENT:
                            AntPlusSpeedSensorData speedSensorData = rideMeasurement.getSpeedSensorData();
                            if (speedSensorData == null) {
                                mRealtimeDistance.setText(R.string.realtime_display_not_connected);
                                mRealtimeSpeed.setText(R.string.realtime_display_not_connected);
                            } else {
                                mRealtimeSpeed.setText(String.valueOf(Constants.convertMStoKMS(speedSensorData.getSpeedInMeterPerSecond())));
                                mRealtimeDistance.setText(String.valueOf(speedSensorData.getCalcAccumulatedDistanceInMeters()/1000));
                            }
                            AntPlusCadenceSensorData cadenceSensorData = rideMeasurement.getCadenceSensorData();
                            if (cadenceSensorData == null) {
                                mRealtimeCadance.setText(R.string.realtime_display_not_connected);
                            } else {
                                mRealtimeCadance.setText(String.valueOf(cadenceSensorData.getCalculatedCadence()));
                            }
                            AntPlusHeartSensorData heartSensorData = rideMeasurement.getHeartSensorData();
                            if (heartSensorData == null) {
                                mRealtimeHeartrate.setText(R.string.realtime_display_not_connected);
                            } else {
                                mRealtimeHeartrate.setText(String.valueOf(heartSensorData.getHeartRate()));
                            }
                            AntPlusPowerSensorData powerSensorData = rideMeasurement.getPowerSensorData();
                            if (powerSensorData == null) {
                                mRealtimePower.setText(R.string.realtime_display_not_connected);
                            } else {
                                mRealtimePower.setText(String.valueOf(powerSensorData.getCalculatedPower()));
                            }
                            break;
                    }
                }
            });


        }

        @Override
        public SupportMapFragment getGoogleMap() {
            return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        }
    }
}
