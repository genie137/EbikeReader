package nl.easthome.antpluslibary.Sensors;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class SpeedSensor extends AntPlusSensorConnection<AntPlusBikeSpeedDistancePcc> {
    private static final String LOGTAG = "SpeedSensor";
    private RideRecordingService mRideRecordingService;
    private AntPlusSensorConnection<AntPlusBikeSpeedDistancePcc> mConnectionFromSavedDevice;

    public SpeedSensor(RideRecordingService rideRecordingService, AntPlusSensorConnection connectionFromSavedDevice) {
        mRideRecordingService = rideRecordingService;
        mConnectionFromSavedDevice = connectionFromSavedDevice;
    }

    @Override
    protected void subscribeToEvents() {
        AntPlusBikeSpeedDistancePcc speedPcc = mConnectionFromSavedDevice.getResultConnection();
        speedPcc.subscribeMotionAndSpeedDataEvent(new AntPlusBikeSpeedDistancePcc.IMotionAndSpeedDataReceiver() {
            @Override
            public void onNewMotionAndSpeedData(long l, EnumSet<EventFlag> enumSet, boolean b) {
                Log.d(LOGTAG, String.valueOf(l) + ", " + enumSet.toString() + ", " + b);
            }
        });
        speedPcc.subscribeCalculatedSpeedEvent(new AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver(new BigDecimal(0)) {
            @Override
            public void onNewCalculatedSpeed(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal) {
                Log.d(LOGTAG, String.valueOf(l) + ", " + enumSet.toString() + ", " + bigDecimal.toString());
            }
        });
    }


    @Override
    public ArrayList<String> getSensorInfoAtTimestamp(long timestamp) {
        return null;
    }
}
