package nl.easthome.antpluslibary.Implementations;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public class SensorStateChangeChangeReceiver<T extends AntPluginPcc, T1 extends AntPlusSensorData> implements AntPluginPcc.IDeviceStateChangeReceiver {
    AntPlusSensorConnection<T, T1> mAntPlusSensorConnection;

    public SensorStateChangeChangeReceiver(AntPlusSensorConnection<T, T1> antPlusSensorConnection) {
        mAntPlusSensorConnection = antPlusSensorConnection;
    }

    @Override
    public void onDeviceStateChange(DeviceState deviceState) {
        mAntPlusSensorConnection.setDeviceState(deviceState);
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
