package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class AntPlusSpeedSensor extends AntPlusConnectedSensor<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> {
    private final ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> mSensorHandler;

    public AntPlusSpeedSensor(ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> sensorHandler) {
        super(DeviceType.BIKE_SPD);
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusBikeSpeedDistancePcc sensorPcc) {
        mSensorHandler.subscribeToEvents(sensorPcc);
    }
}
