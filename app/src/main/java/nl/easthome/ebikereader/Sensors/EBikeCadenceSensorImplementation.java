package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;

public class EBikeCadenceSensorImplementation implements ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> {
    @Override
    public AntPlusCadenceSensorData subscribeToEvents(AntPlusBikeCadencePcc sensorConnection) {
        return null;
    }
}
