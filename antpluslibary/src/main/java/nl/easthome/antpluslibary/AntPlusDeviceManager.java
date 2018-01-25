package nl.easthome.antpluslibary;
import android.app.Activity;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Enums.AntPlusSensorDeviceSaveResult;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;

/**
 * Class that handles the saving, retrieving and connecting of saved devices.
 */
@SuppressWarnings("unchecked")
public class AntPlusDeviceManager {
    private static final int PROXIMITY = 10;
    private static final String SP_PAGE_NAME = "ANT+DeviceManager";
    private final Activity mActivity;

    /**
     * Constructor
     *
     * @param activity The activity where the object was created.
     */
    public AntPlusDeviceManager(Activity activity) {
        mActivity = activity;
    }


    /**
     * Removes the saved sensor for the specified deviceType from the shared preferences.
     * @param deviceType The devicetype.
     * @return true if deleted, false if there was no sensor id saved.
     */
    public boolean removeSensorForType(DeviceType deviceType) {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(SP_PAGE_NAME, 0);
        int deviceNum = sharedPreferences.getInt(deviceType.toString(), 0);

        if (deviceNum == 0){
            return false;
        } else {
            sharedPreferences.edit().putInt(deviceType.toString(), 0).apply();
            return true;
        }
    }

    /**
     * Save an ant+ device number to the shared preferences.
     * @param deviceType The device type that needs to be saved.
     * @param antDeviceNumber The device id that needs to be saved
     * @return The result of saving.
     */
    public AntPlusSensorDeviceSaveResult saveSensorForType(DeviceType deviceType, int antDeviceNumber) {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(SP_PAGE_NAME, 0);

        try {
            int data = sharedPreferences.getInt(deviceType.toString(), 0);
            if (data == 0) {
                sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                return AntPlusSensorDeviceSaveResult.NEW_SENSOR_SAVED;
            } else {
                sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                return AntPlusSensorDeviceSaveResult.REPLACED_OLD_SENSOR;
            }
        } catch (Exception e){
            e.printStackTrace();
            return AntPlusSensorDeviceSaveResult.ERROR;
        }

    }

    /**
     * Returns the saved id from a specified device type.
     * @param type The device type.
     * @return The id for the device.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public int getDeviceIdForType(DeviceType type) throws NoDeviceConfiguredException {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(SP_PAGE_NAME, 0);

        int result = sharedPreferences.getInt(type.toString(), 0);

        if (result == 0){
            throw new NoDeviceConfiguredException(type);
        }

        return result;
    }
}
