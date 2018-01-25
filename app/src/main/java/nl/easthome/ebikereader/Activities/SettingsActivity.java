package nl.easthome.ebikereader.Activities;

import android.os.Bundle;

import butterknife.ButterKnife;
import nl.easthome.antpluslibary.AntPlusSensorScanner;
import nl.easthome.ebikereader.Helpers.BaseActivityWithMenu;
import nl.easthome.ebikereader.R;

/**
 * Activity which handles the changing in settings.
 */
public class SettingsActivity extends BaseActivityWithMenu {
    AntPlusSensorScanner mAntPlusSensorScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ant_sensor, R.id.nav_ant_sensors);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_settings));

    }

    @Override
    protected void onDestroy() {

    }

}
