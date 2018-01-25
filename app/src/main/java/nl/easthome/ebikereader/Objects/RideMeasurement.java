package nl.easthome.ebikereader.Objects;

import android.location.Location;

import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.SensorData.AntPlusCadenceSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusHeartSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusPowerSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusSpeedSensorData;

public class RideMeasurement {

    private long mTimestamp;
    private String mTimeAsString;
    private FirebaseLocation mLocation;
    private AntPlusSpeedSensorData mSpeedSensorData;
    private AntPlusCadenceSensorData mCadenceSensorData;
    private AntPlusPowerSensorData mPowerSensorData;
    private AntPlusHeartSensorData mHeartSensorData;
    private EstimatedPowerData mEstimatedPowerData;

    public RideMeasurement() {
    }

	public RideMeasurement(Location location) {
		mLocation = new FirebaseLocation(location);
	}

    public FirebaseLocation getLocation() {
        return mLocation;
    }

    public void setLocation(FirebaseLocation mLocation) {
        this.mLocation = mLocation;
    }

    public AntPlusSpeedSensorData getSpeedSensorData() {
        return mSpeedSensorData;
    }

    public void setSpeedSensorData(AntPlusSpeedSensorData mSpeedSensorData) {
        this.mSpeedSensorData = mSpeedSensorData;
    }

    public AntPlusCadenceSensorData getCadenceSensorData() {
        return mCadenceSensorData;
    }

    public void setCadenceSensorData(AntPlusCadenceSensorData cadenceSensorData) {
        this.mCadenceSensorData = cadenceSensorData;
    }

    public AntPlusPowerSensorData getPowerSensorData() {
        return mPowerSensorData;
    }

    public void setPowerSensorData(AntPlusPowerSensorData powerSensorData) {
        this.mPowerSensorData = powerSensorData;
    }

    public AntPlusHeartSensorData getHeartSensorData() {
        return mHeartSensorData;
    }

    public void setHeartSensorData(AntPlusHeartSensorData heartSensorData) {
        this.mHeartSensorData = heartSensorData;
    }

    public EstimatedPowerData getEstimatedPowerData() {
        return mEstimatedPowerData;
    }

    public void setEstimatedPowerData(EstimatedPowerData mEstimatedPowerData) {
        this.mEstimatedPowerData = mEstimatedPowerData;
    }

    public void dosetTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
        this.mTimeAsString = Constants.convertTimestampToDateTime(mTimestamp);
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public String getTimeAsString() {
        return mTimeAsString;
    }

    public void setTimeAsString(String mTimeAsString) {
        this.mTimeAsString = mTimeAsString;
    }

    @Override
    public String toString() {
        return "RideMeasurement{" +
                "mTimestamp=" + mTimestamp +
                ", mLocation=" + mLocation +
                ", mSpeedSensorData=" + mSpeedSensorData +
                ", mCadenceSensorData=" + mCadenceSensorData +
                ", mPowerSensorData=" + mPowerSensorData +
                ", mHeartSensorData=" + mHeartSensorData +
                ", mEstimatedPowerData=" + mEstimatedPowerData +
                '}';
    }
}
