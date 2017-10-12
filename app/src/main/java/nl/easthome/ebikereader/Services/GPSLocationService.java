package nl.easthome.ebikereader.Services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.location.*;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import nl.easthome.ebikereader.DashboardActivity;


public class GPSLocationService extends LocationCallback {

	private boolean mIsRequestingLocationUpdates;
	private FusedLocationProviderClient mFusedLocationClient;
	private LocationRequest mLocationRequest = new LocationRequest().setInterval(5000).setFastestInterval(3000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private DashboardActivity mActivity;
	private RideRecorderService mRideRecorderService;

	public GPSLocationService(RideRecorderService rideRecorderService) {
		mRideRecorderService = rideRecorderService;
		mActivity = rideRecorderService.getDashboardActivity();
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
		mIsRequestingLocationUpdates = false;
	}

	public void startLocationUpdates() {
		if (!mIsRequestingLocationUpdates) {
			if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				Dexter.withActivity(mActivity)
						.withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
						.withListener(DialogOnAnyDeniedMultiplePermissionsListener.Builder
											  .withContext(mActivity)
											  .withTitle("WE NEED THIS!")
											  .withMessage("Yo, pls?")
											   .withButtonText("OK")
											  .build())
						.check();
				mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
				mIsRequestingLocationUpdates = true;
			}
			else {
				mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
				mIsRequestingLocationUpdates = true;
			}
		}
	}

	public void stopLocationUpdates() {
		mFusedLocationClient.removeLocationUpdates(this);
		mIsRequestingLocationUpdates = false;
	}


	@Override
	public void onLocationResult(LocationResult locationResult) {
		for (Location location : locationResult.getLocations()){
			mRideRecorderService.onNewLocation(location);
		}
	}
}
