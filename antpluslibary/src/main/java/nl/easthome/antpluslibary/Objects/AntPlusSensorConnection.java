package nl.easthome.antpluslibary.Objects;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.ArrayDeque;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeChangeReceiver;

public abstract class AntPlusSensorConnection<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private DeviceState deviceState = DeviceState.SEARCHING;

    private PccReleaseHandle<SensorPcc> mReleaseHandle;
    private SensorResultReceiver mSensorResultReceiver;
    private SensorStateChangeChangeReceiver mSensorStateChangeReceiver;
    private SensorPcc resultConnection;
    private ArrayDeque<SensorData> mSensorData;

    public AntPlusSensorConnection() {
        mSensorResultReceiver = new SensorResultReceiver<>(this);
        mSensorStateChangeReceiver = new SensorStateChangeChangeReceiver(this);
        mSensorData = new ArrayDeque<>(50);
    }

    public SensorResultReceiver getSensorResultReceiver() {
        return mSensorResultReceiver;
    }

    public SensorStateChangeChangeReceiver getSensorStateChangeReceiver() {
        return mSensorStateChangeReceiver;
    }

    public SensorPcc getResultConnection() {
        return resultConnection;
    }

    public void setResultConnection(SensorPcc resultConnection) {
        this.resultConnection = resultConnection;
        if (deviceState == DeviceState.TRACKING) {
            subscribeToEvents(resultConnection);
        }
    }

    protected abstract void subscribeToEvents(SensorPcc sensor);

    public void addSensorDataToQueue(SensorData newData) {
        mSensorData.addFirst(newData);
    }

    public SensorData getLastSensorData(long timestamp) {
        if (mSensorData.size() > 0) {
            return mSensorData.getFirst();
        } else {
            return null;
        }
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
}
