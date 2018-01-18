package nl.easthome.ebikereader.Implementations;
import com.google.android.gms.maps.SupportMapFragment;

import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;
import nl.easthome.ebikereader.Activities.DashboardActivity;
import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Interfaces.IRideRecordingGuiUpdate;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.R;

public class RideRecordingGuiUpdater implements IRideRecordingGuiUpdate {

    DashboardActivity mDashboardActivity;

    public RideRecordingGuiUpdater(DashboardActivity dashboardActivity) {
        mDashboardActivity = dashboardActivity;
    }

    @Override
    public void onNewRequestedGuiUpdate(final DashboardGuiUpdateStates updateState, final RideMeasurement rideMeasurement) {
        mDashboardActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (updateState) {
                    case STARTED_RECORDING:
                        mDashboardActivity.getStatusText().setText(R.string.dashboard_activity_status_started_recording);
                        break;
                    case STOPPED_RECORDING:
                        mDashboardActivity.getStatusText().setText(R.string.dashboard_activity_status_stopped_recording);
                        break;
                    case NEW_MEASUREMENT:
                        AntPlusSpeedSensorData speedSensorData = rideMeasurement.getSpeedSensorData();
                        if (speedSensorData == null) {
                            mDashboardActivity.getRealtimeDistance().setText(R.string.realtime_display_not_connected);
                            mDashboardActivity.getRealtimeSpeed().setText(R.string.realtime_display_not_connected);
                        } else {
                            mDashboardActivity.getRealtimeSpeed().setText(String.valueOf(Constants.convertMStoKMS(speedSensorData.getSpeedInMeterPerSecond())));
                            mDashboardActivity.getRealtimeDistance().setText(String.valueOf(speedSensorData.getCalcAccumulatedDistanceInMeters()/1000));
                        }
                        AntPlusCadenceSensorData cadenceSensorData = rideMeasurement.getCadenceSensorData();
                        if (cadenceSensorData == null) {
                            mDashboardActivity.getRealtimeCadance().setText(R.string.realtime_display_not_connected);
                        } else {
                            mDashboardActivity.getRealtimeCadance().setText(String.valueOf(cadenceSensorData.getCalculatedCadence()));
                        }
                        AntPlusHeartSensorData heartSensorData = rideMeasurement.getHeartSensorData();
                        if (heartSensorData == null) {
                            mDashboardActivity.getRealtimeHeartrate().setText(R.string.realtime_display_not_connected);
                        } else {
                            mDashboardActivity.getRealtimeHeartrate().setText(String.valueOf(heartSensorData.getHeartRate()));
                        }
                        AntPlusPowerSensorData powerSensorData = rideMeasurement.getPowerSensorData();
                        if (powerSensorData == null) {
                            mDashboardActivity.getRealtimePower().setText(R.string.realtime_display_not_connected);
                        } else {
                            mDashboardActivity.getRealtimePower().setText(String.valueOf(powerSensorData.getCalculatedPower()));
                        }
                        break;
                }
            }
        });


    }

    @Override
    public SupportMapFragment getGoogleMap() {
        return (SupportMapFragment) mDashboardActivity.getSupportFragmentManager().findFragmentById(R.id.map);
    }
}
