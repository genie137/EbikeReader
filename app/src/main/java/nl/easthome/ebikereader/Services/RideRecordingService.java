package nl.easthome.ebikereader.Services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.math.BigDecimal;

import nl.easthome.antpluslibary.AntPlusDeviceManager;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.antpluslibary.Objects.AntPlusSensorList;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.Helpers.SystemTime;
import nl.easthome.ebikereader.Implementations.RideRecordingMappingHelper;
import nl.easthome.ebikereader.Objects.Ride;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.R;
import nl.easthome.ebikereader.Sensors.EBikeCadenceSensorImplementation;
import nl.easthome.ebikereader.Sensors.EBikeHeartSensorImplementation;
import nl.easthome.ebikereader.Sensors.EBikePowerSensorImplementation;
import nl.easthome.ebikereader.Sensors.EBikeSpeedSensorImplementation;

public class RideRecordingService extends Service {
    private static final String LOGTAG =  "RideRecordingService";
    private static int mNumberOfBoundClients = 0;
    private static boolean mIsRecording = false;
    private int mNotificationID = R.string.notification_id;
    private LocationRequest mLocationRequest = new LocationRequest().setInterval(5000).setFastestInterval(3000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private IBinder mBinder = new RideRecordingBinder();
    private Ride mRide;
    private FusedLocationProviderClient mFusedLocationClient;
    private AntPlusSensorList mAntPlusSensorList = new AntPlusSensorList();
    private DashboardActivity mActivity;
    private RideRecordingMappingHelper mRideRecordingMappingHelper;
    private AntPlusDeviceManager mDeviceConnector;

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

    private Notification showNotification() {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationID, new Intent(this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_id_channel));
		Notification notification = builder
				.setContentTitle(getString(R.string.recording_notification_title))
				.setContentText(getString(R.string.recording_notification_text))
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_action_record)
                .setWhen(SystemTime.getSystemTimestamp())
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		return notification;
	}

    /**
     * Method for binded clients, starts the recording.
     * @return true if started correctly
     */
	public boolean startRecording(DashboardActivity activity) throws NoDeviceConfiguredException, NotImplementedException, SecurityException {
        mActivity = activity;
        if (!mIsRecording){
            setUpGuiComponents();
            startRecordingActivities();
            startForeground(mNotificationID, showNotification());
            return true;
            }
        else {
            return false;
        }
    }

    private void setUpGuiComponents() {
        mRideRecordingMappingHelper = new RideRecordingMappingHelper(mActivity, this);
    }

    /**
     * Method for binded clients, stops the recording.
     */
    public void stopRecording() {
        if (mIsRecording){
            stopRecordingActivities();
            mActivity = null;
            stopForeground(true);
        }
    }

    private void startRecordingActivities() throws NoDeviceConfiguredException, NotImplementedException, SecurityException {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("No permission for location was given.");
        } else {
            try {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mRideRecordingMappingHelper, null);
                startSensors();
                mRide = new Ride();
                mRide.startRide();
                mIsRecording = true;
                Log.d(LOGTAG, "RecordingStart");
            } catch (Exception e){
                e.printStackTrace();
                stopRecordingActivities();
                throw e;
            }
        }
    }

    private void startSensors() throws NotImplementedException, NoDeviceConfiguredException {
        mDeviceConnector = new AntPlusDeviceManager(mActivity);
        mAntPlusSensorList.setAntPlusPowerSensor(mDeviceConnector.initConnectionToPowerSensor(new EBikePowerSensorImplementation()));
        mAntPlusSensorList.setAntPlusCadenceSensor(mDeviceConnector.initConnectionToCadenceSensor(new EBikeCadenceSensorImplementation()));
        mAntPlusSensorList.setAntPlusHeartSensor(mDeviceConnector.initConnectionToHeartRateSensor(new EBikeHeartSensorImplementation()));
        mAntPlusSensorList.setAntPlusSpeedSensor(mDeviceConnector.initConnectionToSpeedSensor(new EBikeSpeedSensorImplementation(BigDecimal.valueOf(2.07))));
    }

    private void stopSensors() {
        mDeviceConnector.disconnectAllSensors();
    }

    private void stopRecordingActivities(){
        if (mRide != null){
            mRide.stopRide();

        }
//        if (mRideRecordingMappingHelper != null){
//            mRideRecordingMappingHelper.removePolyLine();
//        }

        stopSensors();
        mFusedLocationClient.removeLocationUpdates(mRideRecordingMappingHelper);
        mIsRecording = false;
        Log.d(LOGTAG, "RecordingStop");
    }

    public boolean isRecording(){
        return mIsRecording;
    }

    public void addRideMeasurement(RideMeasurement rideMeasurement, long timestamp) {
        rideMeasurement.setSpeedSensorData(mAntPlusSensorList.getAntPlusSpeedSensor().getLastSensorData());
        rideMeasurement.setCadenceSensorData(mAntPlusSensorList.getAntPlusCadenceSensor().getLastSensorData());
        Log.d(LOGTAG, "New measurement added at timestamp: " + String.valueOf(timestamp));
        mRide.addRideMeasurement(rideMeasurement);
    }

    public class RideRecordingBinder extends Binder {
        public RideRecordingService getService() {
            return RideRecordingService.this;
        }
    }

}
