package nl.easthome.ebikereader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Adapters.RideHistoryAdapter;
import nl.easthome.ebikereader.Objects.RideRecording;

public class RideHistoryActivity extends BaseActivityWithMenu implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.ride_history_listView) ListView mListView;
    @BindView(R.id.ride_refresh) SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ride_history, R.id.nav_history);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_ridehistory));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void gatherData() {
        AsyncTask<Void, Void, ArrayList<RideRecording>> task = new AsyncTask<Void, Void, ArrayList<RideRecording>>() {
            @Override
            protected ArrayList<RideRecording> doInBackground(Void... voids) {
                //TODO HIER BEN IK GEBLEVEN
                return null;
            }
        };

        try {
            ArrayList<RideRecording> rideRecordings = task.execute().get();
            mListView.setAdapter(new RideHistoryAdapter(this, ));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRefresh() {
        gatherData();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
