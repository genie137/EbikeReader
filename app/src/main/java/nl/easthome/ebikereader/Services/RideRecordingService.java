package nl.easthome.ebikereader.Services;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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
    private LocationRequest mLocationRequest = new LocationRequest().setInterval(Constants.MAX_LOCATION_INTERVAL_MS).setFastestInterval(Constants.MIN_LOCATION_INTERVAL_MS).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private IBinder mBinder = new RideRecordingBinder();
    private IRideRecordingGuiUpdate mRideRecordingGuiUpdate;
    private RideRecording mRideRecording;
    private FusedLocationProviderClient mFusedLocationClient;
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
            if (checkLocationDeviceState()) {
                //1. Start Gui Component Updates
                mRideRecordingMappingHelper = new RideRecordingMappingHelper(mRideRecordingGuiUpdate.getGoogleMap(), this);
                //2. Start Location Services
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mStartedFromActivity);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mRideRecordingMappingHelper, null);
                //3. Start Sensor Connection
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
                //4. Start Measurement Logging
                mRideRecording = new RideRecording();
                mRideRecording.startRide();
                mIsRecording = true;
                //5. Inform log, user
                mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.STARTED_RECORDING, null);
                startForeground(mNotificationID, showNotification());
                startupSucceeded = true;
                Log.d(LOGTAG, "RecordingStart");
            }
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
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mRideRecordingMappingHelper);
        }
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

    public boolean checkLocationDeviceState() throws LocationIsDisabledException, NoLocationPermissionGivenException {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        //Check for network location permission
        if (ActivityCompat.checkSelfPermission(mStartedFromActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check for gps sensor location permission
            if (ActivityCompat.checkSelfPermission(mStartedFromActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Check if gps or network provider is enabled
                if (mLocationManager != null) {
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        return true;
                    } else {
                        throw new LocationIsDisabledException();
                    }
                }
            } else {
                throw new NoLocationPermissionGivenException();
            }
        } else {
            throw new NoLocationPermissionGivenException();
        }
        return false;
    }

    public class RideRecordingBinder extends Binder {
        public RideRecordingService getService() {
            return RideRecordingService.this;
        }
    }
}
