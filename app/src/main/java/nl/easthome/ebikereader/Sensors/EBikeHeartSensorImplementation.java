package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

/**
 * Created by jorisoosterhuis on 27/11/2017.
 */

public class EBikeHeartSensorImplementation implements ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    @Override
    public AntPlusHeartSensorData subscribeToEvents(AntPlusHeartRatePcc sensorConnection) {
        return null;
    }
}
