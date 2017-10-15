package nl.easthome.ebikereader.Services;

import android.location.Location;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.Helpers.DisplayNotification;
import nl.easthome.ebikereader.Objects.Ride;
import nl.easthome.ebikereader.Objects.RideMeasurement;

public class RideRecorderService {
	private static GPSLocationService mGPSLocationService;
	private static String mRideOwnerUid;
	private static Ride mRide;
	private static int mNotificationID;
	private boolean mIsRecording = false;
	private DashboardActivity mDashboardActivity;


	public RideRecorderService(DashboardActivity dashboardActivity) {
		mDashboardActivity = dashboardActivity;
		mRideOwnerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		mGPSLocationService = new GPSLocationService(this);
	}

	public void toggleRecording() {
		if (!mIsRecording) {
			Log.d("RECORDING", "-->> Starting recording.");
			mRide = new Ride();
			mRide.startRide();
			FirebaseDatabase.getInstance().getReference("UserRides").child(mRideOwnerUid).child(mRide.getRideId()).setValue("x");
			mNotificationID = new DisplayNotification(mDashboardActivity).displayOngoingNotification();
			mIsRecording = true;
			mGPSLocationService.startLocationUpdates();

		} else {
			Log.d("RECORDING", "-->> Stopping recording.");
			mGPSLocationService.stopLocationUpdates();
			mRide.stopRide();
			new DisplayNotification(mDashboardActivity).cancelNotification(mNotificationID);
			mIsRecording = false;
		}
	}

	public void onNewLocation(Location location) {
		Log.d("RECORDING", "-->> Received new measurement.");
		RideMeasurement measurement = new RideMeasurement(location);
		mDashboardActivity.updateViews(measurement);
		mRide.addRideMeasurement(measurement);
	}

	public void restoreRecording() {
		mDashboardActivity.restoreViews(mRide);
	}

	public DashboardActivity getDashboardActivity() {
		return mDashboardActivity;
	}

	public boolean isRecording() {
		return mIsRecording;
	}


}
