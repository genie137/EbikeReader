package nl.easthome.ebikereader.Objects;

public class Ride {

	private String mRideId;
	private long mRideStart;


	public Ride() {

	}

	public void startRide() {
		mRideStart = System.currentTimeMillis();
	}






	public String getRideId() {
		return mRideId;
	}

	public void setRideId(String mRideId) {
		this.mRideId = mRideId;
	}

	public long getRideStart() {
		return mRideStart;
	}

	public void setmRideStart(long mRideStart) {
		this.mRideStart = mRideStart;
	}
}
