package nl.easthome.ebikereader.Implementations;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import nl.easthome.ebikereader.Services.RideRecordingService;

public class RideRecordingServiceConnection implements ServiceConnection {
    private RideRecordingService mRideRecordingService;
    private boolean mIsServiceBound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        RideRecordingService.RideRecordingBinder binder = (RideRecordingService.RideRecordingBinder) service;
        mRideRecordingService = binder.getService();
        mIsServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mIsServiceBound = false;
    }

    public RideRecordingService getRideRecordingService() {
        return mRideRecordingService;
    }

    public boolean isServiceBound() {
        return mIsServiceBound;
    }
}
