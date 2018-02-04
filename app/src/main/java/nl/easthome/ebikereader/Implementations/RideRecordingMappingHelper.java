package nl.easthome.ebikereader.Implementations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGivenException;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Objects.FirebaseLocation;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.RideRecordingService;

import static android.content.Context.LOCATION_SERVICE;

public class RideRecordingMappingHelper extends LocationCallback implements OnMapReadyCallback {
    private LocationRequest mLocationRequest = new LocationRequest().setInterval(Constants.MAX_LOCATION_INTERVAL_MS).setFastestInterval(Constants.MIN_LOCATION_INTERVAL_MS).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private FusedLocationProviderClient mFusedLocationClient;
    private RideRecordingService mRideRecordingService;
    private Activity mActivity;
    private boolean mIsMapReady;
    private GoogleMap mGoogleMap;
	private Polyline mPolyline;
    private SupportMapFragment mMapFragment;


    public RideRecordingMappingHelper(Activity activity, SupportMapFragment mapFragment, RideRecordingService rideRecordingService) {
        mRideRecordingService = rideRecordingService;
        mActivity = activity;
        mIsMapReady = false;
        mMapFragment = mapFragment;
        mapFragment.getMapAsync(this);
    }

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mIsMapReady = true;
	}

	public void addPointToMap(Location location) {
		doAddPointToMap(location.getLatitude(), location.getLongitude());
	}

	public void addPointToMap(FirebaseLocation location) {
		doAddPointToMap(location.getLatitude(), location.getLongitude());
	}

	private void doAddPointToMap(double latitude, double longitude){
		if (mIsMapReady && mGoogleMap != null) {
			LatLng newPoint = new LatLng(latitude, longitude);

			if (mPolyline == null){
				mPolyline = mGoogleMap.addPolyline(new PolylineOptions().clickable(true).add(newPoint));
			}
			else {
				List<LatLng> points = mPolyline.getPoints();
				points.add(newPoint);
				mPolyline.setPoints(points);
			}
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
		}
	}

    private void setListWithPoints(List<LatLng> list) {
        if (mIsMapReady && mGoogleMap != null) {
            if (mPolyline == null) {
                mPolyline = mGoogleMap.addPolyline(new PolylineOptions().clickable(true).addAll(list));
            } else {
                List<LatLng> points = mPolyline.getPoints();
                points.addAll(list);
                mPolyline.setPoints(points);
            }
        }
    }

    private void focusOnLastAddedPoint() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPolyline.getPoints().get(mPolyline.getPoints().size() - 1), 16));
    }



    @Override
	public void onLocationResult(LocationResult locationResult) {
	    if (mRideRecordingService != null) {
            addPointToMap(locationResult.getLastLocation());
            mRideRecordingService.addRideMeasurement(new RideMeasurement(locationResult.getLastLocation()), Constants.getSystemTimestamp());
            super.onLocationResult(locationResult);
        }
	}

    /**
     * Starts the requesting of location updates.
     * MissingPermission Lint is being suppressed because checkLocationDeviceState does the permission checking.
     */
    @SuppressLint("MissingPermission")
    public void startLocationServices() throws NoLocationPermissionGivenException, LocationIsDisabledException {
        if (checkLocationDeviceState()) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
        }
    }

    /**
     * Stops the location services.
     */
    public void stopLocationServices() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(this);
        }
    }


    public boolean checkLocationDeviceState() throws LocationIsDisabledException, NoLocationPermissionGivenException {
        LocationManager mLocationManager = (LocationManager) mActivity.getApplicationContext().getSystemService(LOCATION_SERVICE);

        //Check for network location permission
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check for gps sensor location permission
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Check if gps or network provider is enabled
                if (mLocationManager != null) {
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        return true;
                    } else {
                        throw new LocationIsDisabledException();
                    }
                }
            } else {
                throw new NoLocationPermissionGivenException();
            }
        } else {
            throw new NoLocationPermissionGivenException();
        }
        return false;
    }

    public SupportMapFragment getMapFragment() {
        return mMapFragment;
    }
}
