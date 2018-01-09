package nl.easthome.ebikereader.Objects;

import android.location.Location;

import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class RideMeasurement {

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

    @Override
    public String toString() {
        return "RideMeasurement{" +
                "mLocation=" + mLocation +
                ", mSpeedSensorData=" + mSpeedSensorData +
                ", mCadenceSensorData=" + mCadenceSensorData +
                ", mPowerSensorData=" + mPowerSensorData +
                ", mHeartSensorData=" + mHeartSensorData +
                ", mEstimatedPowerData=" + mEstimatedPowerData +
                '}';
    }
}
