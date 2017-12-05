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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;

import nl.easthome.antpluslibary.AntPlusDeviceManager;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.antpluslibary.Objects.AntPlusSensorList;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGiven;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Helpers.SystemTime;
import nl.easthome.ebikereader.Implementations.RideRecordingMappingHelper;
import nl.easthome.ebikereader.Interfaces.IRideRecordingGuiUpdate;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.Objects.UserDetails;
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
    private IRideRecordingGuiUpdate mRideRecordingGuiUpdate;
    private RideRecording mRideRecording;
    private FusedLocationProviderClient mFusedLocationClient;
    private AntPlusSensorList mAntPlusSensorList = new AntPlusSensorList();
    private Activity mStartedFromActivity;
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
    public void startRecording(Activity activity, IRideRecordingGuiUpdate guiUpdater) throws NoDeviceConfiguredException, NotImplementedException, SecurityException, LocationIsDisabledException, NoLocationPermissionGiven {
        mStartedFromActivity = activity;
        mRideRecordingGuiUpdate = guiUpdater;
        boolean startupSucceeded = false;
        try {
            if (checkLocationDeviceState()) {
                //1. Start Gui Component Updates
                mRideRecordingMappingHelper = new RideRecordingMappingHelper(mRideRecordingGuiUpdate.getGoogleMap(), this);
                mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.STARTED_RECORDING, null);
                //2. Start Location Services
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mStartedFromActivity);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mRideRecordingMappingHelper, null);
                //3. Start Sensor Connection

                mDeviceConnector = new AntPlusDeviceManager(mStartedFromActivity);
                mAntPlusSensorList.setAntPlusPowerSensor(mDeviceConnector.initConnectionToPowerSensor(new EBikePowerSensorImplementation()));
                mAntPlusSensorList.setAntPlusCadenceSensor(mDeviceConnector.initConnectionToCadenceSensor(new EBikeCadenceSensorImplementation()));
                mAntPlusSensorList.setAntPlusHeartSensor(mDeviceConnector.initConnectionToHeartRateSensor(new EBikeHeartSensorImplementation()));
                mAntPlusSensorList.setAntPlusSpeedSensor(mDeviceConnector.initConnectionToSpeedSensor(new EBikeSpeedSensorImplementation(BigDecimal.valueOf(getWheelCircumference()))));
                //4. Start Measurement Logging
                mRideRecording = new RideRecording();
                mRideRecording.startRide();
                mIsRecording = true;
                //5. Inform log, user and finally block
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

    private double getWheelCircumference() {
        //TODO figure out a way to pass directly.
        final UserDetails[] userDetails = new UserDetails[1];

        FirebaseSaver.getUserDetails(FirebaseAuth.getInstance().getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDetails[0] = dataSnapshot.getValue(UserDetails.class);
                System.out.println(userDetails[0].toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return 2.07;
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
        if (mDeviceConnector != null) {
            mDeviceConnector.disconnectAllSensors();
        }
        //4. Stop Gui Component Updates
        //TODO reset map when previous rides can be viewed.
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
        rideMeasurement.setSpeedSensorData(mAntPlusSensorList.getAntPlusSpeedSensor().getLastSensorData());
        rideMeasurement.setCadenceSensorData(mAntPlusSensorList.getAntPlusCadenceSensor().getLastSensorData());
        rideMeasurement.setPowerSensorData(mAntPlusSensorList.getAntPlusPowerSensor().getLastSensorData());
        rideMeasurement.setHeartSensorData(mAntPlusSensorList.getAntPlusHeartSensor().getLastSensorData());
        mRideRecording.addRideMeasurement(timestamp, rideMeasurement);
        mRideRecordingGuiUpdate.onNewRequestedGuiUpdate(DashboardGuiUpdateStates.NEW_MEASUREMENT, rideMeasurement);
    }

    public boolean checkLocationDeviceState() throws LocationIsDisabledException, NoLocationPermissionGiven {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        //Check for network location permission
        if (ActivityCompat.checkSelfPermission(mStartedFromActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check for gps sensor location permission
            if (ActivityCompat.checkSelfPermission(mStartedFromActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Check if gps or network provider is enabled
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    return true;
                } else {
                    throw new LocationIsDisabledException();
                }
            } else {
                throw new NoLocationPermissionGiven();
            }
        } else {
            throw new NoLocationPermissionGiven();
        }
    }

    public class RideRecordingBinder extends Binder {
        public RideRecordingService getService() {
            return RideRecordingService.this;
        }
    }
}
