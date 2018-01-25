package nl.easthome.ebikereader.Implementations.Sensors;

import android.app.Activity;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Implementations.SensorResultReceiver;
import nl.easthome.antpluslibary.Implementations.SensorStateChangeReceiver;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.ebikereader.SensorData.AntPlusSpeedSensorData;

public class EBikeSpeedSensorImplementation extends ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> {
    private BigDecimal mWheelCircumference;

    /**
     * Implementation of the SensorHandler for the speed sensor.
     * !
     * @param activity
     * @param deviceID
     * @param wheelCircumference in Meters.
     */
    public EBikeSpeedSensorImplementation(Activity activity, int deviceID, BigDecimal wheelCircumference) {
        super(activity, deviceID);
        this.mWheelCircumference = wheelCircumference;
    }

    @Override
    public void subscribeToEvents(AntPlusBikeSpeedDistancePcc sensorConnection) {
        sensorConnection.subscribeCalculatedSpeedEvent(new AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver(mWheelCircumference) {
            /**
             * Gets called when new data is received from the sensor.
             *
             * @param l          Estimated Timestamp
             * @param enumSet    Event flags
             * @param bigDecimal Calculated Raw Speed in Meters per Seconds (see source)
             */
            @Override
            public void onNewCalculatedSpeed(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                AntPlusSpeedSensorData dataset = getLatestNonCompletedDataset(AntPlusSpeedSensorData.class);
                dataset.dosetSpeedInMeterPerSecond(bigDecimal);
            }
        });
        sensorConnection.subscribeCalculatedAccumulatedDistanceEvent(new AntPlusBikeSpeedDistancePcc.CalculatedAccumulatedDistanceReceiver(mWheelCircumference) {
            /**
             * Gets called when new data is received from the sensor.
             * @param l             Estimated Timestamp
             * @param enumSet       Event Flags
             * @param bigDecimal    Calculated Accumulated Distance in Meters (see source)
             */
            @Override
            public void onNewCalculatedAccumulatedDistance(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                AntPlusSpeedSensorData dataset = getLatestNonCompletedDataset(AntPlusSpeedSensorData.class);
                dataset.dosetCalcAccumulatedDistanceInMeters(bigDecimal);
            }
        });
    }

    @Override
    public PccReleaseHandle<AntPlusBikeSpeedDistancePcc> getReleaseHandle(SensorResultReceiver<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> resultReceiver, SensorStateChangeReceiver<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> stateReceiver) {
        return AntPlusBikeSpeedDistancePcc.requestAccess(mActivity, mDeviceID, PROXIMITY, false, resultReceiver, stateReceiver);
    }
}
