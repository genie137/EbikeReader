package nl.easthome.ebikereader.Objects;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Helpers.SystemTime;

public class RideRecording implements Serializable {

    private String rideId;
    private long rideStart;
    private long rideEnd;
    private Map<String, Object> mRideMeasurements;

    public RideRecording() {
        mRideMeasurements = new HashMap<>();
    }

	public void startRide() {
        rideStart = SystemTime.getSystemTimestamp();
        rideEnd = 0;
        rideId = FirebaseSaver.addNewRide(this, FirebaseAuth.getInstance().getUid());
    }
	public void stopRide() {
        rideEnd = SystemTime.getSystemTimestamp();
        FirebaseSaver.updateRideRecording(this);
    }

	public String getRideId() {
		return rideId;
	}

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public long getRideStart() {
        return rideStart;
    }

    public void setRideStart(long rideStart) {
        this.rideStart = rideStart;
    }

    public long getRideEnd() {
        return rideEnd;
    }

    public void setRideEnd(long rideEnd) {
        this.rideEnd = rideEnd;
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

    @Override
    public String toString() {
        return "RideRecording{" +
                "mRideId='" + rideId + '\'' +
                ", rideStart=" + rideStart +
                ", rideEnd=" + rideEnd +
                ", mRideMeasurements=" + mRideMeasurements +
                '}';
    }
}
