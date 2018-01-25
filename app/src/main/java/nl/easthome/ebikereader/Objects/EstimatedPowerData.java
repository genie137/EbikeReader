package nl.easthome.ebikereader.Objects;

import android.app.Activity;

import com.google.firebase.database.Exclude;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;
import nl.easthome.ebikereader.R;

public class EstimatedPowerData extends AntPlusSensorData {

    private static final int formulaDurationInMinutes = 1;
    private static final double formulaConvertKcaltoWh = 1.163;
    private double estimatedUserWatt = -1;
    private double estimatedMotorWatt = -1;
    private int estimatedUserPercentage = -1;
    private int estimatedMotorPercentage = -1;
    @Exclude
    private Activity mActivity;

    public EstimatedPowerData() {
    }

    public EstimatedPowerData(Activity activity) {
        mActivity = activity;
    }


    public EstimatedPowerData(double userHeartRate, int userAge, double userWeight, String userGender, double bikePowerReading) {
        String[] genders = mActivity.getResources().getStringArray(R.array.genders);


        if (userGender.equals(genders[0])) {
            double mAgeCalc = userAge * 0.217;
            double mWeightCalc = userWeight * 0.1988;
            double mHrCalc = userHeartRate * 0.6309;
            double mUserKcal = ((mAgeCalc + mWeightCalc + mHrCalc - 55.0969) * formulaDurationInMinutes) / 4.184;
            estimatedUserWatt = mUserKcal / formulaConvertKcaltoWh;
            estimatedMotorWatt = bikePowerReading - estimatedUserWatt;
        } else if (userGender.equals(genders[1])) {
            double fAgeCalc = userAge * 0.074;
            double fWeightCalc = userWeight * 0.1263;
            double fHrCalc = userHeartRate * 0.4472;
            double fUserKcal = ((fAgeCalc + fWeightCalc + fHrCalc - 20.4022) * formulaDurationInMinutes) / 4.184;
            estimatedUserWatt = fUserKcal / formulaConvertKcaltoWh;
            estimatedMotorWatt = bikePowerReading - estimatedUserWatt;
        } else {
            estimatedUserWatt = -9999;
            estimatedMotorWatt = -9999;
        }

        estimatedMotorPercentage = (int) Math.round(estimatedMotorWatt/bikePowerReading);
        estimatedUserPercentage = (int) Math.round(estimatedUserWatt/bikePowerReading);
    }

    @Override
    protected void verifyDatasetCompleted() {
        if (estimatedUserWatt > -1 && estimatedMotorWatt > -1 && estimatedUserPercentage > -1 && estimatedMotorPercentage > -1){
            super.isDatasetComplete = true;
        }
    }

    public double getEstimatedUserWatt() {
        return estimatedUserWatt;
    }

    public void setEstimatedUserWatt(double estimatedUserWatt) {
        this.estimatedUserWatt = estimatedUserWatt;
    }

    public double getEstimatedMotorWatt() {
        return estimatedMotorWatt;
    }

    public void setEstimatedMotorWatt(double estimatedMotorWatt) {
        this.estimatedMotorWatt = estimatedMotorWatt;
    }

    public int getEstimatedUserPercentage() {
        return estimatedUserPercentage;
    }

    public void setEstimatedUserPercentage(int estimatedUserPercentage) {
        this.estimatedUserPercentage = estimatedUserPercentage;
    }

    public int getEstimatedMotorPercentage() {
        return estimatedMotorPercentage;
    }

    public void setEstimatedMotorPercentage(int estimatedMotorPercentage) {
        this.estimatedMotorPercentage = estimatedMotorPercentage;
    }

    @Override
    public String toString() {
        return "EstimatedPowerData{" +
                "estimatedUserWatt=" + estimatedUserWatt +
                ", estimatedMotorWatt=" + estimatedMotorWatt +
                ", estimatedUserPercentage=" + estimatedUserPercentage +
                ", estimatedMotorPercentage=" + estimatedMotorPercentage +
                '}';
    }
}
