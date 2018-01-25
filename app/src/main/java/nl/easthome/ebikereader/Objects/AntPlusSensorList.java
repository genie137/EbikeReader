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

    public RideMeasurement addRideMeasurementDataOfAllConnectedSensors(RideMeasurement rideMeasurment) {


        if (antPlusCadenceSensor != null) {
            rideMeasurment.setCadenceSensorData(antPlusCadenceSensor.getLastSensorData());
        }
        if (antPlusHeartSensor != null) {
            rideMeasurment.setHeartSensorData(antPlusHeartSensor.getLastSensorData());
        }
        if (antPlusPowerSensor != null) {
            rideMeasurment.setPowerSensorData(antPlusPowerSensor.getLastSensorData());
        }
        if (antPlusSpeedSensor != null) {
            rideMeasurment.setSpeedSensorData(antPlusSpeedSensor.getLastSensorData());
        }
        return rideMeasurment;
    }

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

}
