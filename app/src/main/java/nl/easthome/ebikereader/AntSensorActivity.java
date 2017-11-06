package nl.easthome.ebikereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.antpluslibary.AntPlusDeviceScanner;

public class AntSensorActivity extends AppCompatActivity {
    AntPlusDeviceScanner mAntPlusDeviceScanner;
    @BindView(R.id.ant_device_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.ant_device_listView) ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_sensor);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.activity_title_antsensor));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAntPlusDeviceScanner = new AntPlusDeviceScanner(this, mListView);
        boolean goodStart = mAntPlusDeviceScanner.startFindDevices();
        if (!goodStart) {
            mProgressBar.setIndeterminate(false);
        }
    }


    @Override
    protected void onDestroy() {
        mAntPlusDeviceScanner.stopFindDevices();
        super.onDestroy();
    }
}
