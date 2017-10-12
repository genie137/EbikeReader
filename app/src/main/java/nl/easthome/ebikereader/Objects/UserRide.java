package nl.easthome.ebikereader.Objects;

public class UserRide {

	private String rideId;
	private String userID;

	public UserRide() {
	}

	public UserRide(String rideId, String userID) {
		this.rideId = rideId;
		this.userID = userID;
	}

	public String getRideId() {
		return rideId;
	}

	public void setRideId(String rideId) {
		this.rideId = rideId;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}


}
