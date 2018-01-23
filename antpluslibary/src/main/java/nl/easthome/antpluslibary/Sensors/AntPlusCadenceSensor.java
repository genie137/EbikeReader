package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;

public class AntPlusCadenceSensor extends AntPlusConnectedSensor<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> {
    private ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> mSensorHandler;

    public AntPlusCadenceSensor(ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> sensorHandler) {
        super(DeviceType.BIKE_CADENCE);
        this.mSensorHandler = sensorHandler;
    }

    @Override
    protected void subscribeToEvents(AntPlusBikeCadencePcc sensorPcc) {

        mSensorHandler.subscribeToEvents(sensorPcc);
    }

}
