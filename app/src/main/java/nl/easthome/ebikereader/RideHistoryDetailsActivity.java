package nl.easthome.ebikereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Helpers.SystemTime;
import nl.easthome.ebikereader.Objects.RideRecording;

public class RideHistoryDetailsActivity extends AppCompatActivity {
    private String mRideID;
    private RideRecording mRideRecording;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.ride_history_id) TextView mRideHistoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRideID = getIntent().getStringExtra("RIDEID");
        mRideRecording = (RideRecording) getIntent().getSerializableExtra("RIDEOBJECT");
        setTitle("Ride: " + SystemTime.convertTimestampToDateTime(mRideRecording.getRideStart()));
        mRideHistoryId.setText(mRideRecording.toString());
    }






}
