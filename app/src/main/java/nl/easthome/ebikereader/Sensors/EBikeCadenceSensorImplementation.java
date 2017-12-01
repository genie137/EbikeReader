package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;

public class EBikeCadenceSensorImplementation extends ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> {
    public EBikeCadenceSensorImplementation() {

    }

    @Override
    public void subscribeToEvents(AntPlusBikeCadencePcc sensorConnection) {
        sensorConnection.subscribeCalculatedCadenceEvent(new AntPlusBikeCadencePcc.ICalculatedCadenceReceiver() {
            /**
             * Gets called when new data is received from the sensor.
             *
             * @param l          Estimated Timestamp
             * @param enumSet    EventFlags
             * @param bigDecimal CalculatedCadence
             */
            @Override
            public void onNewCalculatedCadence(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                AntPlusCadenceSensorData dataset = getLatestNonCompletedDataset(AntPlusCadenceSensorData.class);
                dataset.setCalculatedCadence(bigDecimal);
            }
        });

        sensorConnection.subscribeRawCadenceDataEvent(new AntPlusBikeCadencePcc.IRawCadenceDataReceiver() {
            @Override
            public void onNewRawCadenceData(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, long l1) {
                AntPlusCadenceSensorData dataset = getLatestNonCompletedDataset(AntPlusCadenceSensorData.class);
                dataset.setCumulativeResolutions(l1);
            }
        });
    }
}
