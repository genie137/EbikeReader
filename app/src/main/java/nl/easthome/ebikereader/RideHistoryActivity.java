package nl.easthome.ebikereader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Adapters.RideHistoryAdapter;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Objects.RideRecording;

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
    }

    private void gatherData() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                FirebaseSaver.getUserRides(FirebaseAuth.getInstance().getUid(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot rideSnapshot: dataSnapshot.getChildren()){
                            System.out.println(rideSnapshot.getValue().toString());

                            FirebaseSaver.getRideDetail(rideSnapshot.getValue().toString(), new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    RideRecording rideRecording = dataSnapshot.getValue(RideRecording.class);
                                    rideHistoryAdapter.add(rideRecording);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return null;
            }
        };

        try {
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
}
