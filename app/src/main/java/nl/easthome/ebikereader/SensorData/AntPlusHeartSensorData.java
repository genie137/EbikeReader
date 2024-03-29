package nl.easthome.ebikereader.SensorData;
import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

/**
 *
 */
@SuppressWarnings("ALL")
public class AntPlusHeartSensorData extends AntPlusSensorData {
    private int heartRate = -1;

    public AntPlusHeartSensorData() {
    }

    @Override
    protected void verifyDatasetCompleted() {
        if (heartRate > -1){
            super.isDatasetComplete = true;
        }
    }

    public void dosetHeartrate(int i){
        heartRate = i;
        verifyDatasetCompleted();
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public String toString() {
        return "AntPlusHeartSensorData{" +
                "heartRate=" + heartRate +
                '}';
    }
}
