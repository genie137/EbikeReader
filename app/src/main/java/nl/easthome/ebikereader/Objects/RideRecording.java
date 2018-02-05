package nl.easthome.ebikereader.Objects;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.R;
import nl.easthome.ebikereader.SensorData.AntPlusCadenceSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusHeartSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusPowerSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusSpeedSensorData;

public class RideRecording {

    private String rideId;
    private long rideStart;
    private long rideEnd;
    private HashMap<String, RideMeasurement> mRideMeasurements;

    public RideRecording() {
        mRideMeasurements = new HashMap<>();
    }

    public void startRide() {
        rideStart = Constants.getSystemTimestamp();
        rideEnd = 0;
        rideId = FirebaseSaver.addNewRide(this, FirebaseAuth.getInstance().getUid());
    }

    public void stopRide() {
        rideEnd = Constants.getSystemTimestamp();
        FirebaseSaver.updateRideRecording(this);
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public long getRideStart() {
        return rideStart;
    }

    public void setRideStart(long rideStart) {
        this.rideStart = rideStart;
    }

    public long getRideEnd() {
        return rideEnd;
    }

    public void setRideEnd(long rideEnd) {
        this.rideEnd = rideEnd;
    }

    public HashMap<String, RideMeasurement> getRideMeasurements() {
        return mRideMeasurements;
    }

    public void setRideMeasurements(HashMap<String, RideMeasurement> mRideMeasurements) {
        this.mRideMeasurements = mRideMeasurements;
    }

    public void addRideMeasurement(long timestamp, RideMeasurement rideMeasurement) {
        mRideMeasurements.put(String.valueOf(timestamp), rideMeasurement);
        FirebaseSaver.updateRideRecording(this);
    }

    @Exclude
    public String[] createDetailFields(Context context) {
        String[] strings = new String[8];
        strings[0] = durationField(context);
        strings[1] = distanceField(context);
        strings[2] = speedField(context);
        strings[3] = cadenceField(context);
        strings[4] = heartrateField(context);
        strings[5] = powerField(context);
        strings[6] = estTotalUserPowerField(context);
        strings[7] = avgUserEngineRatioField(context);
        return strings;
    }

    @Exclude
    private String avgUserEngineRatioField(Context context) {
        Double userPercentageTotal = 0d;
        Double enginePercentageTotal = 0d;
        int numberOfPowerMeasurements = 0;

        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getEstimatedPowerData() != null) {
                EstimatedPowerData estimatedPowerData = rideMeasurement.getEstimatedPowerData();
                userPercentageTotal += estimatedPowerData.getEstimatedUserPercentage();
                enginePercentageTotal += estimatedPowerData.getEstimatedMotorPercentage();
                numberOfPowerMeasurements++;
            }
        }

