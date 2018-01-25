package nl.easthome.ebikereader.Activities;

import android.Manifest;
import android.app.Activity;
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

import java.util.Locale;
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
    private String mRideID;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRideID = getIntent().getStringExtra(intentExtraRideId);
        long mRideStart = getIntent().getLongExtra(intentExtraRideStart, 0L);
        setTitle(getString(R.string.ride_history_detail_title_preamp) + Constants.convertTimestampToDateTime(mRideStart));
        mRideRecordingMappingHelper = new RideRecordingMappingHelper((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMap), null);
        getRideRecording();

    }

    private void getRideRecording() {
        mProgressDialog = ProgressDialog.show(this, getString(R.string.progress_dialog_loading_ride_title), getString(R.string.progress_dialog_loading_ride_message), true, false);
        FirebaseSaver.getRideRecording(mRideID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRideRecording = dataSnapshot.getValue(RideRecording.class);
                if (mRideRecording != null) {
                    TreeMap<String, RideMeasurement> treeMap = new TreeMap<>(mRideRecording.getRideMeasurements());
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
                    //TODO CALCULATE ALL FIELDS IN UI


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
                toast.setText(getString(R.string.toast_error_preamp) + databaseError.getMessage());
                toast.show();
            }
        });
    }

    private void fillDurationField() {
        long duration = rideEnd - rideStart;
        long hours = TimeUnit.SECONDS.toHours(duration);
        long minutes = TimeUnit.SECONDS.toMinutes(duration - TimeUnit.HOURS.toMinutes(hours));
        long seconds = duration - TimeUnit.MINUTES.toSeconds(minutes);

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
        mProgressDialog = ProgressDialog.show(this, getString(R.string.progress_csv_export_title), getString(R.string.progress_csv_export_message), true, false);

        try {
            CSVExportHelper csvExportHelper = new CSVExportHelper(mRideRecording);
            String filename = csvExportHelper.export();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_intent_title)));
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mCoordinatorLayout, R.string.export_error, Snackbar.LENGTH_LONG).show();
        } finally {
            mProgressDialog.dismiss();
        }
    }
}
