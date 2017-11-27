package nl.easthome.ebikereader.Sensors;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;

import java.math.BigDecimal;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;

public class EBikeSpeedSensorImplementation implements ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> {
    private BigDecimal mWheelCircumference;

    public EBikeSpeedSensorImplementation(BigDecimal wheelCircumference) {
        mWheelCircumference = wheelCircumference;
    }

    @Override
    public AntPlusSpeedSensorData subscribeToEvents(AntPlusBikeSpeedDistancePcc sensorConnection) {
        sensorConnection.subscribeMotionAndSpeedDataEvent(new AntPlusBikeSpeedDistancePcc.IMotionAndSpeedDataReceiver() {
            @Override
            public void onNewMotionAndSpeedData(long l, EnumSet<EventFlag> enumSet, boolean b) {
                Log.d("SPEEDSENSOR MOTION", "long: " + String.valueOf(l) + " enumset: " + enumSet.toString() + " boolean: " + b);
            }
        });
        sensorConnection.subscribeCalculatedSpeedEvent(new AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver(mWheelCircumference) {
            @Override
            public void onNewCalculatedSpeed(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                Log.d("SPEEDSENSOR CALCSPEED", "long: " + String.valueOf(l) + " enumset: " + enumSet.toString() + " bigdecimal: " + bigDecimal.toString());
            }
        });
        sensorConnection.subscribeRawSpeedAndDistanceDataEvent(new AntPlusBikeSpeedDistancePcc.IRawSpeedAndDistanceDataReceiver() {
            @Override
            public void onNewRawSpeedAndDistanceData(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, long l1) {
                Log.d("SPEEDSENSOR RAWSPEED", "long: " + String.valueOf(l) + " enumset: " + enumSet.toString() + " boolean: " + bigDecimal.toString() + " long1: " + String.valueOf(l1));
            }
        });
        sensorConnection.subscribeCalculatedAccumulatedDistanceEvent(new AntPlusBikeSpeedDistancePcc.CalculatedAccumulatedDistanceReceiver(mWheelCircumference) {
            @Override
            public void onNewCalculatedAccumulatedDistance(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                Log.d("SPEEDSENSOR ACCDIST", "long: " + String.valueOf(l) + " enumset: " + enumSet.toString() + " bigdecimal: " + bigDecimal.toString());
            }
        });

        return null;
    }
}
