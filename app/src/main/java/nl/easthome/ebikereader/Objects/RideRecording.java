package nl.easthome.ebikereader.Objects;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;

public class RideRecording {

    private String rideId;
    private long rideStart;
    private long rideEnd;
    private HashMap<String, RideMeasurement> mRideMeasurements;


    public RideRecording() {
        mRideMeasurements = new HashMap<>();
    }

    public void startRide() {
        rideStart = Constants.getSystemTimestamp();
        rideEnd = 0;
        rideId = FirebaseSaver.addNewRide(this, FirebaseAuth.getInstance().getUid());
    }

    public void stopRide() {
        rideEnd = Constants.getSystemTimestamp();
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

    public HashMap<String, RideMeasurement> getRideMeasurements() {
        return mRideMeasurements;
    }

    public void setRideMeasurements(HashMap<String, RideMeasurement> mRideMeasurements) {
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
