package nl.easthome.antpluslibary.Implementations;

import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import nl.easthome.antpluslibary.Objects.AntPlusConnectedSensor;
import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

/**
 * Class that handles changes in the state of the ant+ device/sensor.
 *
 * @param <SensorPcc>  Defines the sensor/device type.
 * @param <SensorData> Defines the data type.
 */
public class SensorStateChangeReceiver<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> implements AntPluginPcc.IDeviceStateChangeReceiver {
    private static final String LOGTAG = "ANT+SENSOR_STATE_CHANGE";
    private AntPlusConnectedSensor<SensorPcc, SensorData> mAntPlusConnectedSensor;

    /**
     * Constructor
     *
     * @param antPlusConnectedSensor The device that is being monitored.
     */
    public SensorStateChangeReceiver(AntPlusConnectedSensor<SensorPcc, SensorData> antPlusConnectedSensor) {
        mAntPlusConnectedSensor = antPlusConnectedSensor;
    }

    /**
     * Method that gets called when the state of the device changes.
     * Sets the state of the connected device.
     *
     * @param deviceState New device state.
     */
    @Override
    public void onDeviceStateChange(DeviceState deviceState) {
        Log.d(LOGTAG, "Sensor " + mAntPlusConnectedSensor.getDeviceType() + " State Changed --> " + deviceState.toString());
        mAntPlusConnectedSensor.setDeviceState(deviceState);
    }
}
