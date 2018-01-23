package nl.easthome.antpluslibary;

import android.app.Activity;
import android.widget.ListView;

import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import java.util.ArrayList;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Adapters.AntPlusDeviceListViewAdapter;
import nl.easthome.antpluslibary.Enums.AntAddType;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Implementations.MdsSearchCallback;
import nl.easthome.antpluslibary.Objects.AntPlusFoundSensor;

@SuppressWarnings("ALL")
public class AntPlusSensorScanner {
    private final Activity mActivity;
    private final ListView mListView;
    private final EnumSet<DeviceType> mDeviceSet;
    private final AntPlusDeviceManager mDeviceManager;
    private final ArrayList<AntPlusFoundSensor> mSensors;
    private AntPlusDeviceListViewAdapter mAntPlusDeviceListViewAdapter;
    private MultiDeviceSearch mMultiDeviceSearch;


    /**
     * Object that facilitates the scanning of Ant+ sensors.
     * @param activity The activity from which the scanning was started.
     * @param listView The ListView in which the results are to be displayed.
     * @param deviceTypes An EnumSet which contains all devices that need to be found by the scanner.
     */
    public AntPlusSensorScanner(Activity activity, ListView listView, EnumSet<DeviceType> deviceTypes) {
        mActivity = activity;
        mListView = listView;
        mDeviceManager = new AntPlusDeviceManager(mActivity);
        mDeviceSet = deviceTypes;
        mSensors = new ArrayList<>();

    }

    /**
     * Returns an empty EnumSet of the deviceTypes.
     * DeviceTypes need to be added with .add().
     * Easy method to help with the construction of the AntPlusSensorScanner.
     */
    public static EnumSet<DeviceType> getEmptyDeviceTypeSet() {
        return EnumSet.noneOf(DeviceType.class);
    }

    /**
     * Returns an EnumSet with all of the deviceTypes.
     * Easy method to help with the construction of the AntPlusSensorScanner.
     */
    public static EnumSet<DeviceType> getAllDeviceTypeSet() {
        return EnumSet.allOf(DeviceType.class);
    }


    public static EnumSet<DeviceType> getBikeDeviceTypeSet() {
        EnumSet<DeviceType> deviceTypes = getEmptyDeviceTypeSet();
        deviceTypes.add(DeviceType.BIKE_POWER);
        deviceTypes.add(DeviceType.BIKE_CADENCE);
        deviceTypes.add(DeviceType.BIKE_SPD);
        deviceTypes.add(DeviceType.HEARTRATE);
        return deviceTypes;
    }


    /**
     * Starts the search for nearby Ant+ sensors.
     * @return True if started without exceptions.
     */
    public boolean startFindDevices(){
        try{
            mAntPlusDeviceListViewAdapter = new AntPlusDeviceListViewAdapter(mActivity, mSensors);
            addPreviouslyConnectedDevices();
            mListView.setAdapter(mAntPlusDeviceListViewAdapter);
            MdsSearchCallback mMdsSearchCallbacks = new MdsSearchCallback(mActivity, mSensors, mAntPlusDeviceListViewAdapter);
            mMultiDeviceSearch = new MultiDeviceSearch(mActivity, mDeviceSet, mMdsSearchCallbacks, null);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds previously saved devices if they exist, to the array attached to the ListViewAdapter.
     * That is the same array as mSensors.
     */
    private void addPreviouslyConnectedDevices() {
        for(DeviceType deviceType: mDeviceSet) {
            final DeviceType deviceType1 = deviceType;
            final int id;
            try {
                id = mDeviceManager.getDeviceIdForType(deviceType);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAntPlusDeviceListViewAdapter.add(new AntPlusFoundSensor(deviceType1, id, mActivity.getString(R.string.ant_connected_device_preamp) + id, AntAddType.EXISTING_AND_MISSING));
                    }
                });
            } catch (NoDeviceConfiguredException ignored) {
            }
        }
    }

    /**
     * Stops the search for nearby Ant+ Sensors
     */
    public void stopFindDevices(){
        mMultiDeviceSearch.close();
    }

}
