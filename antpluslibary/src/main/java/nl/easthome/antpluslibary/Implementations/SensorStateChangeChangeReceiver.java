package nl.easthome.antpluslibary.Implementations;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;

public class SensorStateChangeChangeReceiver<T extends AntPluginPcc> implements AntPluginPcc.IDeviceStateChangeReceiver{
    AntPlusSensorConnection<T> mAntPlusSensorConnection;

    public SensorStateChangeChangeReceiver(AntPlusSensorConnection<T> antPlusSensorConnection) {
        mAntPlusSensorConnection = antPlusSensorConnection;
    }

    @Override
    public void onDeviceStateChange(DeviceState deviceState) {
        switch (deviceState){
            case DEAD:
                break;
            case CLOSED:
                break;
            case SEARCHING:
                break;
            case TRACKING:
                break;
            case PROCESSING_REQUEST:
                break;
            case UNRECOGNIZED:
                break;
        }
    }
}
