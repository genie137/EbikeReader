package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;

public class AntPlusCadenceSensor extends AntPlusSensorConnection<AntPlusBikeCadencePcc> {
    public AntPlusCadenceSensor() {
    }

    @Override
    protected void subscribeToEvents(AntPlusBikeCadencePcc sensor) {

    }


    @Override
    public ArrayList<String> getSensorDataAtTimestamp(long timestamp) {
        return null;
    }
}
