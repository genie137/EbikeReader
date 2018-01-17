package nl.easthome.ebikereader.Helpers;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;
import nl.easthome.ebikereader.Exceptions.ExportException;
import nl.easthome.ebikereader.Objects.EstimatedPowerData;
import nl.easthome.ebikereader.Objects.FirebaseLocation;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;


public class CSVExportHelper extends AsyncTask<Void,Void,String> {

    private RideRecording mRideRecording;
    private static final String errorString = "error";
    private static final String header_time = "UNIXtimestamp,";
    private static final String header_gps = "gps_accuracy,gps_altitude,gps_bearing,gps_elapsedrealtimenanos,gps_latitude,gps_longitude,gps_provider,gps_speed,gps_time,";
    private static final String header_speedsens = "speedsens_speed_ms,speedsens_distance_meters,";
    private static final String header_cadancesens = "cadancesens_calculated,cadancesens_cumulative_resolutions,";
    private static final String header_powersens = "powersens_watt,";
    private static final String header_heartsens = "heartsens_bpm,";
    private static final String header_est = "est_user_watt,est_motor_watt,est_user_percentage,est_motor_percentage";
    private static final String csv_line_break = "\n";
    private static final String csv_column_break = ",";
    private static final String unknown_item = "X";





    public CSVExportHelper(RideRecording rideRecording) {
        mRideRecording = rideRecording;
    }

    public String export() throws ExecutionException, InterruptedException, ExportException {
        String output = this.execute().get();
        if (output.equals(errorString)){
            throw new ExportException();
        }
        else {
            return output;
        }
    }


    @Override
    protected String doInBackground(Void... voids) {
        // Step 1 - creating an export location
        File folder = new File(Environment.getExternalStorageDirectory() + "/EbikeReader");
        boolean doesFolderExists = folder.exists();
        if (!doesFolderExists) {
            folder.mkdir();
        }

        // Step 2 - creating file an create column headers
        final String filename = folder.toString() + "/Ride" + Constants.convertTimestampToSimpleDateTime(mRideRecording.getRideStart()) + ".csv";
        try {

            FileWriter fw = new FileWriter(filename);
            fw.append(header_time + header_gps + header_speedsens + header_cadancesens + header_powersens + header_heartsens + header_est + csv_line_break);

            for (Map.Entry<String, RideMeasurement> recording : mRideRecording.getRideMeasurements().entrySet()) {

                StringBuilder csvLine= new StringBuilder();
                RideMeasurement recordingValue = recording.getValue();
                String timestamp = recording.getKey();

                csvLine.append(timestamp + csv_column_break);
                csvLine.append(csvLocation(recordingValue.getLocation()));
                csvLine.append(csvSpeedSensor(recordingValue.getSpeedSensorData()));
                csvLine.append(csvCadenceSensor(recordingValue.getCadenceSensorData()));
                csvLine.append(csvPowerSensor(recordingValue.getPowerSensorData()));
                csvLine.append(csvHeartSensor(recordingValue.getHeartSensorData()));
                csvLine.append(csvEstimatedSensor(recordingValue.getEstimatedPowerData()));
                csvLine.append(csv_line_break);

            }
            fw.close();
            return filename;

        } catch (IOException e) {
            e.printStackTrace();
            return errorString;
        }
    }

    private String csvEstimatedSensor(EstimatedPowerData ed) {
        StringBuilder estimatedLine = new StringBuilder();

        if (ed != null){
            estimatedLine.append(ed.getEstimatedUserWatt() + csv_column_break);
            estimatedLine.append(ed.getEstimatedMotorWatt() + csv_column_break);
            estimatedLine.append(ed.getEstimatedUserPercentage() + csv_column_break);
            estimatedLine.append(ed.getEstimatedMotorPercentage());


            return estimatedLine.toString();
        }
        else {
            return estimatedLine.append(unknown_item + csv_column_break + unknown_item + csv_column_break + unknown_item + csv_column_break + unknown_item ).toString();
        }
    }

    private String csvHeartSensor(AntPlusHeartSensorData hd) {
        if (hd != null) {
            return hd.getHeartRate() + csv_column_break;
        }
        else {
            return unknown_item + csv_column_break;
        }
    }

    private String csvPowerSensor(AntPlusPowerSensorData pd) {
        if (pd != null) {
            return pd.getCalculatedPower() + csv_column_break;
        }
        else {
            return unknown_item + csv_column_break;
        }
    }

    private String csvCadenceSensor(AntPlusCadenceSensorData cd) {
        StringBuilder cadenceLine = new StringBuilder();

        if (cd != null) {
            cadenceLine.append(cd.getCalculatedCadence() + csv_column_break + cd.getCumulativeResolutions() + csv_column_break);
            return cadenceLine.toString();

        } else {
            cadenceLine.append(unknown_item + csv_column_break + unknown_item + csv_column_break);
            return cadenceLine.toString();

        }
    }

    private String csvSpeedSensor(AntPlusSpeedSensorData sd) {
        StringBuilder speedLine = new StringBuilder();

        if (sd != null) {
            speedLine.append(sd.getSpeedInMeterPerSecond() + csv_column_break + sd.getCalcAccumulatedDistanceInMeters() + csv_column_break);
            return speedLine.toString();
        } else {
            speedLine.append(unknown_item + csv_column_break + unknown_item + csv_column_break);
            return speedLine.toString();
        }
    }

    private String csvLocation(FirebaseLocation l){
        StringBuilder locationLine = new StringBuilder();
        if (l != null) {
            locationLine.append(l.getAccuracy() + csv_column_break
                    + l.getAltitude() + csv_column_break
                    + l.getBearing() + csv_column_break
                    + l.getElapsedRealTimeNanos() + csv_column_break
                    + l.getLatitude() + csv_column_break
                    + l.getLongitude() + csv_column_break
                    + l.getProvider() + csv_column_break
                    + l.getSpeed() + csv_column_break
                    + l.getTime() + csv_column_break);
            return locationLine.toString();
        } else {
            for (int i = 0; i < 9; i++) {
                locationLine.append(unknown_item);
                locationLine.append(csv_column_break);
            }
            return locationLine.toString();
        }
    }

}
