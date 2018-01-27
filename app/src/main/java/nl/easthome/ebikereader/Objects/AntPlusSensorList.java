package nl.easthome.ebikereader.Objects;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.ebikereader.SensorData.AntPlusCadenceSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusHeartSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusPowerSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusSpeedSensorData;

/**
 * Class that contains references to the different sensor objects.
 */
public class AntPlusSensorList {
    AntPlusConnectedSensor<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> antPlusCadenceSensor;
    AntPlusConnectedSensor<AntPlusHeartRatePcc, AntPlusHeartSensorData> antPlusHeartSensor;
    AntPlusConnectedSensor<AntPlusBikePowerPcc, AntPlusPowerSensorData> antPlusPowerSensor;
    AntPlusConnectedSensor<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> antPlusSpeedSensor;

    /**
     * Constructor.
     */
    public AntPlusSensorList() {
    }

    /**
     * Add the sensor data of connected sensors to the rideMeasurement.
     *
     * @param rideMeasurement The rideMeasurement in which data needs to be added.
     * @return The RideMeasurement after it has been filled with data.
     */
    public RideMeasurement addRideMeasurementDataOfAllConnectedSensors(RideMeasurement rideMeasurement) {

        if (antPlusCadenceSensor != null) {
            rideMeasurement.setCadenceSensorData(antPlusCadenceSensor.getLastSensorData());
        }
        if (antPlusHeartSensor != null) {
            rideMeasurement.setHeartSensorData(antPlusHeartSensor.getLastSensorData());
        }
        if (antPlusPowerSensor != null) {
            rideMeasurement.setPowerSensorData(antPlusPowerSensor.getLastSensorData());
        }
        if (antPlusSpeedSensor != null) {
            rideMeasurement.setSpeedSensorData(antPlusSpeedSensor.getLastSensorData());
        }
        return rideMeasurement;
    }

    /**
     * Disconnect all connected sensors.
     */
    public void disconnectAllConnectedSensors() {

        if (antPlusCadenceSensor != null) {
            antPlusCadenceSensor.disconnectSensor();
        }
        if (antPlusHeartSensor != null) {
            antPlusHeartSensor.disconnectSensor();
        }
        if (antPlusPowerSensor != null) {
            antPlusPowerSensor.disconnectSensor();
        }
        if (antPlusSpeedSensor != null) {
            antPlusSpeedSensor.disconnectSensor();
        }
    }


    //Getters and Setters

    public AntPlusConnectedSensor<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> getAntPlusCadenceSensor() {
        return antPlusCadenceSensor;
    }

    public void setAntPlusCadenceSensor(AntPlusConnectedSensor<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> antPlusCadenceSensor) {
        this.antPlusCadenceSensor = antPlusCadenceSensor;
    }

    public AntPlusConnectedSensor<AntPlusHeartRatePcc, AntPlusHeartSensorData> getAntPlusHeartSensor() {
        return antPlusHeartSensor;
    }

    public void setAntPlusHeartSensor(AntPlusConnectedSensor<AntPlusHeartRatePcc, AntPlusHeartSensorData> antPlusHeartSensor) {
        this.antPlusHeartSensor = antPlusHeartSensor;
    }

    public AntPlusConnectedSensor<AntPlusBikePowerPcc, AntPlusPowerSensorData> getAntPlusPowerSensor() {
        return antPlusPowerSensor;
    }

    public void setAntPlusPowerSensor(AntPlusConnectedSensor<AntPlusBikePowerPcc, AntPlusPowerSensorData> antPlusPowerSensor) {
        this.antPlusPowerSensor = antPlusPowerSensor;
    }

    public AntPlusConnectedSensor<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> getAntPlusSpeedSensor() {
        return antPlusSpeedSensor;
    }

    public void setAntPlusSpeedSensor(AntPlusConnectedSensor<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> antPlusSpeedSensor) {
        this.antPlusSpeedSensor = antPlusSpeedSensor;
    }




}
