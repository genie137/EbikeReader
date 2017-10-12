package nl.easthome.ebikereader.Services;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.R;

import java.util.List;

public class MappingService implements OnMapReadyCallback {
	private boolean mIsMapReady;
	private DashboardActivity mActivity;
	private SupportMapFragment mMapFragment;
	private GoogleMap mGoogleMap;
	private Polyline mPolyline;


	public MappingService(DashboardActivity activity) {
		mActivity = activity;
		mIsMapReady = false;
		mMapFragment = (SupportMapFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.map);
		mMapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mIsMapReady = true;
	}

	public void addPointToMap(Location location) {
		if (mIsMapReady && mGoogleMap != null) {
			if (mPolyline == null){
				mPolyline = mGoogleMap.addPolyline(new PolylineOptions().clickable(true).add(new LatLng(location.getLatitude(), location.getLongitude())));
			}
			else {
				LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());
				List<LatLng> points = mPolyline.getPoints();
				points.add(newPoint);
				mPolyline.setPoints(points);
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
			}
		}
		else {
			return;
		}
	}
}
