package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class PowerSensor extends AntPlusSensorConnection<AntPlusBikePowerPcc> {
    private RideRecordingService mRideRecordingService;
    private AntPlusSensorConnection<AntPlusBikePowerPcc> mConnectionFromSavedDevice;

    public PowerSensor(RideRecordingService rideRecordingService, AntPlusSensorConnection connectionFromSavedDevice) {
        mRideRecordingService = rideRecordingService;
        mConnectionFromSavedDevice = connectionFromSavedDevice;
    }

    @Override
    protected void subscribeToEvents() {
        AntPlusBikePowerPcc powerPcc = mConnectionFromSavedDevice.getResultConnection();
    }


    @Override
    public ArrayList<String> getSensorInfoAtTimestamp(long timestamp) {
        return null;
    }
}
