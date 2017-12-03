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

import java.util.ArrayList;
import java.util.List;

import nl.easthome.ebikereader.Helpers.SystemTime;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class RideRecordingMappingHelper extends LocationCallback implements OnMapReadyCallback {
	private static final String LOGTAG = "RideRecordingMapping";
	private RideRecordingService mRideRecordingService;
	private boolean mIsMapReady;
	private SupportMapFragment mMapFragment;
	private GoogleMap mGoogleMap;
	private Polyline mPolyline;


    public RideRecordingMappingHelper(SupportMapFragment mapFragment, RideRecordingService rideRecordingService) {
        mRideRecordingService = rideRecordingService;
        mIsMapReady = false;
        mMapFragment = mapFragment;
        mMapFragment.getMapAsync(this);
    }

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mIsMapReady = true;
	}

	private void addPointToMap(Location location) {
		if (mIsMapReady && mGoogleMap != null) {
			LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());

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

	public void restoreAllPointsOnMap(ArrayList<RideMeasurement> rideMeasurements) {
		if (mIsMapReady && mGoogleMap != null) {
			if (mPolyline == null) {
				mPolyline = mGoogleMap.addPolyline(new PolylineOptions().clickable(true));
				List<LatLng> points = mPolyline.getPoints();
				for (RideMeasurement rideMeasurement : rideMeasurements) {
					points.add(new LatLng(rideMeasurement.getLocation().getLatitude(), rideMeasurement.getLocation().getLongitude()));
				}
				mPolyline.setPoints(points);
			}

			RideMeasurement lastMeasurement = rideMeasurements.get(rideMeasurements.size() - 1);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastMeasurement.getLocation().getLatitude(), lastMeasurement.getLocation().getLongitude()), 16));
		}
	}

	public void removePolyLine(){
		if (mPolyline != null){
			mPolyline.remove();
		}
	}

	@Override
	public void onLocationResult(LocationResult locationResult) {
		addPointToMap(locationResult.getLastLocation());
		mRideRecordingService.addRideMeasurement(new RideMeasurement(locationResult.getLastLocation()), SystemTime.getSystemTimestamp());
		super.onLocationResult(locationResult);
	}
}
