package nl.easthome.ebikereader.Objects;

public class UserDetails {
    private String UserHeight;
    private String UserWeight;
    private String BikeModel;
    private String BikeKmRange;
    private String BikeBattWatt;
    private String BikeMaxSpeed;
    private String BikeWheelCircumference;

    public UserDetails() {

    }

    public UserDetails(String userHeight, String userWeight, String bikeModel, String bikeKmRange, String bikeBattWatt, String bikeMaxSpeed, String bikeWheelCircumference) {
        UserHeight = userHeight;
        UserWeight = userWeight;
        BikeModel = bikeModel;
        BikeKmRange = bikeKmRange;
        BikeBattWatt = bikeBattWatt;
        BikeMaxSpeed = bikeMaxSpeed;
        BikeWheelCircumference = bikeWheelCircumference;
    }

    public String getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(String userHeight) {
        UserHeight = userHeight;
    }

    public String getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(String userWeight) {
        UserWeight = userWeight;
    }

    public String getBikeModel() {
        return BikeModel;
    }

    public void setBikeModel(String bikeModel) {
        BikeModel = bikeModel;
    }

    public String getBikeKmRange() {
        return BikeKmRange;
    }

    public void setBikeKmRange(String bikeKmRange) {
        BikeKmRange = bikeKmRange;
    }

    public String getBikeBattWatt() {
        return BikeBattWatt;
    }

    public void setBikeBattWatt(String bikeBattWatt) {
        BikeBattWatt = bikeBattWatt;
    }

    public String getBikeMaxSpeed() {
        return BikeMaxSpeed;
    }

    public void setBikeMaxSpeed(String bikeMaxSpeed) {
        BikeMaxSpeed = bikeMaxSpeed;
    }

    public String getBikeWheelCircumference() {
        return BikeWheelCircumference;
    }

    public void setBikeWheelCircumference(String bikeWheelCircumference) {
        BikeWheelCircumference = bikeWheelCircumference;
    }
}
