package nl.easthome.ebikereader.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.AntPlusDeviceManager;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.ebikereader.Activities.DashboardActivity;
import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGivenException;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.SharedPrefsSaver;
import nl.easthome.ebikereader.Implementations.RideRecordingMappingHelper;
import nl.easthome.ebikereader.Implementations.Sensors.EBikeCadenceSensorImplementation;
import nl.easthome.ebikereader.Implementations.Sensors.EBikeHeartSensorImplementation;
import nl.easthome.ebikereader.Implementations.Sensors.EBikePowerSensorImplementation;
import nl.easthome.ebikereader.Implementations.Sensors.EBikeSpeedSensorImplementation;
import nl.easthome.ebikereader.Interfaces.IRideRecordingGuiUpdate;
import nl.easthome.ebikereader.Objects.AntPlusSensorList;
import nl.easthome.ebikereader.Objects.EstimatedPowerData;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.Objects.UserDetails;
import nl.easthome.ebikereader.R;

public class RideRecordingService extends Service {
    private static final String LOGTAG =  "RideRecordingService";
    private static int mNumberOfBoundClients = 0;
    private static boolean mIsRecording = false;
    private int mNotificationID = R.string.notification_id;
    private IBinder mBinder = new RideRecordingBinder();
    private IRideRecordingGuiUpdate mRideRecordingGuiUpdate;
    private RideRecording mRideRecording;
    private AntPlusSensorList mAntPlusSensorList = new AntPlusSensorList();
    private Activity mStartedFromActivity;
    private RideRecordingMappingHelper mRideRecordingMappingHelper;

    public RideRecordingService() {
    }
    @Override public void onCreate() {
        Log.d(LOGTAG,"OnCreate");
	}
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOGTAG,"OnStartCommand");
        return START_STICKY;
    }
    @Override public void onDestroy() {
        Log.d(LOGTAG,"OnDestroy");
        if (isRecording()){
            stopRecording();
            stopForeground(true);
        }
	}
    @Nullable @Override public IBinder onBind(Intent intent) {
        mNumberOfBoundClients++;
        Log.d(LOGTAG,"OnBind " + String.valueOf(mNumberOfBoundClients));
        return mBinder;
    }
    @Override public boolean onUnbind(Intent intent) {
        Log.d(LOGTAG,"OnUnbind");
        mNumberOfBoundClients--;
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOGTAG, "OnRebind");
        super.onRebind(intent);
    }

    /**
     * Shows a notification to indicate that a recording is running.
     *
     * @return Notification object.
     */
    private Notification showNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationID, new Intent(this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_id_channel));
        Notification notification = builder
                .setContentTitle(getString(R.string.recording_notification_title))
                .setContentText(getString(R.string.recording_notification_text))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_action_record)
                .setWhen(Constants.getSystemTimestamp())
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    /**
     * Method for binded clients, starts the recording.
     */
    public void startRecording(Activity activity, IRideRecordingGuiUpdate guiUpdater, boolean recordPowerSensor, boolean recordCadenceSensor, boolean recordHeartSensor, boolean recordSpeedSensor) throws NoDeviceConfiguredException, SecurityException, LocationIsDisabledException, NoLocationPermissionGivenException {
        mStartedFromActivity = activity;
        mRideRecordingGuiUpdate = guiUpdater;
        boolean startupSucceeded = false;
        try {
            mRideRecordingMappingHelper = new RideRecordingMappingHelper(mStartedFromActivity, mRideRecordingGuiUpdate.getGoogleMap(), this);
            if (recordPowerSensor || recordCadenceSensor || recordHeartSensor || recordSpeedSensor) {
                AntPlusDeviceManager mDeviceConnector = new AntPlusDeviceManager(mStartedFromActivity);
                if (recordPowerSensor) {
                    mAntPlusSensorList.setAntPlusPowerSensor(new AntPlusConnectedSensor<>(DeviceType.BIKE_POWER, new EBikePowerSensorImplementation(mStartedFromActivity, mDeviceConnector.getDeviceIdForType(DeviceType.BIKE_POWER))));
                }
                if (recordCadenceSensor) {
                    mAntPlusSensorList.setAntPlusCadenceSensor(new AntPlusConnectedSensor<>(DeviceType.BIKE_CADENCE, new EBikeCadenceSensorImplementation(mStartedFromActivity, mDeviceConnector.getDeviceIdForType(DeviceType.BIKE_CADENCE))));
                }
                if (recordHeartSensor) {
                    mAntPlusSensorList.setAntPlusHeartSensor(new AntPlusConnectedSensor<>(DeviceType.HEARTRATE, new EBikeHeartSensorImplementation(mStartedFromActivity, mDeviceConnector.getDeviceIdForType(DeviceType.HEARTRATE))));
                }
                if (recordSpeedSensor) {
                    mAntPlusSensorList.setAntPlusSpeedSensor(new AntPlusConnectedSensor<>(DeviceType.BIKE_SPD, new EBikeSpeedSensorImplementation(mStartedFromActivity, mDeviceConnector.getDeviceIdForType(DeviceType.BIKE_SPD), SharedPrefsSaver.getWheelCircumference(mStartedFromActivity))));
                }
            }
            mRideRecordingMappingHelper.startLocationServices();
            mRideRecording = new RideRecording();
            mRideRecording.startRide();
            mIsRecording = true;
            mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.STARTED_RECORDING, null);
            startForeground(mNotificationID, showNotification());
            startupSucceeded = true;
            Log.d(LOGTAG, "RecordingStart");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (!startupSucceeded) {
                stopRecording();
            }
        }

    }

    /**
     * Method for binded clients, stops the recording.
     */
    public void stopRecording() {
        //1. Inform log and user of stopping.
        stopForeground(true);
        Log.d(LOGTAG, "RecordingStop");

        //2. Stop Measurement Logging
        if (mIsRecording) {
            mIsRecording = false;
            mRideRecording.stopRide();
            mRideRecording = null;
        }
        //3. Disconnect Sensors
        mAntPlusSensorList.disconnectAllConnectedSensors();
        //4. Stop Gui Component Updates
        mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.STOPPED_RECORDING, null);
        //5. Stop Location Updates
        mRideRecordingMappingHelper.stopLocationServices();
    }

    public boolean isRecording(){
        return mIsRecording;
    }

    public void addRideMeasurement(RideMeasurement rideMeasurement, long timestamp) {
        Log.d(LOGTAG, "New measurement added at timestamp: " + String.valueOf(timestamp));
        rideMeasurement.dosetTimestamp(timestamp);
        rideMeasurement = mAntPlusSensorList.addRideMeasurementDataOfAllConnectedSensors(rideMeasurement);

        if (rideMeasurement.getHeartSensorData() != null && rideMeasurement.getPowerSensorData() != null) {
            UserDetails userDetails = SharedPrefsSaver.getAllUserDetails(mStartedFromActivity);
            rideMeasurement.setEstimatedPowerData(new EstimatedPowerData(rideMeasurement.getHeartSensorData().getHeartRate(), userDetails.getUserAge(), userDetails.getUserWeight(), userDetails.getUserGender(), rideMeasurement.getPowerSensorData().getCalculatedPower()));
        }

        mRideRecording.addRideMeasurement(timestamp, rideMeasurement);
        mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.NEW_MEASUREMENT, rideMeasurement);
    }



    public class RideRecordingBinder extends Binder {
        public RideRecordingService getService() {
            return RideRecordingService.this;
        }
    }
}
