package nl.easthome.ebikereader.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Helpers.SystemTime;

public class Ride {

	private static String mRideId;
	private static long mRideStart;
	private static long mRideEnd;
	private static ArrayList<RideMeasurement> mRideMeasurement;

	public Ride() {
		mRideMeasurement = new ArrayList<>();
	}

	public void startRide() {
        mRideStart = SystemTime.getSystemTimestamp();
        mRideEnd = 0;
        mRideId = FirebaseDatabase.getInstance().getReference("rides").push().getKey();
        //TODO optimize saving
        FirebaseDatabase.getInstance().getReference("rides").child(mRideId).setValue(this);
        FirebaseSaver.addRideToUser(mRideId, FirebaseAuth.getInstance().getUid());
    }

	public void stopRide() {
        mRideEnd = SystemTime.getSystemTimestamp();
        FirebaseDatabase.getInstance().getReference("rides").child(mRideId).child("rideEnd").setValue(mRideEnd);
    }

	public String getRideId() {
		return mRideId;
	}

	public long getRideStart() {
		return mRideStart;
	}

	public long getRideEnd() {
		return mRideEnd;
	}

	public ArrayList<RideMeasurement> getRideMeasurements() {
		return mRideMeasurement;
	}

	public void addRideMeasurement(RideMeasurement rideMeasurement) {
		mRideMeasurement.add(rideMeasurement);
		FirebaseSaver.addRideMeasurement(mRideId, rideMeasurement);
	}
}
