package nl.easthome.ebikereader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Adapters.RideHistoryAdapter;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.Helpers.FirebaseExecutorEventListener;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;

import static java.lang.Thread.sleep;

public class RideHistoryActivity extends BaseActivityWithMenu implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<RideRecording> mRides;
    RideHistoryAdapter mRideHistoryAdapter;
    @BindView(R.id.ride_history_listView) ListView mListView;
    @BindView(R.id.ride_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ride_history_constraint_layout)
    ConstraintLayout mConstraintLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ride_history, R.id.nav_history);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_ridehistory));
        mRides = new ArrayList<>();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRideHistoryAdapter = new RideHistoryAdapter(this, mRides);
        mListView.setAdapter(mRideHistoryAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RideRecording ride = mRideHistoryAdapter.getItem(position);
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

        toggleRefreshing();
        onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        mRideHistoryAdapter.clear();
        FirebaseSaver.getUserRides(FirebaseAuth.getInstance().getUid(), new FirebaseExecutorEventListener(Executors.newSingleThreadExecutor()) {
            @Override
            protected void onDataChangeExecutor(DataSnapshot dataSnapshot) {
                final boolean[] isDone = {false};
                final int[] amountOfRides = {0};
                final long rideCount = dataSnapshot.getChildrenCount();
                final int[] loopIndex = {1};

                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    if (rideSnapshot.getValue() != null) {
                        FirebaseSaver.getRideRecording(rideSnapshot.getValue().toString(), new FirebaseExecutorEventListener(Executors.newSingleThreadExecutor()) {
                            @Override
                            protected void onDataChangeExecutor(DataSnapshot dataSnapshot) {
                                final RideRecording rideRecording = dataSnapshot.getValue(RideRecording.class);
                                System.out.println(rideRecording);
                                if (rideRecording != null) {
                                    if (rideRecording.getRideEnd() != 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mRideHistoryAdapter.add(rideRecording);
                                            }
                                        });
                                        amountOfRides[0]++;
                                    }
                                }
                                loopIndex[0]++;
                                if (loopIndex[0] == rideCount) {
                                    isDone[0] = true;
                                }
                            }

                            @Override
                            protected void onCancelledExecutor(DatabaseError databaseError) {
                                Toast.makeText(RideHistoryActivity.this, getString(R.string.toast_error_preamp) + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                }

                //Makes the getUserRides thread wait for the subthreads with getRideRecording to finish.
                while (!isDone[0]) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (amountOfRides[0] > 0) {
                    sortList();
                    toggleRefreshing();
                } else {
                    toggleRefreshing();
                    showNoRidesRecordedSnackbar();
                }
            }

            @Override
            protected void onCancelledExecutor(DatabaseError databaseError) {
                Toast.makeText(RideHistoryActivity.this, getString(R.string.toast_error_preamp) + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void toggleRefreshing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(true);
                }

            }
        });
    }

    /**
     * Sorts the list while looking at the timestamp of the ride start.
     * Returns -1 if o1 is older (less) then o2
     * Returns 0 if o1 is as old as o2
     * Returns 1 if o1 newer then o2
     */
    private void sortList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRideHistoryAdapter.sort(new Comparator<RideRecording>() {
                    @Override
                    public int compare(RideRecording o1, RideRecording o2) {
                        if (o1.getRideStart() < o2.getRideStart()) {
                            return -1;
                        } else if (o1.getRideStart() > o2.getRideStart()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
        });

    }

    private void showNoRidesRecordedSnackbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mConstraintLayout, "You don't have any rides recorded.\nGo to the dashboard and record one!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}



