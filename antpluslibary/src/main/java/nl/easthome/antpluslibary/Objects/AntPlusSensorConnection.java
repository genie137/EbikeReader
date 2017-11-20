package nl.easthome.antpluslibary.Objects;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeChangeReceiver;

public abstract class AntPlusSensorConnection<T extends AntPluginPcc> {
    private DeviceState deviceState = DeviceState.SEARCHING;

    private PccReleaseHandle<T> mReleaseHandle;
    private SensorResultReceiver mSensorResultReceiver;
    private SensorStateChangeChangeReceiver mSensorStateChangeReceiver;
    private T resultConnection;

    public AntPlusSensorConnection() {
        mSensorResultReceiver = new SensorResultReceiver<>(this);
        mSensorStateChangeReceiver = new SensorStateChangeChangeReceiver(this);
    }

    public SensorResultReceiver getSensorResultReceiver() {
        return mSensorResultReceiver;
    }

    public SensorStateChangeChangeReceiver getSensorStateChangeReceiver() {
        return mSensorStateChangeReceiver;
    }

    public void setReleaseHandle(PccReleaseHandle<T> releaseHandle) {
        this.mReleaseHandle = releaseHandle;
    }

    public void setResultConnection(T resultConnection) {
        this.resultConnection = resultConnection;
        subscribeToEvents();
    }

    public T getResultConnection() {
        return resultConnection;
    }

    protected abstract void subscribeToEvents();

    abstract public ArrayList<String> getSensorInfoAtTimestamp(long timestamp);

    public PccReleaseHandle<T> getReleaseHandle() {
        return mReleaseHandle;
    }

    public DeviceState getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(DeviceState newDeviceState) {
        if (newDeviceState == DeviceState.TRACKING){
            subscribeToEvents();
        }
        this.deviceState = newDeviceState;
    }
}
