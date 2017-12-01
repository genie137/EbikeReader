package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

public class EBikeHeartSensorImplementation extends ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    @Override
    public void subscribeToEvents(AntPlusHeartRatePcc sensorConnection) {

    }
}
