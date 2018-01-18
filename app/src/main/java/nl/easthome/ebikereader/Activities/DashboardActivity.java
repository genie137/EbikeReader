package nl.easthome.ebikereader.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Dialogs.StartRecordingDialog;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.Implementations.RideRecordingServiceConnection;
import nl.easthome.ebikereader.R;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class DashboardActivity extends BaseActivityWithMenu {
    @BindView(R.id.dashboard_layout) ConstraintLayout mDashboardLayout;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.dashboard_statustext) TextView mStatusText;
    @BindView(R.id.realtime_cadance) TextView mRealtimeCadance;
    @BindView(R.id.realtime_distance) TextView mRealtimeDistance;
    @BindView(R.id.realtime_heartrate) TextView mRealtimeHeartrate;
    @BindView(R.id.realtime_power) TextView mRealtimePower;
    @BindView(R.id.realtime_speed_kmh) TextView mRealtimeSpeed;
    private RideRecordingServiceConnection mRideRecordingServiceConnection;
    private Intent mRideRecordingIntent;
    private RideRecordingService mRideRecordingService;

    @OnClick(R.id.fab) public void onStartRecordingButtonPress() { pressedStartRecording();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_dashboard_content, R.id.nav_dashboard);
		ButterKnife.bind(this);
		setTitle(getString(R.string.activity_title_dashboard));
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
        super.onDestroy();
    }

    private void pressedStartRecording() {
        mRideRecordingService = mRideRecordingServiceConnection.getRideRecordingService();
        if (mRideRecordingService.isRecording()) {
            mRideRecordingService.stopRecording();
            mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
        } else {
            new StartRecordingDialog(this).show();
        }
    }

    public void showNoDeviceConfiguredExceptionSnackbar() {
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

    public void showLocationDisabledExceptionSnackbar() {
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

    public void showLocationPermissionMissingExceptionSnackbar() {
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

    public RideRecordingService getRideRecordingService() {
        return mRideRecordingService;
    }

    public FloatingActionButton getFloatingActionButton() {
        return mFloatingActionButton;
    }

    public TextView getStatusText() {
        return mStatusText;
    }

    public TextView getRealtimeCadance() {
        return mRealtimeCadance;
    }

    public TextView getRealtimeDistance() {
        return mRealtimeDistance;
    }

    public TextView getRealtimeHeartrate() {
        return mRealtimeHeartrate;
    }

    public TextView getRealtimePower() {
        return mRealtimePower;
    }

    public TextView getRealtimeSpeed() {
        return mRealtimeSpeed;
    }

}
