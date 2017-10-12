package nl.easthome.ebikereader.Objects;

import com.google.firebase.database.FirebaseDatabase;

public class Ride {

	private String mRideId;
	private long mRideStart;
	private long mRideEnd;

	public Ride() {

	}

	public void startRide() {
		mRideStart = System.currentTimeMillis();
		mRideEnd = 0;
		mRideId = FirebaseDatabase.getInstance().getReference("rides").push().getKey();
		FirebaseDatabase.getInstance().getReference("rides").child(mRideId).setValue(this);

	}

	public void stopRide() {
		mRideEnd = System.currentTimeMillis();
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

	public void addMeasurement(RideMeasurement measurement) {
		FirebaseDatabase.getInstance().getReference("rides").child(mRideId).child("measurements").child(Long.toString(System.currentTimeMillis())).setValue(measurement);
	}
}
