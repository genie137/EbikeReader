package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;

public class AntPlusPowerSensor extends AntPlusSensorConnection<AntPlusBikePowerPcc> {


    public AntPlusPowerSensor() {
    }

    @Override
    protected void subscribeToEvents(AntPlusBikePowerPcc sensor) {

    }


    @Override
    public ArrayList<String> getSensorDataAtTimestamp(long timestamp) {
        return null;
    }
}
