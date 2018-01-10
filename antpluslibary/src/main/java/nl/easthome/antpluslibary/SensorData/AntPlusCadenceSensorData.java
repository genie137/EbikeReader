package nl.easthome.antpluslibary.SensorData;
import java.math.BigDecimal;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public class AntPlusCadenceSensorData extends AntPlusSensorData {
    private double calculatedCadence = -1;
    private double cumulativeResolutions = -1;

    public AntPlusCadenceSensorData() {
    }

    @Override
    protected void verifyDatasetCompleted() {
        if (calculatedCadence > -1 && cumulativeResolutions > -1) {
            super.isDatasetComplete = true;
        }
    }

    public double getCalculatedCadence() {
        return calculatedCadence;
    }

    public void dosetCalculatedCadence(BigDecimal calculatedCadence) {
        this.calculatedCadence = Double.valueOf(calculatedCadence.toPlainString());
        verifyDatasetCompleted();
    }

    public void setCalculatedCadence(double calculatedCadence) {
        this.calculatedCadence = calculatedCadence;
    }

    public double getCumulativeResolutions() {
        return cumulativeResolutions;
    }

    public void dosetCumulativeResolutions(long cumulativeResolutions) {
        this.cumulativeResolutions = Long.valueOf(cumulativeResolutions).doubleValue();
        verifyDatasetCompleted();
    }

    public void setCumulativeResolutions(double cumulativeResolutions) {
        this.cumulativeResolutions = cumulativeResolutions;
    }

    @Override
    public String toString() {
        return "AntPlusCadenceSensorData{" +
                "calculatedCadence=" + calculatedCadence +
                ", cumulativeResolutions=" + cumulativeResolutions +
                '}';
    }
}
