package nl.easthome.ebikereader.Objects;

import android.location.Location;

import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class RideMeasurement {

	private Location mLocation;
    private AntPlusSpeedSensorData mSpeedSensorData;
    private AntPlusCadenceSensorData mCadenceSensorData;

	public RideMeasurement() {
	}

	public RideMeasurement(Location location) {
		mLocation = location;
	}

	public Location getLocation() {
		return mLocation;
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

}
