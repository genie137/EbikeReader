package nl.easthome.ebikereader.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Dialogs.FixLocationSnackbarOnClick;
import nl.easthome.ebikereader.Dialogs.StartRecordingDialog;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.Implementations.RideRecordingServiceConnection;
import nl.easthome.ebikereader.R;
import nl.easthome.ebikereader.Services.RideRecordingService;

/**
 * Activity which shows the dashboard and starts/stops the recording.
 * @BindView is part of the Butterknife libary
 */
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

    @OnClick(R.id.fab) public void onClickRecordingToggle() { onRecordingToggleButton();}

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

    /**
     * Activity onBackPressed, gets executed if the back button was pressed.
     * If a ride is running the activity will move to background and goes to home.
     * Else the activity gets destroyed.
     */
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

    /**
     * Activity onDestroy, gets executed when the activity is destroyed.
     * Stops the recording if its still running.
     */
    @Override
    protected void onDestroy() {
        if (mRideRecordingService != null) {
            if (!mRideRecordingService.isRecording()) {
                stopService(mRideRecordingIntent);
            }
        }
        unbindService(mRideRecordingServiceConnection);
        super.onDestroy();
    }

    /**
     * Perform actions when the recording button was pressed.
     * If already recording, it stops the recording. Else it starts the recording by showing the start recording dialog.
     */
    private void onRecordingToggleButton() {
        mRideRecordingService = mRideRecordingServiceConnection.getRideRecordingService();
        if (mRideRecordingService.isRecording()) {
            mRideRecordingService.stopRecording();
            mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
        } else {
            new StartRecordingDialog(this).show();
        }
    }

    /**
     * Shows a Snackbar explaining the error if a sensor wants to be recorded, but has no ID saved.
     */
    public void showNoDeviceConfiguredExceptionSnackbar() {
        Snackbar snackbar = Snackbar.make(mDashboardLayout, R.string.snackbar_not_all_sensors_configured, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_button_fix_this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(DashboardActivity.this, AntSensorActivity.class);
                startActivity(viewIntent);
            }
        });
        snackbar.show();
    }

    /**
     * Shows a Snackbar explaining the error that Location is disabled in android settings.
     */
    public void showLocationDisabledExceptionSnackbar() {
        Snackbar snackbar = Snackbar.make(mDashboardLayout, R.string.snackbar_location_not_enabled_in_settings, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_button_fix_this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        snackbar.show();
    }

    /**
     * Shows a snackbar explaining the error that no Location permission was given.
     * TODO TEST new onclick
     */
    public void showLocationPermissionMissingExceptionSnackbar() {
        Snackbar snackbar = Snackbar.make(mDashboardLayout, R.string.snackbar_no_location_permission, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_button_fix_this, new FixLocationSnackbarOnClick(this, mDashboardLayout)).show();
    }

    //Getters and setters

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
