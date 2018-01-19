package nl.easthome.ebikereader.Activities;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Adapters.RideHistoryAdapter;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
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
                Intent intent = new Intent(
                        RideHistoryActivity.this,
                        RideHistoryDetailsActivity.class)
                        .putExtra(RideHistoryDetailsActivity.intentExtraRideId, ride.getRideId())
                        .putExtra(RideHistoryDetailsActivity.intentExtraRideStart, ride.getRideStart());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        gatherData();
    }

    private void gatherData() {
        try {
            AsyncTask<Void,Void,Void> task = new GatherDataAsyncTask();
            rideHistoryAdapter.clear();
            task.execute().get();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        gatherData();
    }

    class GatherDataAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseSaver.getUserRides(FirebaseAuth.getInstance().getUid(), new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                        FirebaseSaver.getRideRecording(rideSnapshot.getValue().toString(), new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RideRecording rideRecording = dataSnapshot.getValue(RideRecording.class);
                                rideHistoryAdapter.add(rideRecording);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                               Toast toast = new Toast(RideHistoryActivity.this);
                               toast.setText("ERROR: " + databaseError.getMessage());
                               toast.show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast toast = new Toast(RideHistoryActivity.this);
                    toast.setText("ERROR: " + databaseError.getMessage());
                    toast.show();
                }
            });
            return null;
        }
    }
}



