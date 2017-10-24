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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import java.util.List;

import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.Objects.Ride;
import nl.easthome.ebikereader.R;

public class RideRecordingService extends Service {
    protected static boolean mArePermissionsGranted = false;
    private static final String mLogTag =  "RideRecordingService";
	private int mNotificationID = R.string.notification_id;
    private LocationRequest mLocationRequest = new LocationRequest().setInterval(5000).setFastestInterval(3000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private IBinder mBinder = new RideRecordingBinder();
    private boolean mIsRecording = false;
    private Ride mRide;
    private FusedLocationProviderClient mFusedLocationClient;
    private RideRecordingLocationCallback mLocationCallback;
    private DashboardActivity mActivity;

    public RideRecordingService() {
    }
    @Override public void onCreate() {
        Log.d(mLogTag,"OnCreate");
        mLocationCallback = new RideRecordingLocationCallback();
	}
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(mLogTag,"OnStartCommand");
        return START_NOT_STICKY;
    }
    @Override public void onDestroy() {
        Log.d(mLogTag,"OnDestroy");
		stopForeground(true);
	}
    @Nullable @Override public IBinder onBind(Intent intent) {
        Log.d(mLogTag,"OnBind");
        return mBinder;
    }
    private Notification showNotification() {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationID, new Intent(this, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_id_channel));
		Notification notification = builder
				.setContentTitle(getString(R.string.recording_notification_title))
				.setContentText(getString(R.string.recording_notification_text))
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_action_record)
				.setWhen(System.currentTimeMillis())
				.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		return notification;
	}

    /**
     * Method for binded clients, starts the recording.
     * @return true if started correctly
     */
	public boolean startRecording(DashboardActivity activity) {
        mActivity = activity;
        if (!mIsRecording){
            startRecordingActivities();
            if (mArePermissionsGranted){
                startForeground(mNotificationID, showNotification());
                return true;
            }
            else {
                stopRecordingActivities();
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Method for binded clients, stops the recording.
     * @return true if stopped correctly
     */
    public boolean stopRecording() {
        if (mIsRecording){
            stopRecordingActivities();
            mActivity = null;
            stopForeground(true);
            return true;
        }
        else {
            return false;
        }
    }

    private void startRecordingActivities(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withActivity(mActivity)
                    .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new CompositeMultiplePermissionsListener(
                            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                    .with(mActivity.getRootView(), R.string.permission_ride_title)
                                    .withOpenSettingsButton(R.string.permission_ride_button)
                                    .build(),
                            new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        mArePermissionsGranted = true;
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }))
                    .check();
        } else {
            mArePermissionsGranted =true;
        }

        if (mArePermissionsGranted) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            mRide = new Ride();
            mRide.startRide();
            mIsRecording = true;
            Log.d(mLogTag, "RecordingStart");
        }
    }

    private void stopRecordingActivities(){
        if (mRide != null){
            mRide.stopRide();
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mIsRecording = false;
        Log.d(mLogTag, "RecordingStop");
    }

    public boolean isRecording(){
        return mIsRecording;
    }

	public class RideRecordingBinder extends Binder {
        public RideRecordingService getService() {
            return RideRecordingService.this;
        }
    }
    public class RideRecordingLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(mLogTag, "onLocationResult");
            super.onLocationResult(locationResult);
        }
    }



}