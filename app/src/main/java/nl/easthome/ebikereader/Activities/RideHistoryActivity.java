package nl.easthome.ebikereader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Adapters.RideHistoryAdapter;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.Helpers.FirebaseExecutorEventListener;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;

public class RideHistoryActivity extends BaseActivityWithMenu implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<RideRecording> mRides;
    RideHistoryAdapter rideHistoryAdapter;
    @BindView(R.id.ride_history_listView) ListView mListView;
    @BindView(R.id.ride_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ride_history, R.id.nav_history);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_ridehistory));
        mRides = new ArrayList<>();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rideHistoryAdapter = new RideHistoryAdapter(this, mRides);
        mListView.setAdapter(rideHistoryAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RideRecording ride = rideHistoryAdapter.getItem(position);
                Intent intent = null;
                if (ride != null) {
                    intent = new Intent(
                            RideHistoryActivity.this,
                            RideHistoryDetailsActivity.class)
                            .putExtra(RideHistoryDetailsActivity.intentExtraRideId, ride.getRideId())
                            .putExtra(RideHistoryDetailsActivity.intentExtraRideStart, ride.getRideStart());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        FirebaseSaver.getUserDetails(FirebaseAuth.getInstance().getUid(), new FirebaseExecutorEventListener(Executors.newSingleThreadExecutor()) {
            @Override
            protected void onDataChangeExecutor(DataSnapshot dataSnapshot) {
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    if (rideSnapshot.getValue() != null) {
                        FirebaseSaver.getRideRecording(rideSnapshot.getValue().toString(), new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RideRecording rideRecording = dataSnapshot.getValue(RideRecording.class);
                                if (rideRecording != null) {
                                    //TODO check if ended
                                        rideHistoryAdapter.add(rideRecording);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(RideHistoryActivity.this, getString(R.string.toast_error_preamp) + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                RideHistoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            protected void onCancelledExecutor(DatabaseError databaseError) {
                Toast.makeText(RideHistoryActivity.this, getString(R.string.toast_error_preamp) + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}



