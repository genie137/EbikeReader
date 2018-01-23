package nl.easthome.antpluslibary.Sensors;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

public class AntPlusHeartSensor extends AntPlusConnectedSensor<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    private final ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> mSensorHandler;

    public AntPlusHeartSensor(ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> sensorHandler) {
        super(DeviceType.HEARTRATE);
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusHeartRatePcc sensorPcc) {
        mSensorHandler.subscribeToEvents(sensorPcc);
    }

}
