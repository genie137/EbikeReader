package nl.easthome.antpluslibary.Interfaces;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

public interface ISensorHandler<Sensor extends AntPluginPcc> {

    void subscribeToEvents(Sensor sensorConnection);

}
