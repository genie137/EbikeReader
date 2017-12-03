package nl.easthome.ebikereader.Objects;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Helpers.SystemTime;

public class RideRecording {

    private String mRideId;
    private long mRideStart;
    private long mRideEnd;
    private Map<String, Object> mRideMeasurements;

    public RideRecording() {
        mRideMeasurements = new HashMap<>();
    }

	public void startRide() {
        mRideStart = SystemTime.getSystemTimestamp();
        mRideEnd = 0;
        mRideId = FirebaseSaver.addNewRide(this, FirebaseAuth.getInstance().getUid());
    }
	public void stopRide() {
        mRideEnd = SystemTime.getSystemTimestamp();
        FirebaseSaver.updateRideRecording(this);
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

    public Map<String, Object> getRideMeasurements() {
        return mRideMeasurements;
    }

    public void setRideMeasurements(Map<String, Object> mRideMeasurements) {
        this.mRideMeasurements = mRideMeasurements;
    }

    public void addRideMeasurement(long timestamp, RideMeasurement rideMeasurement) {
        mRideMeasurements.put(String.valueOf(timestamp), rideMeasurement);
        FirebaseSaver.updateRideRecording(this);
    }
}
