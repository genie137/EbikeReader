package nl.easthome.antpluslibary.Implementations;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public class SensorStateChangeChangeReceiver<T extends AntPluginPcc, T1 extends AntPlusSensorData> implements AntPluginPcc.IDeviceStateChangeReceiver {
    private AntPlusConnectedSensor<T, T1> mAntPlusConnectedSensor;

    public SensorStateChangeChangeReceiver(AntPlusConnectedSensor<T, T1> antPlusConnectedSensor) {
        mAntPlusConnectedSensor = antPlusConnectedSensor;
    }

    @Override
    public void onDeviceStateChange(DeviceState deviceState) {
        mAntPlusConnectedSensor.setDeviceState(deviceState);
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
