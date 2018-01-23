package nl.easthome.antpluslibary.Objects;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.concurrent.ConcurrentLinkedDeque;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;


/**
 * Class that contains all information, classes and connections to a connected Ant + Sensor.
 *
 * @param <SensorPcc>  Defines the sensor/device type.
 * @param <SensorData> Defines the data type.
 */
public abstract class AntPlusConnectedSensor<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private DeviceState deviceState = DeviceState.SEARCHING;

    private String deviceType;
    private PccReleaseHandle<SensorPcc> mReleaseHandle;
    private SensorResultReceiver mSensorResultReceiver;
    private SensorStateChangeReceiver<SensorPcc, SensorData> mSensorStateChangeReceiver;
    private SensorPcc resultConnection;
    private ConcurrentLinkedDeque<SensorData> mSensorData;

    /**
     * Constructor.
     *
     * @param deviceType The type of device that has the connection.
     */
    protected AntPlusConnectedSensor(DeviceType deviceType) {
        this.deviceType = deviceType.toString();
        mSensorResultReceiver = new SensorResultReceiver<>(this);
        mSensorStateChangeReceiver = new SensorStateChangeReceiver<>(this);
        mSensorData = new ConcurrentLinkedDeque<>();
    }

    /**
     * Abstracted method, that is used to record the events.
     * TODO simplify method by changing this object to incorporate the ISensorHandler itself.
     *
     * @param sensorPcc The connection to the sensor.
     */
    protected abstract void subscribeToEvents(SensorPcc sensorPcc);

    /**
     * Method that returns the newest SensorData object in the deque that is complete.
     * @return The SensorData object.
     */
    public SensorData getLastSensorData() {
        if (mSensorData.size() > 0) {
            for (SensorData sensorData : mSensorData) {
                if (sensorData.isDatasetComplete()) {
                    return sensorData;
                }
            }
        }
        return null;
    }

    public SensorPcc getResultConnection() {
        return resultConnection;
    }

    // Getters and Setters

    /**
     * Method that sets the connection of a sensor.
     * This only works if the device is being tracked.
     * Fortunately a resultConnection (PCC) is only given when a device is actually connected.
     * When this connection is set, it can be used to subscribe to the events of the sensor.
     *
     * @param resultConnection The connection with the device.
     */
    public void setResultConnection(SensorPcc resultConnection) {
        if (deviceState == DeviceState.TRACKING) {
            this.resultConnection = resultConnection;
            subscribeToEvents(resultConnection);
        }
    }

    public SensorResultReceiver getSensorResultReceiver() {
        return mSensorResultReceiver;
    }

    public SensorStateChangeReceiver<SensorPcc, SensorData> getSensorStateChangeReceiver() {
        return mSensorStateChangeReceiver;
    }

    public PccReleaseHandle<SensorPcc> getReleaseHandle() {
        return mReleaseHandle;
    }

    public void setReleaseHandle(PccReleaseHandle<SensorPcc> releaseHandle) {
        this.mReleaseHandle = releaseHandle;
    }

    public DeviceState getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(DeviceState newDeviceState) {
        this.deviceState = newDeviceState;
    }

    public ConcurrentLinkedDeque<SensorData> getSensorDataDeque() {
        return mSensorData;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
