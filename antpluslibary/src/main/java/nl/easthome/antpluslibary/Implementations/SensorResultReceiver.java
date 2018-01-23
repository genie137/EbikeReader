package nl.easthome.antpluslibary.Implementations;

import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

/**
 * Class that handles the result of connecting to the ant+ device/sensor.
 *
 * @param <SensorPcc>  Defines the sensor/device type.
 * @param <SensorData> Defines the data type.
 */
public class SensorResultReceiver<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> implements AntPluginPcc.IPluginAccessResultReceiver<SensorPcc> {
    private static final String LOGTAG = "ANT+MDS_SENSOR_RESULT";
    private final AntPlusConnectedSensor<SensorPcc, SensorData> mAntPlusConnectedSensor;

    /**
     * Constructor
     *
     * @param antPlusConnectedSensor The sensor for which this class handles the connection.
     */
    public SensorResultReceiver(AntPlusConnectedSensor<SensorPcc, SensorData> antPlusConnectedSensor) {
        mAntPlusConnectedSensor = antPlusConnectedSensor;
    }

    /**
     * Method gets called when the connection of a device has completed or failed.
     * Only gets called on the initial connection!
     * @param sensorPcc The sensor/device pcc.
     * @param requestAccessResult   Result of the process to establish the connection.
     * @param deviceState State of the device connecting.
     */
    @Override
    public void onResultReceived(SensorPcc sensorPcc, RequestAccessResult requestAccessResult, DeviceState deviceState) {
        String sensorPccType;
        if (sensorPcc == null) {
            sensorPccType = "Null";
        } else {
            sensorPccType = sensorPcc.toString();
        }

        switch (requestAccessResult){
            case SUCCESS:
                mAntPlusConnectedSensor.setDeviceState(deviceState);
                mAntPlusConnectedSensor.setResultConnection(sensorPcc);
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

        Log.d(LOGTAG, "Type: " + sensorPccType + " AccessResult: " + requestAccessResult.toString() + " Device State: " + deviceState.toString());
    }
}