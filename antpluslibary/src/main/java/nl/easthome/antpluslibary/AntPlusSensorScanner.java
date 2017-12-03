package nl.easthome.antpluslibary;

import android.app.Activity;
import android.widget.ListView;

import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import java.util.ArrayList;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Adapters.AntDeviceListViewAdapter;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Implementations.MdsSearchCallback;
import nl.easthome.antpluslibary.Objects.AntPlusFoundSensor;

public class AntPlusSensorScanner {
    private Activity mActivity;
    private ListView mListView;
    private MdsSearchCallback mMdsSearchCallbacks;
    private AntDeviceListViewAdapter mAntDeviceListViewAdapter;
    private EnumSet<DeviceType> mDeviceSet;
    private MultiDeviceSearch mMultiDeviceSearch;
    private AntPlusDeviceManager mDeviceManager;
    private ArrayList<AntPlusFoundSensor> mSensors;


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
            mAntDeviceListViewAdapter = new AntDeviceListViewAdapter(mActivity, mSensors);
            addPreviouslyConnectedDevices();
            mListView.setAdapter(mAntDeviceListViewAdapter);
            mMdsSearchCallbacks = new MdsSearchCallback(mActivity, mSensors, mAntDeviceListViewAdapter);
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
                        mAntDeviceListViewAdapter.add(new AntPlusFoundSensor(deviceType1, id, "Device: " + id, AntPlusFoundSensor.AntAddType.EXISTING_AND_MISSING));
                    }
                });
            } catch (NoDeviceConfiguredException e) {

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
