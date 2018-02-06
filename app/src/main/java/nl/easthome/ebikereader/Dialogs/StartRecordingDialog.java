package nl.easthome.ebikereader.Dialogs;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Arrays;

import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.ebikereader.Activities.DashboardActivity;
import nl.easthome.ebikereader.Exceptions.LocationIsDisabledException;
import nl.easthome.ebikereader.Exceptions.NoBluetoothEnabledException;
import nl.easthome.ebikereader.Exceptions.NoLocationPermissionGivenException;
import nl.easthome.ebikereader.Implementations.RideRecordingGuiUpdater;
import nl.easthome.ebikereader.R;

public class StartRecordingDialog extends MaterialDialog.Builder implements MaterialDialog.ListCallbackMultiChoice, MaterialDialog.SingleButtonCallback {

    DashboardActivity mDashboardActivity;

    public StartRecordingDialog(DashboardActivity dashboardActivity) {
        super(dashboardActivity);
        mDashboardActivity = dashboardActivity;
        title(R.string.dashboard_sensor_choose_dialog);
        items(R.array.sensors);
        itemsCallbackMultiChoice(null, this);
        positiveText(R.string.dashboard_recording_start_dialog_confirm);
        negativeText(R.string.dashboard_recording_start_dialog_deny);
        onNegative(this);
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
        String selected = Arrays.toString(text);
        String[] sensorStrings = mDashboardActivity.getResources().getStringArray(R.array.sensors);
        try {
            //Check if bluetooth is enabled first but only if a sensor is being recorded.
            if (selected.contains(sensorStrings[0]) || selected.contains(sensorStrings[1]) || selected.contains(sensorStrings[2]) || selected.contains(sensorStrings[3])) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    throw new NoBluetoothEnabledException();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        throw new NoBluetoothEnabledException();
                    }
                }
            }

            mDashboardActivity.getRideRecordingService().startRecording(
                    mDashboardActivity,
                    new RideRecordingGuiUpdater(mDashboardActivity),
                    selected.contains(sensorStrings[0]),
                    selected.contains(sensorStrings[1]),
                    selected.contains(sensorStrings[2]),
                    selected.contains(sensorStrings[3]));

            mDashboardActivity.getFloatingActionButton().setImageResource(R.drawable.ic_stop_white_36dp);
        } catch (SecurityException nie) {
            nie.printStackTrace();
        } catch (NoDeviceConfiguredException ndce) {
            mDashboardActivity.showNoDeviceConfiguredExceptionSnackbar();
            mDashboardActivity.getFloatingActionButton().setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
        } catch (LocationIsDisabledException lide) {
            mDashboardActivity.showLocationDisabledExceptionSnackbar();
            mDashboardActivity.getFloatingActionButton().setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
        } catch (NoLocationPermissionGivenException noLocationPermissionGiven) {
            mDashboardActivity.showLocationPermissionMissingExceptionSnackbar();
        } catch (NoBluetoothEnabledException nbee) {
            mDashboardActivity.showBluetoothIsDisabledException();
        }
        return true;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        dialog.dismiss();
    }
}
