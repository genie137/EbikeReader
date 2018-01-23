package nl.easthome.antpluslibary.SensorData;
import java.math.BigDecimal;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

/**
 * Class containing "useful" data from the sensor.
 */
@SuppressWarnings("ALL")
public class AntPlusCadenceSensorData extends AntPlusSensorData {
    private double calculatedCadence = -1;
    private double cumulativeResolutions = -1;

    /**
     * Constructor
     */
    public AntPlusCadenceSensorData() {
    }

    @Override
    protected void verifyDatasetCompleted() {
        if (calculatedCadence > -1 && cumulativeResolutions > -1) {
            super.isDatasetComplete = true;
        }
    }

    /**
     * Set the value of cumulativeResolutions while converting the value from long to double.
     * This method has the name doset, because firebase uses POJO to build/convert objects, and these need to be empty.
     *
     * @param cumulativeResolutions The amount of cumulative resolutions.
     */
    public void dosetCumulativeResolutions(long cumulativeResolutions) {
        this.cumulativeResolutions = Long.valueOf(cumulativeResolutions).doubleValue();
        verifyDatasetCompleted();
    }

    /**
     * Set the value of cumulativeResolutions while converting the value from BigDecimal to double.
     * Firebase cannot handle BigDecimals.
     * This method has the name doset, because firebase uses POJO to build/convert objects, and these need to be empty.
     * @param calculatedCadence The calculated cadence.
     */
    public void dosetCalculatedCadence(BigDecimal calculatedCadence) {
        this.calculatedCadence = Double.valueOf(calculatedCadence.toPlainString());
        verifyDatasetCompleted();
    }

    //Getters and setters

    public double getCalculatedCadence() {
        return calculatedCadence;
    }

    public void setCalculatedCadence(double calculatedCadence) {
        this.calculatedCadence = calculatedCadence;
    }

    public double getCumulativeResolutions() {
        return cumulativeResolutions;
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
