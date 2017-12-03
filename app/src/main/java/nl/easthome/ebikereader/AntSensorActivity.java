package nl.easthome.ebikereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.antpluslibary.AntPlusSensorScanner;

public class AntSensorActivity extends AppCompatActivity {
    AntPlusSensorScanner mAntPlusSensorScanner;
    @BindView(R.id.ant_device_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.ant_device_listView) ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_sensor);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.activity_title_antsensor));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAntPlusSensorScanner = new AntPlusSensorScanner(this, mListView, AntPlusSensorScanner.getBikeDeviceTypeSet());
        boolean goodStart = mAntPlusSensorScanner.startFindDevices();
        if (!goodStart) {
            mProgressBar.setIndeterminate(false);
        }
    }

    @Override
    protected void onDestroy() {
        mAntPlusSensorScanner.stopFindDevices();
        super.onDestroy();
    }
}
