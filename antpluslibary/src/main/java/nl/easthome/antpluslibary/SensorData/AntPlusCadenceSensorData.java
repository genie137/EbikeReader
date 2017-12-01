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

    public void setCalculatedCadence(BigDecimal calculatedCadence) {
        this.calculatedCadence = Double.valueOf(calculatedCadence.toPlainString());
        verifyDatasetCompleted();
    }

    public double getCumulativeResolutions() {
        return cumulativeResolutions;
    }

    public void setCumulativeResolutions(long cumulativeResolutions) {
        this.cumulativeResolutions = Long.valueOf(cumulativeResolutions).doubleValue();
        verifyDatasetCompleted();
    }
}
