package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;

public class EBikePowerSensorImplementation extends ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> {

    public EBikePowerSensorImplementation() {

    }

    @Override
    public void subscribeToEvents(AntPlusBikePowerPcc sensorConnection) {

    }
}
