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
            @Override
            public void onNewHeartRateData(long l, EnumSet<EventFlag> enumSet, int i, long l1, BigDecimal bigDecimal, AntPlusHeartRatePcc.DataState dataState) {

            }
        });
    }
}
