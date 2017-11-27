package nl.easthome.antpluslibary.Interfaces;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public interface ISensorHandler<Sensor extends AntPluginPcc, Data extends AntPlusSensorData> {

    Data subscribeToEvents(Sensor sensorConnection);

}
