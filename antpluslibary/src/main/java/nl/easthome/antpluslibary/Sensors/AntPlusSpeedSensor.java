package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;

public class AntPlusSpeedSensor extends AntPlusSensorConnection<AntPlusBikeSpeedDistancePcc> {
    private static final String LOGTAG = "AntPlusSpeedSensor";

    public AntPlusSpeedSensor() {
    }

    @Override
    protected void subscribeToEvents(AntPlusBikeSpeedDistancePcc sensor) {

    }

    @Override
    public ArrayList<String> getSensorDataAtTimestamp(long timestamp) {
        return null;
    }
}
