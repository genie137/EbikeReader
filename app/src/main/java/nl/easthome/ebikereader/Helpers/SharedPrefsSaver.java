package nl.easthome.ebikereader.Helpers;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

import nl.easthome.ebikereader.Objects.UserDetails;

public class SharedPrefsSaver {

    private final static String SPPageUserDetails = "userDetails";
    private final static String userHeightKey = "userHeight";
    private final static String userWeightKey = "userWeight";
    private final static String userBikeModelKey = "bikeModel";
    private final static String userBikeBatteryCapacityKey = "bikeBatteryCapacity";
    private final static String userBikeBatteryKmRangeKey = "bikeBatteryKmRange";
    private final static String userBikeMaxSpeedKey = "userHeight";
    private final static String userBikeWheelCircumferenceKey = "bikeWheelCircumference";


    public static void saveUserDetails(Activity activity, UserDetails userDetails){
        SharedPreferences.Editor preferences = activity.getSharedPreferences(SPPageUserDetails, Context.MODE_PRIVATE).edit();

        preferences.putInt(userHeightKey, userDetails.getUserHeight());
        preferences.putInt(userWeightKey, userDetails.getUserWeight());
        preferences.putString(userBikeModelKey, userDetails.getBikeModel());
        preferences.putInt(userBikeBatteryCapacityKey, userDetails.getBikeBattWatt());
        preferences.putInt(userBikeBatteryKmRangeKey, userDetails.getBikeKmRange());
        preferences.putInt(userBikeMaxSpeedKey, userDetails.getBikeMaxSpeed());
        preferences.putInt(userBikeWheelCircumferenceKey, userDetails.getBikeWheelCircumference());

        preferences.apply();
    }

    public static UserDetails getAllUserDetails(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        UserDetails userDetails = new UserDetails();

        userDetails.setUserHeight(preferences.getInt(userHeightKey, 0));
        userDetails.setUserWeight(preferences.getInt(userWeightKey, 0));
        userDetails.setBikeModel(preferences.getString(userBikeModelKey, "0"));
        userDetails.setBikeBattWatt(preferences.getInt(userBikeBatteryCapacityKey,0));
        userDetails.setBikeKmRange(preferences.getInt(userBikeBatteryKmRangeKey, 0));
        userDetails.setBikeMaxSpeed(preferences.getInt(userBikeMaxSpeedKey, 0));
        userDetails.setBikeWheelCircumference(preferences.getInt(userBikeWheelCircumferenceKey, 0));

        return userDetails;
    }

    public static BigDecimal getWheelCircumference(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SPPageUserDetails, Context.MODE_PRIVATE);
        return BigDecimal.valueOf(preferences.getInt(userBikeWheelCircumferenceKey, 207)/100d);
    }




}
