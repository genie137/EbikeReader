package nl.easthome.ebikereader.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Helpers.CSVExportHelper;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Implementations.RideRecordingMappingHelper;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;

public class RideHistoryDetailsActivity extends AppCompatActivity {
    public static final String intentExtraRideId = "RIDEID";
    public static final String intentExtraRideStart = "RIDESTART";
    @BindView(R.id.ride_history_detail_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.exportFab)
    FloatingActionButton mExportFab;
    @BindView(R.id.durationValue)
    TextView mDurationValue;
    private String mRideID;
    private long mRideStart;
    private RideRecording mRideRecording;
    private ProgressDialog mProgressDialog;
    private RideRecordingMappingHelper mRideRecordingMappingHelper;
    private int numberOfMeasurements;
    private long rideStart = Long.MAX_VALUE;
    private long rideEnd = Long.MIN_VALUE;

    @OnClick(R.id.exportFab) public void onExportFabButtonPress(){exportRideDetails();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRideID = getIntent().getStringExtra(intentExtraRideId);
        mRideStart = getIntent().getLongExtra(intentExtraRideStart, 0L);
        setTitle("Ride: " + Constants.convertTimestampToDateTime(mRideStart));
        mRideRecordingMappingHelper = new RideRecordingMappingHelper((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMap), null);
        getRideRecording();

    }

    private void getRideRecording() {
        mProgressDialog = ProgressDialog.show(this, "Loading Ride.", "This wil take a minute...", true, false);
        FirebaseSaver.getRideRecording(mRideID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRideRecording = dataSnapshot.getValue(RideRecording.class);
                if (mRideRecording != null) {
                    TreeMap<String, RideMeasurement> treeMap = new TreeMap(mRideRecording.getRideMeasurements());
                    numberOfMeasurements = treeMap.size();

                    for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
                        RideMeasurement recordingValue = measurementEntry.getValue();

                        if (recordingValue.getTimestamp() > rideEnd){
                            rideEnd =  recordingValue.getTimestamp();
                        }
                        if (recordingValue.getTimestamp() < rideStart) {
                            rideStart = recordingValue.getTimestamp();
                        }




                        mRideRecordingMappingHelper.addPointToMap(recordingValue.getLocation());

                    }

                    fillDurationField();
                } else {
                    mExportFab.setEnabled(false);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = new Toast(RideHistoryDetailsActivity.this);
                toast.setText("ERROR: " + databaseError.getMessage());
                toast.show();
            }
        });
    }

    private void fillDurationField() {
        long duration = rideEnd - rideStart;
        long hours = TimeUnit.SECONDS.toHours(duration);
        long minutes = TimeUnit.SECONDS.toMinutes(duration - TimeUnit.HOURS.toMinutes(hours));
        long seconds = duration - TimeUnit.MINUTES.toSeconds(minutes);

        final String formattedString = String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDurationValue.setText(formattedString);
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

        try {
            CSVExportHelper csvExportHelper = new CSVExportHelper(mRideRecording);
            String filename = csvExportHelper.export();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
            startActivity(Intent.createChooser(sharingIntent, "Send to: "));
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mCoordinatorLayout, "Something went wrong while exporting. Try again later.", Snackbar.LENGTH_LONG).show();
        } finally {
            mProgressDialog.dismiss();
        }
    }
}
