package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class AntPlusSpeedSensor extends AntPlusSensorConnection<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> {
    private ISensorHandler mSensorHandler;

    public AntPlusSpeedSensor(ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> sensorHandler) {
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusBikeSpeedDistancePcc sensor) {
        mSensorHandler.subscribeToEvents(sensor);
    }
}