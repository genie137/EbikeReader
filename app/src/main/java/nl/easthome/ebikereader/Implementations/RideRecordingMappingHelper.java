package nl.easthome.ebikereader.Implementations;

import android.location.Location;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Objects.FirebaseLocation;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class RideRecordingMappingHelper extends LocationCallback implements OnMapReadyCallback {
	private RideRecordingService mRideRecordingService;
	private boolean mIsMapReady;
	private GoogleMap mGoogleMap;
	private Polyline mPolyline;


    public RideRecordingMappingHelper(SupportMapFragment mapFragment, RideRecordingService rideRecordingService) {
        mRideRecordingService = rideRecordingService;
        mIsMapReady = false;
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

    @Override
	public void onLocationResult(LocationResult locationResult) {
	    if (mRideRecordingService != null) {
            addPointToMap(locationResult.getLastLocation());
            mRideRecordingService.addRideMeasurement(new RideMeasurement(locationResult.getLastLocation()), Constants.getSystemTimestamp());
            super.onLocationResult(locationResult);
        }
	}
}
