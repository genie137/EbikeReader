package nl.easthome.ebikereader.Helpers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nl.easthome.ebikereader.Activities.RideHistoryDetailsActivity;
import nl.easthome.ebikereader.Exceptions.ExportException;
import nl.easthome.ebikereader.Objects.EstimatedPowerData;
import nl.easthome.ebikereader.Objects.FirebaseLocation;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;
import nl.easthome.ebikereader.SensorData.AntPlusCadenceSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusHeartSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusPowerSensorData;
import nl.easthome.ebikereader.SensorData.AntPlusSpeedSensorData;


public class CSVExportHelper extends AsyncTask<Void,Void,String> {

    private static final String LOGTAG = "CSV_EXPORT_HELPER";
    private static final String errorString = "error";
    private static final String header_time = "UNIXtimestamp,timeString,";
    private static final String header_gps = "gps_accuracy,gps_altitude,gps_bearing,gps_elapsedrealtimenanos,gps_latitude,gps_longitude,gps_provider,gps_speed,gps_time,";
    private static final String header_speedsens = "speedsens_speed_ms,speedsens_distance_meters,";
    private static final String header_cadancesens = "cadancesens_calculated,cadancesens_cumulative_resolutions,";
    private static final String header_powersens = "powersens_watt,";
    private static final String header_heartsens = "heartsens_bpm,";
    private static final String header_est = "est_user_watt,est_motor_watt,est_user_percentage,est_motor_percentage";
    private static final String csv_line_break = "\n";
    private static final String csv_column_break = ",";
    private static final String unknown_item = "X";
    private RideRecording mRideRecording;
    private ProgressDialog mProgressDialog;
    @SuppressLint("StaticFieldLeak")
    private RideHistoryDetailsActivity mRideHistoryDetailsActivity;


    public CSVExportHelper(RideHistoryDetailsActivity detailsActivity, RideRecording rideRecording) {
        mRideRecording = rideRecording;
        mRideHistoryDetailsActivity = detailsActivity;
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
    protected void onPreExecute() {
        super.onPreExecute();
        mRideHistoryDetailsActivity.showOrHideDialog(mRideHistoryDetailsActivity.getString(R.string.progress_csv_export_title), mRideHistoryDetailsActivity.getString(R.string.progress_csv_export_message));
    }

    @Override
    protected void onPostExecute(String s) {
        mRideHistoryDetailsActivity.showOrHideDialog(null, null);
        super.onPostExecute(s);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
                Log.d(LOGTAG, "Processing Measurement");
                StringBuilder csvLine= new StringBuilder();
                RideMeasurement recordingValue = recording.getValue();

                csvLine.append(recordingValue.getTimestamp()).append(csv_column_break).append(recordingValue.getTimeAsString()).append(csv_column_break);
                csvLine.append(csvLocation(recordingValue.getLocation()));
                csvLine.append(csvSpeedSensor(recordingValue.getSpeedSensorData()));
                csvLine.append(csvCadenceSensor(recordingValue.getCadenceSensorData()));
                csvLine.append(csvPowerSensor(recordingValue.getPowerSensorData()));
                csvLine.append(csvHeartSensor(recordingValue.getHeartSensorData()));
                csvLine.append(csvEstimatedSensor(recordingValue.getEstimatedPowerData()));
                csvLine.append(csv_line_break);
                fw.append(csvLine.toString());

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
            estimatedLine.append(ed.getEstimatedUserWatt()).append(csv_column_break);
            estimatedLine.append(ed.getEstimatedMotorWatt()).append(csv_column_break);
            estimatedLine.append(ed.getEstimatedUserPercentage()).append(csv_column_break);
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
            cadenceLine.append(cd.getCalculatedCadence()).append(csv_column_break).append(cd.getCumulativeResolutions()).append(csv_column_break);
            return cadenceLine.toString();

        } else {
            cadenceLine.append(unknown_item + csv_column_break + unknown_item + csv_column_break);
            return cadenceLine.toString();

        }
    }

    private String csvSpeedSensor(AntPlusSpeedSensorData sd) {
        StringBuilder speedLine = new StringBuilder();

        if (sd != null) {
            speedLine.append(sd.getSpeedInMeterPerSecond()).append(csv_column_break).append(sd.getCalcAccumulatedDistanceInMeters()).append(csv_column_break);
            return speedLine.toString();
        } else {
            speedLine.append(unknown_item + csv_column_break + unknown_item + csv_column_break);
            return speedLine.toString();
        }
    }

    private String csvLocation(FirebaseLocation l){
        StringBuilder locationLine = new StringBuilder();
        if (l != null) {
            locationLine.append(l.getAccuracy())
                    .append(csv_column_break)
                    .append(l.getAltitude())
                    .append(csv_column_break)
                    .append(l.getBearing())
                    .append(csv_column_break)
                    .append(l.getElapsedRealTimeNanos())
                    .append(csv_column_break)
                    .append(l.getLatitude())
                    .append(csv_column_break)
                    .append(l.getLongitude())
                    .append(csv_column_break)
                    .append(l.getProvider())
                    .append(csv_column_break)
                    .append(l.getSpeed())
                    .append(csv_column_break)
                    .append(l.getTime())
                    .append(csv_column_break);
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
