package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;

public class EBikePowerSensorImplementation implements ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> {

    @Override
    public AntPlusPowerSensorData subscribeToEvents(AntPlusBikePowerPcc sensorConnection) {
        return null;
    }
}
