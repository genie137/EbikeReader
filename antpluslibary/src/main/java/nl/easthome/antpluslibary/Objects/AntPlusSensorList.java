package nl.easthome.antpluslibary.Objects;
import nl.easthome.antpluslibary.Sensors.AntPlusCadenceSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusHeartSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusPowerSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusSpeedSensor;

public class AntPlusSensorList {
    AntPlusCadenceSensor antPlusCadenceSensor;
    AntPlusHeartSensor antPlusHeartSensor;
    AntPlusPowerSensor antPlusPowerSensor;
    AntPlusSpeedSensor antPlusSpeedSensor;

    public AntPlusSensorList() {
    }

    public AntPlusCadenceSensor getAntPlusCadenceSensor() {
        return antPlusCadenceSensor;
    }

    public void setAntPlusCadenceSensor(AntPlusCadenceSensor antPlusCadenceSensor) {
        this.antPlusCadenceSensor = antPlusCadenceSensor;
    }

    public AntPlusHeartSensor getAntPlusHeartSensor() {
        return antPlusHeartSensor;
    }

    public void setAntPlusHeartSensor(AntPlusHeartSensor antPlusHeartSensor) {
        this.antPlusHeartSensor = antPlusHeartSensor;
    }

    public AntPlusPowerSensor getAntPlusPowerSensor() {
        return antPlusPowerSensor;
    }

    public void setAntPlusPowerSensor(AntPlusPowerSensor antPlusPowerSensor) {
        this.antPlusPowerSensor = antPlusPowerSensor;
    }

    public AntPlusSpeedSensor getAntPlusSpeedSensor() {
        return antPlusSpeedSensor;
    }

    public void setAntPlusSpeedSensor(AntPlusSpeedSensor antPlusSpeedSensor) {
        this.antPlusSpeedSensor = antPlusSpeedSensor;
    }
}
