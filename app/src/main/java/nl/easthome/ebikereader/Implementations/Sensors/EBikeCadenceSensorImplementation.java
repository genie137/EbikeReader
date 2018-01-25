package nl.easthome.ebikereader.Implementations.Sensors;

import android.app.Activity;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.ebikereader.SensorData.AntPlusCadenceSensorData;

public class EBikeCadenceSensorImplementation extends ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> {
    public EBikeCadenceSensorImplementation(Activity activity, int deviceID) {
        super(activity, deviceID);
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
                dataset.dosetCalculatedCadence(bigDecimal);
            }
        });

        sensorConnection.subscribeRawCadenceDataEvent(new AntPlusBikeCadencePcc.IRawCadenceDataReceiver() {
            @Override
            public void onNewRawCadenceData(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, long l1) {
                AntPlusCadenceSensorData dataset = getLatestNonCompletedDataset(AntPlusCadenceSensorData.class);
                dataset.dosetCumulativeResolutions(l1);
            }
        });
    }

    @Override
    public PccReleaseHandle<AntPlusBikeCadencePcc> getReleaseHandle(SensorResultReceiver<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> resultReceiver, SensorStateChangeReceiver<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> stateReceiver) {
        return AntPlusBikeCadencePcc.requestAccess(mActivity, mDeviceID, PROXIMITY, false, resultReceiver, stateReceiver);
    }
}
