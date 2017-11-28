package nl.easthome.ebikereader.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class EBikeSpeedSensorImplementation extends ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> {
    private BigDecimal mWheelCircumference;

    /**
     * Implementation of the SensorHandler for the speed sensor.
     *
     * @param wheelCircumference in Meters.
     *                           (source: ANT+ Managed Network Document – Bike Speed and Cadence Device Profiles, Rev 2.1 page 26)
     */
    public EBikeSpeedSensorImplementation(BigDecimal wheelCircumference) {
        mWheelCircumference = wheelCircumference;
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
                dataset.setSpeedInMeterPerSecond(bigDecimal);
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
                dataset.setCalcAccumulatedDistanceInMeters(bigDecimal);
            }
        });
    }

}
