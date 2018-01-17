package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;

public class EBikeHeartSensorImplementation extends ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> {
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
}
