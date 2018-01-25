package nl.easthome.ebikereader.Objects;

import nl.easthome.ebikereader.Fragments.BikeInfoSlideFragment;
import nl.easthome.ebikereader.Fragments.BodyMeasurementSlideFragment;

public class UserDetails {
    private int UserHeight;
    private int UserWeight;
    private int UserAge;
    private String UserGender;
    private String BikeModel;
    private int BikeKmRange;
    private int BikeBattWatt;
    private int BikeMaxSpeed;
    private int BikeWheelCircumference;

    public UserDetails() {

    }

    public void fillUserBodyFields(BodyMeasurementSlideFragment bodyMeasurementSlideFragment) {
        UserHeight = bodyMeasurementSlideFragment.getUserHeight();
        UserWeight = bodyMeasurementSlideFragment.getUserWeight();
        UserAge = bodyMeasurementSlideFragment.getUserAge();
        UserGender = bodyMeasurementSlideFragment.getUserGender();
    }


    public void fillBikeInfoFields(BikeInfoSlideFragment bikeInfoSlideFragment) {
        BikeModel = bikeInfoSlideFragment.getBikeModel();
        BikeKmRange = bikeInfoSlideFragment.getBikeKmRange();
        BikeBattWatt = bikeInfoSlideFragment.getBikeBattWatt();
        BikeMaxSpeed = bikeInfoSlideFragment.getBikeMaxSpeed();
        BikeWheelCircumference = bikeInfoSlideFragment.getBikeWheelCircumference();
    }

    public int getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(int userHeight) {
        UserHeight = userHeight;
    }

    public int getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(int userWeight) {
        UserWeight = userWeight;
    }

    public String getBikeModel() {
        return BikeModel;
    }

    public void setBikeModel(String bikeModel) {
        BikeModel = bikeModel;
    }

    public int getBikeKmRange() {
        return BikeKmRange;
    }

    public void setBikeKmRange(int bikeKmRange) {
        BikeKmRange = bikeKmRange;
    }

    public int getBikeBattWatt() {
        return BikeBattWatt;
    }

    public void setBikeBattWatt(int bikeBattWatt) {
        BikeBattWatt = bikeBattWatt;
    }

    public int getBikeMaxSpeed() {
        return BikeMaxSpeed;
    }

    public void setBikeMaxSpeed(int bikeMaxSpeed) {
        BikeMaxSpeed = bikeMaxSpeed;
    }

    public int getBikeWheelCircumference() {
        return BikeWheelCircumference;
    }

    public void setBikeWheelCircumference(int bikeWheelCircumference) {
        BikeWheelCircumference = bikeWheelCircumference;
    }

    public int getUserAge() {
        return UserAge;
    }

    public void setUserAge(int userAge) {
        UserAge = userAge;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        this.UserGender = userGender;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "UserHeight=" + UserHeight +
                ", UserWeight=" + UserWeight +
                ", UserAge=" + UserAge +
                ", UserGender='" + UserGender + '\'' +
                ", BikeModel='" + BikeModel + '\'' +
                ", BikeKmRange=" + BikeKmRange +
                ", BikeBattWatt=" + BikeBattWatt +
                ", BikeMaxSpeed=" + BikeMaxSpeed +
                ", BikeWheelCircumference=" + BikeWheelCircumference +
                '}';
    }
}
