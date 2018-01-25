package nl.easthome.antpluslibary.Objects;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.concurrent.ConcurrentLinkedDeque;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;


/**
 * Class that contains all information, classes and connections to a connected Ant + Sensor.
 *
 * @param <SensorPcc>  Defines the sensor/device type.
 * @param <SensorData> Defines the data type.
 */
@SuppressWarnings("ALL")
public class AntPlusConnectedSensor<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private final String deviceType;
    private final SensorResultReceiver mSensorResultReceiver;
    private final SensorStateChangeReceiver<SensorPcc, SensorData> mSensorStateChangeReceiver;
    private final ISensorHandler<SensorPcc, SensorData> mSensorHandler;
    private final ConcurrentLinkedDeque<SensorData> mSensorData;
    private DeviceState deviceState = DeviceState.SEARCHING;
    private PccReleaseHandle<SensorPcc> mReleaseHandle;
    private SensorPcc resultConnection;

    /**
     * Constructor.
     *
     * @param deviceType The type of device that has the connection.
     */
    public AntPlusConnectedSensor(DeviceType deviceType, ISensorHandler<SensorPcc, SensorData> sensorHandler) {
        this.deviceType = deviceType.toString();
        mSensorHandler = sensorHandler;
        mSensorResultReceiver = new SensorResultReceiver<>(this);
        mSensorStateChangeReceiver = new SensorStateChangeReceiver<>(this);
        mSensorData = new ConcurrentLinkedDeque<>();
        mSensorHandler.setDataDeque(mSensorData);
        connectSensor();
    }

    public void connectSensor() {
        mReleaseHandle = mSensorHandler.getReleaseHandle(mSensorResultReceiver, mSensorStateChangeReceiver);
    }

    public void disconnectSensor() {
        resultConnection.releaseAccess();
    }

    /**
     * Abstracted method, that is used to record the events.
     *
     * @param sensorPcc The connection to the sensor.
     */
    protected void subscribeToEvents(SensorPcc sensorPcc) {
        mSensorHandler.subscribeToEvents(sensorPcc);
    }

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
