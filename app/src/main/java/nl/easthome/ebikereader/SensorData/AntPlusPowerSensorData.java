package nl.easthome.ebikereader.SensorData;
import java.math.BigDecimal;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

@SuppressWarnings("ALL")
public class AntPlusPowerSensorData extends AntPlusSensorData {
    private double calculatedPower = -1;

    public AntPlusPowerSensorData() {

    }

    @Override
    protected void verifyDatasetCompleted() {
        if (calculatedPower > -1) {
            super.isDatasetComplete = true;
        }
    }

    public double getCalculatedPower() {
        return calculatedPower;
    }

    public void setCalculatedPower(double calculatedPower) {
        this.calculatedPower = calculatedPower;
    }

    public void dosetCalculatedPower(BigDecimal calculatedPower) {
        this.calculatedPower = Double.valueOf(calculatedPower.toPlainString());
        System.out.println(calculatedPower);
        verifyDatasetCompleted();
    }

    @Override
    public String toString() {
        return "AntPlusPowerSensorData{" +
                "calculatedPower=" + calculatedPower +
                '}';
    }
}
