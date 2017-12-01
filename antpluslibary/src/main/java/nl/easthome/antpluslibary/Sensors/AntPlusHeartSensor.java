package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

public class AntPlusHeartSensor extends AntPlusConnectedSensor<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    private ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> mSensorHandler;

    public AntPlusHeartSensor(ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> sensorHandler) {
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusHeartRatePcc sensor) {
        mSensorHandler.subscribeToEvents(sensor);
    }

}
