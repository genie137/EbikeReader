package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

public class AntPlusHeartSensor extends AntPlusSensorConnection<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    private ISensorHandler<AntPlusHeartRatePcc> mSensorHandler;

    public AntPlusHeartSensor(ISensorHandler<AntPlusHeartRatePcc> sensorHandler) {
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusHeartRatePcc sensor) {
        mSensorHandler.subscribeToEvents(sensor);
    }

}