        if (userPercentageTotal > 0 && enginePercentageTotal > 0 && numberOfPowerMeasurements > 0) {
            userPercentageTotal = userPercentageTotal / numberOfPowerMeasurements;
            enginePercentageTotal = enginePercentageTotal / numberOfPowerMeasurements;

            return String.format(Locale.ENGLISH, "( %d / %d )", userPercentageTotal.intValue(), enginePercentageTotal.intValue());
        } else {
            return context.getString(R.string.details_no_measurementdata);

        }
    }

    @Exclude
    private String estTotalUserPowerField(Context context) {
        Double totalUserPower = 0d;
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getEstimatedPowerData() != null) {
                EstimatedPowerData estimatedPowerData = rideMeasurement.getEstimatedPowerData();
                totalUserPower += estimatedPowerData.getEstimatedUserWatt();
            }
        }

        if (totalUserPower > 0) {
            return String.format(Locale.ENGLISH, "%d Wh", totalUserPower.intValue());
        } else {
            return context.getString(R.string.details_no_measurementdata);
        }

    }

    @Exclude
    private String powerField(Context context) {
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        Double powerMax = Double.MIN_VALUE;
        Double powerMin = Double.MAX_VALUE;
        Double powerTotal = 0d;
        int numberOfPowerMeasurements = 0;

        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getCadenceSensorData() != null) {
                AntPlusPowerSensorData cadenceSensorData = rideMeasurement.getPowerSensorData();
                numberOfPowerMeasurements++;
                double power = cadenceSensorData.getCalculatedPower();
                powerTotal += power;
                if (power > powerMax) {
                    powerMax = power;
                }
                if (power < powerMax) {
                    powerMin = power;
                }

            }
        }

        if (numberOfPowerMeasurements > 0 && powerTotal > 0) {
            Double avg = powerTotal / numberOfPowerMeasurements;
            return String.format(Locale.ENGLISH, context.getString(R.string.details_minavgmax_field), powerMin.intValue(), avg.intValue(), powerMax.intValue());
        } else {
            return context.getString(R.string.details_no_measurementdata);
        }
    }

    @Exclude
    private String cadenceField(Context context) {
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        Double cadenceMax = Double.MIN_VALUE;
        Double cadenceMin = Double.MAX_VALUE;
        Double cadenceTotal = 0d;
        int numberOfCadenceMeasurements = 0;

        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getCadenceSensorData() != null) {
                AntPlusCadenceSensorData cadenceSensorData = rideMeasurement.getCadenceSensorData();
                numberOfCadenceMeasurements++;
                double cadance = cadenceSensorData.getCalculatedCadence();
                cadenceTotal += cadance;
                if (cadance > cadenceMax) {
                    cadenceMax = cadance;
                }
                if (cadance < cadenceMax) {
                    cadenceMin = cadance;
                }

            }
        }

        if (numberOfCadenceMeasurements > 0 && cadenceTotal > 0) {
            Double avg = cadenceTotal / numberOfCadenceMeasurements;
            return String.format(Locale.ENGLISH, context.getString(R.string.details_minavgmax_field), cadenceMin.intValue(), avg.intValue(), cadenceMax.intValue());
        } else {
            return context.getString(R.string.details_no_measurementdata);
        }
    }

    @Exclude
    private String speedField(Context context) {
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        Double speedMax = Double.MIN_VALUE;
        Double speedMin = Double.MAX_VALUE;
        Double speedTotal = 0d;
        int numberOfSpeedMeasurements = 0;

        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getSpeedSensorData() != null) {
                AntPlusSpeedSensorData speedSensorData = rideMeasurement.getSpeedSensorData();
                numberOfSpeedMeasurements++;
                double speed = speedSensorData.getSpeedInMeterPerSecond();
                speedTotal += speed;
                if (speed > speedMax) {
                    speedMax = speed;
                }
                if (speed < speedMax) {
                    speedMin = speed;
                }

            }
        }

        if (numberOfSpeedMeasurements > 0 && speedTotal > 0) {
            Double avg = speedTotal / numberOfSpeedMeasurements;
            return String.format(Locale.ENGLISH, context.getString(R.string.details_minavgmax_field), speedMin.intValue(), avg.intValue(), speedMax.intValue());
        } else {
            return context.getString(R.string.details_no_measurementdata);
        }
    }

    @Exclude
    private String distanceField(Context context) {
        return context.getString(R.string.details_no_measurementdata);
    }

    @Exclude
    public ArrayList<LatLng> getLatLngList() {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);

        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            latLngList.add(new LatLng(rideMeasurement.getLocation().getLatitude(), rideMeasurement.getLocation().getLongitude()));
        }
        return latLngList;
    }

    @Exclude
    private String durationField(Context context) {
        long duration = rideEnd - rideStart;
        long hours = TimeUnit.SECONDS.toHours(duration);
        long minutes = TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = duration - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);

        return String.format(Locale.ENGLISH, context.getString(R.string.details_hoursminutesseconds), hours, minutes, seconds);
    }

    @Exclude
    private String heartrateField(Context context) {
        TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideMeasurements);
        int hrMax = Integer.MIN_VALUE;
        int hrMin = Integer.MAX_VALUE;
        int hrTotal = 0;
        int numberOfHrMeasurements = 0;

        for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
            RideMeasurement rideMeasurement = measurementEntry.getValue();
            if (rideMeasurement.getHeartSensorData() != null) {
                AntPlusHeartSensorData heartSensorData = rideMeasurement.getHeartSensorData();
                numberOfHrMeasurements++;
                int heartRate = heartSensorData.getHeartRate();
                hrTotal += heartRate;
                if (heartRate > hrMax) {
                    hrMax = heartRate;
                }
                if (heartRate < hrMin) {
                    hrMin = heartRate;
                }

            }
        }

        if (numberOfHrMeasurements > 0 && hrTotal > 0) {
            return String.format(Locale.ENGLISH, context.getString(R.string.details_minavgmax_field), hrMin, (hrTotal / numberOfHrMeasurements), hrMax);
        } else {
            return context.getString(R.string.details_no_measurementdata);
        }
    }











    @Override
    public String toString() {
        return "RideRecording{" +
                "mRideId='" + rideId + '\'' +
                ", rideStart=" + rideStart +
                ", rideEnd=" + rideEnd +
                ", mRideMeasurements=" + mRideMeasurements +
                '}';
    }
}
