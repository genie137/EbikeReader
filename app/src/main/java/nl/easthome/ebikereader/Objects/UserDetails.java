package nl.easthome.ebikereader.Objects;

import nl.easthome.ebikereader.Fragments.BikeInfoSlideFragment;
import nl.easthome.ebikereader.Fragments.BodyMeasurementSlideFragment;

public class UserDetails {
    private double UserHeight;
    private double UserWeight;
    private String BikeModel;
    private double BikeKmRange;
    private double BikeBattWatt;
    private double BikeMaxSpeed;
    private double BikeWheelCircumference;

    public UserDetails() {

    }

    public void fillUserBodyFields(BodyMeasurementSlideFragment bodyMeasurementSlideFragment) {
        UserHeight = bodyMeasurementSlideFragment.getUserHeight();
        UserWeight = bodyMeasurementSlideFragment.getUserWeight();
    }


    public void fillBikeInfoFields(BikeInfoSlideFragment bikeInfoSlideFragment) {
        BikeModel = bikeInfoSlideFragment.getBikeModel();
        BikeKmRange = bikeInfoSlideFragment.getBikeKmRange();
        BikeBattWatt = bikeInfoSlideFragment.getBikeBattWatt();
        BikeMaxSpeed = bikeInfoSlideFragment.getBikeMaxSpeed();
        BikeWheelCircumference = bikeInfoSlideFragment.getBikeWheelCircumference();
    }

    public double getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(double userHeight) {
        UserHeight = userHeight;
    }

    public double getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(double userWeight) {
        UserWeight = userWeight;
    }

    public String getBikeModel() {
        return BikeModel;
    }

    public void setBikeModel(String bikeModel) {
        BikeModel = bikeModel;
    }

    public double getBikeKmRange() {
        return BikeKmRange;
    }

    public void setBikeKmRange(double bikeKmRange) {
        BikeKmRange = bikeKmRange;
    }

    public double getBikeBattWatt() {
        return BikeBattWatt;
    }

    public void setBikeBattWatt(double bikeBattWatt) {
        BikeBattWatt = bikeBattWatt;
    }

    public double getBikeMaxSpeed() {
        return BikeMaxSpeed;
    }

    public void setBikeMaxSpeed(double bikeMaxSpeed) {
        BikeMaxSpeed = bikeMaxSpeed;
    }

    public double getBikeWheelCircumference() {
        return BikeWheelCircumference;
    }

    public void setBikeWheelCircumference(double bikeWheelCircumference) {
        BikeWheelCircumference = bikeWheelCircumference;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "UserHeight=" + UserHeight +
                ", UserWeight=" + UserWeight +
                ", BikeModel='" + BikeModel + '\'' +
                ", BikeKmRange=" + BikeKmRange +
                ", BikeBattWatt=" + BikeBattWatt +
                ", BikeMaxSpeed=" + BikeMaxSpeed +
                ", BikeWheelCircumference=" + BikeWheelCircumference +
                '}';
    }
}
