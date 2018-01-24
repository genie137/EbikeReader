package nl.easthome.ebikereader.Activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.antpluslibary.AntPlusSensorScanner;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.R;

/**
 * Activity which handles the pairing and unpairing of ant+ sensors
 */
public class AntSensorActivity extends BaseActivityWithMenu {
    AntPlusSensorScanner mAntPlusSensorScanner;
    @BindView(R.id.ant_device_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.ant_device_listView) ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ant_sensor, R.id.nav_ant_sensors);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_antsensor));
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
