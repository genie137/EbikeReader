package nl.easthome.ebikereader.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.antpluslibary.AntPlusSensorScanner;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.Helpers.Constants;
import nl.easthome.ebikereader.R;

/**
 * Activity which handles the pairing and unpairing of ant+ sensors
 */
public class AntSensorActivity extends BaseActivityWithMenu {
    AntPlusSensorScanner mAntPlusSensorScanner;
    @BindView(R.id.ant_device_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.ant_device_listView) ListView mListView;
    @BindView(R.id.ant_sensor_add_layout)
    ConstraintLayout mConstraintLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ant_sensor, R.id.nav_ant_sensors);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_antsensor));
        mAntPlusSensorScanner = new AntPlusSensorScanner(this, mListView, Constants.getBikeDeviceTypeSet());

    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Snackbar.make(mConstraintLayout, R.string.snackbar_no_bluetooth_adapter, Snackbar.LENGTH_LONG).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                boolean goodStart = mAntPlusSensorScanner.startFindDevices();
                if (!goodStart) {
                    mProgressBar.setIndeterminate(false);
                }
            } else {
                Snackbar.make(mConstraintLayout, R.string.snackbar_bluetooth_is_disabled, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_button_fix_this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                    }
                }).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mAntPlusSensorScanner.stopFindDevices();
        super.onDestroy();
    }

}
