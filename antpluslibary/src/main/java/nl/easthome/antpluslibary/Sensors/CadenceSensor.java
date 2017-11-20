package nl.easthome.antpluslibary.Sensors;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class CadenceSensor extends AntPlusSensorConnection<AntPlusBikeCadencePcc> {
    private RideRecordingService mRideRecordingService;
    private AntPlusSensorConnection<AntPlusBikeCadencePcc> mConnectionFromSavedDevice;

    public CadenceSensor(RideRecordingService rideRecordingService, AntPlusSensorConnection connectionFromSavedDevice) {
        mRideRecordingService = rideRecordingService;
        mConnectionFromSavedDevice = connectionFromSavedDevice;
    }

    @Override
    protected void subscribeToEvents() {
        AntPlusBikeCadencePcc cadencePcc = mConnectionFromSavedDevice.getResultConnection();
    }


    @Override
    public ArrayList<String> getSensorInfoAtTimestamp(long timestamp) {
        return null;
    }
}
