package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;

public class AntPlusPowerSensor extends AntPlusConnectedSensor<AntPlusBikePowerPcc, AntPlusPowerSensorData> {
    private final ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> mSensorHandler;

    public AntPlusPowerSensor(ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> sensorHandler) {
        super(DeviceType.BIKE_POWER);
        mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusBikePowerPcc sensorPcc) {
        mSensorHandler.subscribeToEvents(sensorPcc);
    }
}
