package nl.easthome.ebikereader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGiven;
import nl.easthome.ebikereader.Helpers.UserLogout;
import nl.easthome.ebikereader.Implementations.RideRecordingServiceConnection;
import nl.easthome.ebikereader.Interfaces.IRideRecordingGuiUpdate;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String LOGTAG = "DashboardActivity";
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.dashboard_layout) ConstraintLayout mDashboardLayout;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.dashboard_measurementtext) TextView mMeasurementText;
    @BindView(R.id.dashboard_statustext) TextView mStatusText;
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
            try {
                mRideRecordingService.startRecording(this, mRideRecordingGuiUpdater);
                //populatePieChart();
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //0. Android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
		ButterKnife.bind(this);
        //1. Setup the menu
        onCreateSetupNav();
        //2. Apply some viewsize changes for the arch
        //3. Connect to RideRecording service
        mRideRecordingGuiUpdater = new RideRecordingGuiUpdater();
        mRideRecordingServiceConnection = new RideRecordingServiceConnection();
        mRideRecordingIntent = new Intent(this, RideRecordingService.class);
        bindService(mRideRecordingIntent, mRideRecordingServiceConnection, Context.BIND_AUTO_CREATE);
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
    @Override public void onBackPressed() {
        //TODO BUG When back is pressed, app is not returned to previous activity
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mRideRecordingService != null && mRideRecordingService.isRecording()) {
                new MaterialDialog.Builder(this)
                        .title(R.string.dialog_quit_while_recording_title)
                        .content(R.string.dialog_quit_while_recording_content)
                        .positiveText(R.string.dialog_quit_while_recording_positive_button)
                        .negativeText(R.string.dialog_quit_while_recording_negative_button)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DashboardActivity.super.onBackPressed();
                            }
                        })
                        .show();
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

		if (id == R.id.nav_ant_sensors) {
		    startActivity(new Intent(this, AntSensorActivity.class));
        } else if (id == R.id.nav_logout) {
            UserLogout.showUserLogoutDialogs(this);
        }

		mDrawerLayout.closeDrawer(GravityCompat.START);
		return true;
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

	private void onCreateSetupNav() {
		setSupportActionBar(mToolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		mNavigationView.setNavigationItemSelectedListener(this);
	}

    public class RideRecordingGuiUpdater implements IRideRecordingGuiUpdate {
        @Override
        public void onNewRequestedGuiUpdate(DashboardGuiUpdateStates updateState, final RideMeasurement rideMeasurement) {
            switch (updateState) {
                case STARTED_RECORDING:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mStatusText.setText("STATUS: Started Recording");
                        }
                    });
                    break;
                case STOPPED_RECORDING:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mStatusText.setText("STATUS: Stopped Recording");
                        }
                    });
                    break;
                case NEW_MEASUREMENT:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMeasurementText.setText(rideMeasurement.toString());
                        }
                    });
                    break;
            }
        }

        @Override
        public SupportMapFragment getGoogleMap() {
            return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        }
    }
}
