package nl.easthome.ebikereader.Implementations.Sensors;

import android.app.Activity;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.ebikereader.SensorData.AntPlusHeartSensorData;

public class EBikeHeartSensorImplementation extends ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
    public EBikeHeartSensorImplementation(Activity activity, int deviceID) {
        super(activity, deviceID);
    }

    @Override
    public void subscribeToEvents(AntPlusHeartRatePcc sensorConnection) {
        sensorConnection.subscribeHeartRateDataEvent(new AntPlusHeartRatePcc.IHeartRateDataReceiver() {
            /**
             *
             * @param l             timestamp
             * @param enumSet       eventFlags
             * @param i             computedHeartRate
             * @param l1            heartBeatCounter
             * @param bigDecimal    heartBeatEventTime
             * @param dataState     dataState
             */
            @Override
            public void onNewHeartRateData(long l, EnumSet<EventFlag> enumSet, int i, long l1, BigDecimal bigDecimal, AntPlusHeartRatePcc.DataState dataState) {
                AntPlusHeartSensorData dataset = getLatestNonCompletedDataset(AntPlusHeartSensorData.class);
                dataset.dosetHeartrate(i);
            }
        });
    }

    @Override
    public PccReleaseHandle<AntPlusHeartRatePcc> getReleaseHandle(SensorResultReceiver<AntPlusHeartRatePcc, AntPlusHeartSensorData> resultReceiver, SensorStateChangeReceiver<AntPlusHeartRatePcc, AntPlusHeartSensorData> stateReceiver) {
        AntPlusHeartRatePcc.requestAccess(mActivity, mDeviceID, PROXIMITY, resultReceiver, stateReceiver);
        return null;
    }
}
