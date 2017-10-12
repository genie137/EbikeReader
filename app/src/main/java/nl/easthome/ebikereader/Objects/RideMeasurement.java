package nl.easthome.ebikereader.Objects;

import android.location.Location;

public class RideMeasurement {

	private Location mLocation;

	public RideMeasurement() {
	}

	public RideMeasurement(Location location) {
		mLocation = location;
	}

	public Location getLocation() {
		return mLocation;
	}
}
