package nl.easthome.ebikereader.Services;

import android.location.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.Objects.Ride;
import nl.easthome.ebikereader.Objects.RideMeasurement;

public class RideRecorderService {
	private static boolean mIsRecording = false;
	private DashboardActivity mDashboardActivity;
	private GPSLocationService mGPSLocationService;
	private String mRideOwnerUid;
	private Ride mRide;


	public RideRecorderService(DashboardActivity dashboardActivity) {
		mDashboardActivity = dashboardActivity;
		mRideOwnerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		mGPSLocationService = new GPSLocationService(this);
	}

	public void toggleRecording() {
		if (!mIsRecording) {
			mRide = new Ride();
			mRide.startRide();
			FirebaseDatabase.getInstance().getReference("UserRides").child(mRideOwnerUid).child(mRide.getRideId()).setValue("x");
			mIsRecording = true;
			mGPSLocationService.startLocationUpdates();
		} else {
			mGPSLocationService.stopLocationUpdates();
			mRide.stopRide();
			mIsRecording = false;
		}
	}

	public void onNewLocation(Location location) {
		RideMeasurement measurement = new RideMeasurement(location);
		mDashboardActivity.updateViews(measurement);
		mRide.addMeasurement(measurement);
	}


	public DashboardActivity getDashboardActivity() {
		return mDashboardActivity;
	}

}
