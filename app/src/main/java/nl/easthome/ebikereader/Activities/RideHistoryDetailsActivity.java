package nl.easthome.ebikereader.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Helpers.CSVExportHelper;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.Helpers.FirebaseExecutorEventListener;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Implementations.RideRecordingMappingHelper;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;

@SuppressWarnings("SpellCheckingInspection")
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
    @BindView(R.id.detailMap)
    Fragment mMapFragment;
    private RideRecordingMappingHelper mRideRecordingMappingHelper;
    private RideRecording mRideRecording;
    private MaterialDialog mProgressDialog;
    private int numberOfMeasurements;
    private long rideStart = Long.MAX_VALUE;
    private long rideEnd = Long.MIN_VALUE;
    private String mRideID;
    private Executor executor;


    @OnClick(R.id.exportFab) public void onExportFabButtonPress(){exportRideDetails();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRideID = getIntent().getStringExtra(intentExtraRideId);
        long mRideStart = getIntent().getLongExtra(intentExtraRideStart, 0L);
        setTitle(getString(R.string.ride_history_detail_title_preamp) + Constants.convertTimestampToDateTime(mRideStart));
        mRideRecordingMappingHelper = new RideRecordingMappingHelper(this, (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMap), null);
        executor = Executors.newSingleThreadExecutor();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getRideRecording(mRideID);
    }

    private void getRideRecording(String rideID) {
        showOrHideDialog("Loading Ride", "This may take some time depending on the ride duration.");
        FirebaseSaver.getRideRecording(rideID, new FirebaseExecutorEventListener(executor) {

            @Override
            protected void onDataChangeExecutor(DataSnapshot dataSnapshot) {
                mRideRecording = dataSnapshot.getValue(RideRecording.class);
                if (mRideRecording != null) {
                    TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideRecording.getRideMeasurements());
                    numberOfMeasurements = treeMap.size();

                    for (Map.Entry<String, RideMeasurement> measurementEntry : treeMap.entrySet()) {
                        Log.d("RIDELOADING", "Processing Measurement");
                        final RideMeasurement recordingValue = measurementEntry.getValue();

                        if (recordingValue.getTimestamp() > rideEnd){
                            rideEnd =  recordingValue.getTimestamp();
                        }
                        if (recordingValue.getTimestamp() < rideStart) {
                            rideStart = recordingValue.getTimestamp();
                        }



                    }

                    fillDurationField(rideStart, rideEnd);
                    //TODO CALCULATE ALL FIELDS IN UI

                    //TODO SETLISTWITHPOINTS
                    RideHistoryDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRideRecordingMappingHelper.addPointToMap();

                        }
                    });

                } else {
                    mExportFab.setEnabled(false);
                }

                showOrHideDialog(null, null);
            }

            @Override
            protected void onCancelledExecutor(DatabaseError databaseError) {
                Toast.makeText(RideHistoryDetailsActivity.this, getString(R.string.toast_error_preamp) + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    protected void onPause() {
        showOrHideDialog(null, null);
        super.onPause();
    }

    public void fillDurationField(long rideStart, long rideEnd) {
        System.out.println(rideStart);
        System.out.println(rideEnd);
        long duration = rideEnd - rideStart;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);

        final String formattedString = String.format(Locale.ENGLISH, getString(R.string.detail_duration_format), hours, minutes, seconds);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDurationValue.setText(formattedString);
            }
        });
    }


    private void exportRideDetails(){
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dexter.withActivity(activity).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        doOnExport();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Snackbar.make(mCoordinatorLayout, R.string.export_permission_denied, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        Snackbar.make(mCoordinatorLayout, R.string.export_permission_rationale, Snackbar.LENGTH_LONG).setAction(getString(R.string.snackbar_button_fix_this), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                token.continuePermissionRequest();
                            }
                        }).show();
                    }
                }).check();
            }
        });
    }

    private void doOnExport() {

        try {
            CSVExportHelper csvExportHelper = new CSVExportHelper(this, mRideRecording);
            String filename = csvExportHelper.export();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_intent_title)));
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mCoordinatorLayout, R.string.export_error, Snackbar.LENGTH_LONG).show();
        }
    }

    public void showOrHideDialog(final String title, final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                } else if (title != null && content != null) {
                    mProgressDialog = new MaterialDialog.Builder(RideHistoryDetailsActivity.this)
                            .title(title)
                            .content(content)
                            .progress(true, 0)
                            .cancelable(false)
                            .build();
                    mProgressDialog.show();
                }
            }
        });
    }
}
