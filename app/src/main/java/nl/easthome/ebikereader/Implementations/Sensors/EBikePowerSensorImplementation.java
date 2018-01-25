package nl.easthome.ebikereader.Implementations.Sensors;

import android.app.Activity;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.ebikereader.SensorData.AntPlusPowerSensorData;

public class EBikePowerSensorImplementation extends ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> {


    public EBikePowerSensorImplementation(Activity activity, int deviceID) {
        super(activity, deviceID);
    }

    @Override
    public void subscribeToEvents(AntPlusBikePowerPcc sensorConnection) {
        sensorConnection.subscribeCalculatedPowerEvent(new AntPlusBikePowerPcc.ICalculatedPowerReceiver() {
            /**
             * Gets called when new data is received from the sensor.
             *
             * @param l          Estimated Timestamp
             * @param enumSet    Event Flags
             * @param dataSource Data Source
             * @param bigDecimal Calculated Power
             */
            @Override
            public void onNewCalculatedPower(long l, EnumSet<EventFlag> enumSet, AntPlusBikePowerPcc.DataSource dataSource, BigDecimal bigDecimal) {
                Log.d("POWERSENSOR", "esttime: " + String.valueOf(l) + " eventflags: " + enumSet.toString() + " dataSource: " + dataSource.toString() + " calcpower: " + bigDecimal.toString());
                AntPlusPowerSensorData dataset = getLatestNonCompletedDataset(AntPlusPowerSensorData.class);
                dataset.dosetCalculatedPower(bigDecimal);
            }
        });
    }

    @Override
    public PccReleaseHandle<AntPlusBikePowerPcc> getReleaseHandle(SensorResultReceiver<AntPlusBikePowerPcc, AntPlusPowerSensorData> resultReceiver, SensorStateChangeReceiver<AntPlusBikePowerPcc, AntPlusPowerSensorData> stateReceiver) {
        return AntPlusBikePowerPcc.requestAccess(mActivity, mDeviceID, PROXIMITY, resultReceiver, stateReceiver);
    }
}
