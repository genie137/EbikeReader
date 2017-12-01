package nl.easthome.antpluslibary.Objects;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.concurrent.ConcurrentLinkedDeque;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeChangeReceiver;

public abstract class AntPlusConnectedSensor<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private DeviceState deviceState = DeviceState.SEARCHING;

    private PccReleaseHandle<SensorPcc> mReleaseHandle;
    private SensorResultReceiver mSensorResultReceiver;
    private SensorStateChangeChangeReceiver<SensorPcc, SensorData> mSensorStateChangeReceiver;
    private SensorPcc resultConnection;
    private ConcurrentLinkedDeque<SensorData> mSensorData;

    protected AntPlusConnectedSensor() {
        mSensorResultReceiver = new SensorResultReceiver<>(this);
        mSensorStateChangeReceiver = new SensorStateChangeChangeReceiver<>(this);
        mSensorData = new ConcurrentLinkedDeque<>();
    }

    public SensorResultReceiver getSensorResultReceiver() {
        return mSensorResultReceiver;
    }

    public SensorStateChangeChangeReceiver<SensorPcc, SensorData> getSensorStateChangeReceiver() {
        return mSensorStateChangeReceiver;
    }

    public SensorPcc getResultConnection() {
        return resultConnection;
    }

    public void setResultConnection(SensorPcc resultConnection) {
        if (deviceState == DeviceState.TRACKING) {
            this.resultConnection = resultConnection;
            subscribeToEvents(resultConnection);
        }
    }

    protected abstract void subscribeToEvents(SensorPcc sensor);


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
}
