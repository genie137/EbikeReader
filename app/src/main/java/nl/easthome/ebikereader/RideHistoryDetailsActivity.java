package nl.easthome.ebikereader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Objects.EstimatedPowerData;
import nl.easthome.ebikereader.Objects.FirebaseLocation;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;

public class RideHistoryDetailsActivity extends AppCompatActivity {
    private String mRideID;
    private long mRideStart;
    private RideRecording mRideRecording;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.ride_history_detail_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.ride_history_id) TextView mRideHistoryId;
    @OnClick(R.id.exportFab) public void onExportFabButtonPress(){exportRideDetails();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRideID = getIntent().getStringExtra("RIDEID");
        mRideStart = getIntent().getLongExtra("RIDESTART", 0L);
        setTitle("Ride: " + Constants.convertTimestampToDateTime(mRideStart));
        getRideRecording();
    }

    private void getRideRecording() {
        mProgressDialog = ProgressDialog.show(this, "Loading Ride.", "This wil take a minute...", true, false);
        FirebaseSaver.getRideRecording(mRideID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRideRecording = dataSnapshot.getValue(RideRecording.class);
                mRideHistoryId.setText(mRideRecording.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void exportRideDetails(){
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                        doOnExport();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Snackbar.make(mCoordinatorLayout, "If you give no permission, we cannot generate a file. \nChange this in settings.", Snackbar.LENGTH_LONG);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(mCoordinatorLayout, "If you give no permission, we cannot generate a file.", Snackbar.LENGTH_LONG).setAction("FIX THIS", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                token.continuePermissionRequest();
                            }
                        });
                    }
                });
            }
        }).check();
    }

    private void doOnExport() {
        mProgressDialog = ProgressDialog.show(this, "Generating CSV file.", "This wil take a minute...", true, false);


        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                // Step 1 - creating an export location
                File folder =  new File(Environment.getExternalStorageDirectory() + "/EbikeReader");
                boolean doesFolderExists = folder.exists();
                if (!doesFolderExists){
                    folder.mkdir();
                }
                Log.d("CSVEXPORT", "Export Location at " + folder.getAbsolutePath() + " - Existed: " + doesFolderExists);

                // Step 2 - creating file an create column headers
                final String filename = folder.toString() + "/Ride" + Constants.convertTimestampToSimpleDateTime(mRideRecording.getRideStart()) + ".csv";
                Log.d("CSVEXPORT", "Filename is " + filename);
                try {

                    FileWriter fw = new FileWriter(filename);

                    fw.append("UNIXtimestamp,");
                    fw.append("gps_accuracy,gps_altitude,gps_bearing,gps_elapsedrealtimenanos,gps_latitude,gps_longitude,gps_provider,gps_speed,gps_time,");
                    fw.append("speedsens_speed_ms,speedsens_distance_meters,");
                    fw.append("cadancesens_calculated,cadancesens_cumulative_resolutions,");
                    fw.append("powersens_watt,");
                    fw.append("heartsens_bpm,");
                    fw.append("est_user_watt,est_motor_watt,est_user_percentage,est_motor_percentage");
                    fw.append("\n");

                    //Step 3 -  Add Data
                    System.out.println(mRideRecording.getRideMeasurements().toString());

                    for (Map.Entry<String, RideMeasurement> recording: mRideRecording.getRideMeasurements().entrySet()) {

                        String csvLine = "";
                        RideMeasurement recordingValue =  recording.getValue();
                        String recordingKey = recording.getKey();

                        csvLine += recordingKey + ",";

                        if (recordingValue.getLocation() != null){
                            FirebaseLocation l = recordingValue.getLocation();
                            csvLine += l.getAccuracy() + ","
                                    + l.getAltitude() + ","
                                    + l.getBearing() + ","
                                    + l.getElapsedRealTimeNanos() + ","
                                    + l.getLatitude() + ","
                                    + l.getLongitude() + ","
                                    + l.getProvider() + ","
                                    + l.getSpeed() + ","
                                    + l.getTime() + ",";

                        }
                        else {
                            csvLine += "x,x,x,x,x,x,x,x,x,";
                        }

                        if (recordingValue.getSpeedSensorData() != null){
                            AntPlusSpeedSensorData sd = recordingValue.getSpeedSensorData();

                            csvLine += sd.getSpeedInMeterPerSecond() + ","
                                    + sd.getCalcAccumulatedDistanceInMeters() + ",";
                        }
                        else {
                            csvLine += "x,x,";
                        }

                        if (recordingValue.getCadenceSensorData() != null) {
                            AntPlusCadenceSensorData cd = recordingValue.getCadenceSensorData();
                            csvLine += cd.getCalculatedCadence() + ","
                                    + cd.getCumulativeResolutions() + ",";
                        }
                        else {
                            csvLine += "x,x,";
                        }

                        if (recordingValue.getPowerSensorData() !=null){
                            AntPlusPowerSensorData pd = recordingValue.getPowerSensorData();
                            csvLine += pd.getCalculatedPower() + ",";
                        }
                        else {
                            csvLine += "x,";
                        }

                        if (recordingValue.getHeartSensorData() != null){
                            AntPlusHeartSensorData hd = recordingValue.getHeartSensorData();
                            //TODO implement
                            csvLine += "NOTIMPLEMENTED" + ",";
                        }
                        else {
                            csvLine += "x,";
                        }

                        if (recordingValue.getEstimatedPowerData() != null){
                            EstimatedPowerData ed = recordingValue.getEstimatedPowerData();
                            //TODO implement
                            csvLine += "NOTIMPLEMENTED" + "," + "NOTIMPLEMENTED" + "," +"NOTIMPLEMENTED" + "," + "NOTIMPLEMENTED";
                        }
                        else {
                            csvLine += "x,x,x,x\n";
                        }


                        fw.append(csvLine);
                        Log.d("CSVEXPORT", csvLine);
                    }
                    fw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Step 4 - Share the file.




                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
                        startActivity(Intent.createChooser(sharingIntent, "Send to: "));
                    }
                });
                return null;
            }
        };
        task.execute();
    }


}
