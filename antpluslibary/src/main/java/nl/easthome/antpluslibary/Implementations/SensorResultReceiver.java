package nl.easthome.antpluslibary.Implementations;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;


public class SensorResultReceiver<T extends AntPluginPcc> implements AntPluginPcc.IPluginAccessResultReceiver<T>{
    AntPlusSensorConnection<T> mAntPlusSensorConnection;

    public SensorResultReceiver(AntPlusSensorConnection<T> antPlusSensorConnection) {
        mAntPlusSensorConnection = antPlusSensorConnection;
    }

    @Override
    public void onResultReceived(T t, RequestAccessResult requestAccessResult, DeviceState deviceState) {
        String result;
        if (t ==  null) {
            result = "Null";
        }
        else{
            result = t.toString();
        }


        System.out.println("access: "+ requestAccessResult.toString() + ", result: "+ result + ", devicestate: " + deviceState.toString());
        switch (requestAccessResult){
            case SUCCESS:
                mAntPlusSensorConnection.setResultConnection(t);
                break;
            case USER_CANCELLED:
                break;
            case CHANNEL_NOT_AVAILABLE:
                break;
            case OTHER_FAILURE:
                break;
            case DEPENDENCY_NOT_INSTALLED:
                break;
            case DEVICE_ALREADY_IN_USE:
                break;
            case SEARCH_TIMEOUT:
                break;
            case ALREADY_SUBSCRIBED:
                break;
            case BAD_PARAMS:
                break;
            case ADAPTER_NOT_DETECTED:
                break;
            case UNRECOGNIZED:
                break;
        }
    }
}