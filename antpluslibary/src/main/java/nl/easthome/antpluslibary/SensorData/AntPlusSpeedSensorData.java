package nl.easthome.antpluslibary.SensorData;
import java.math.BigDecimal;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public class AntPlusSpeedSensorData extends AntPlusSensorData {
    private double speedInMeterPerSecond = -1;
    private double calcAccumulatedDistanceInMeters = -1;

    public AntPlusSpeedSensorData() {
    }


    @Override
    public void verifyDatasetCompleted() {
        if (speedInMeterPerSecond > -1 && calcAccumulatedDistanceInMeters > -1) {
            super.isDatasetComplete = true;
        }
    }

    public double getSpeedInMeterPerSecond() {
        return speedInMeterPerSecond;
    }

    public void dosetSpeedInMeterPerSecond(BigDecimal speedInMeterPerSecond) {
        this.speedInMeterPerSecond = Double.valueOf(speedInMeterPerSecond.toPlainString());
        verifyDatasetCompleted();
    }

    public double getCalcAccumulatedDistanceInMeters() {
        return calcAccumulatedDistanceInMeters;
    }

    public void dosetCalcAccumulatedDistanceInMeters(BigDecimal calcAccumulatedDistanceInMeters) {
        this.calcAccumulatedDistanceInMeters = Double.valueOf(calcAccumulatedDistanceInMeters.toPlainString());
        verifyDatasetCompleted();
    }

    public void setSpeedInMeterPerSecond(double speedInMeterPerSecond) {
        this.speedInMeterPerSecond = speedInMeterPerSecond;
    }

    public void setCalcAccumulatedDistanceInMeters(double calcAccumulatedDistanceInMeters) {
        this.calcAccumulatedDistanceInMeters = calcAccumulatedDistanceInMeters;
    }

    @Override
    public String toString() {
        return "AntPlusSpeedSensorData{" +
                "speedInMeterPerSecond=" + speedInMeterPerSecond +
                ", calcAccumulatedDistanceInMeters=" + calcAccumulatedDistanceInMeters +
                '}';
    }
}
