package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;

public class AntPlusPowerSensor extends AntPlusConnectedSensor<AntPlusBikePowerPcc, AntPlusPowerSensorData> {
    private ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> mSensorHandler;

    public AntPlusPowerSensor(ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> sensorHandler) {
        mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusBikePowerPcc sensor) {
        mSensorHandler.subscribeToEvents(sensor);
    }
}
