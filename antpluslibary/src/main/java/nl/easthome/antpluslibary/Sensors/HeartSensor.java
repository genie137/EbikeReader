package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class HeartSensor extends AntPlusSensorConnection<AntPlusHeartRatePcc> {
    private RideRecordingService mRideRecordingService;
    private AntPlusSensorConnection<AntPlusHeartRatePcc> mConnectionFromSavedDevice;

    public HeartSensor(RideRecordingService rideRecordingService, AntPlusSensorConnection connectionFromSavedDevice) {
        mRideRecordingService = rideRecordingService;
        mConnectionFromSavedDevice = connectionFromSavedDevice;
    }

    @Override
    protected void subscribeToEvents() {
        AntPlusHeartRatePcc heartPcc = mConnectionFromSavedDevice.getResultConnection();
    }


    @Override
    public ArrayList<String> getSensorInfoAtTimestamp(long timestamp) {
        return null;
    }
}
