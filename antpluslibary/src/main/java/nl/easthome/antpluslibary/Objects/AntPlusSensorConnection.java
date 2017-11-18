package nl.easthome.antpluslibary.Objects;
import android.app.Activity;

import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeChangeReceiver;

public class AntPlusSensorConnection<T extends AntPluginPcc> {
    private PccReleaseHandle<T> mReleaseHandle;
    private SensorResultReceiver mSensorResultReceiver;
    private SensorStateChangeChangeReceiver mSensorStateChangeReceiver;
    private Activity mActivity;
    private T resultConnection;

    public AntPlusSensorConnection(Activity activity) {
        mActivity = activity;
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
    }

    public T getResultConnection() {
        return resultConnection;
    }

    public PccReleaseHandle<T> getReleaseHandle() {
        return mReleaseHandle;
    }
}
