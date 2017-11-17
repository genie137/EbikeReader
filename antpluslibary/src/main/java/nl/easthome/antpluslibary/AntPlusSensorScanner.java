package nl.easthome.antpluslibary;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;

import java.util.ArrayList;
import java.util.EnumSet;

import nl.easthome.antpluslibary.Adapters.AntDeviceListViewAdapter;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Objects.AntPlusSensor;

public class AntPlusSensorScanner {
    private Activity mActivity;
    private ListView mListView;
    private AntDeviceListViewAdapter mAntDeviceListViewAdapter;
    private EnumSet<DeviceType> mDeviceSet = EnumSet.noneOf(DeviceType.class);
    private MdsSearchCallbacks mMdsSearchCallbacks;
    private MultiDeviceSearch mMultiDeviceSearch;
    private AntPlusDeviceConnector mDeviceConnector;
    private ArrayList<AntPlusSensor> mSensors;



    public AntPlusSensorScanner(Activity activity, ListView listView) {
        mActivity = activity;
        mListView = listView;
        mDeviceConnector = new AntPlusDeviceConnector(mActivity);
        mDeviceSet.add(DeviceType.BIKE_POWER);
        mDeviceSet.add(DeviceType.BIKE_CADENCE);
        mDeviceSet.add(DeviceType.BIKE_SPD);
        mDeviceSet.add(DeviceType.HEARTRATE);
        mSensors = new ArrayList<>();

    }

    public boolean startFindDevices(){
        try{
            mAntDeviceListViewAdapter = new AntDeviceListViewAdapter(mActivity, mSensors);
            addPreviouslyConnectedDevices();
            mListView.setAdapter(mAntDeviceListViewAdapter);
            mMdsSearchCallbacks = new MdsSearchCallbacks();
            mMultiDeviceSearch = new MultiDeviceSearch(mActivity, mDeviceSet, mMdsSearchCallbacks, null);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private void addPreviouslyConnectedDevices() {
        for(DeviceType deviceType: mDeviceSet) {
            final DeviceType deviceType1 = deviceType;
            final int id;
            try {
                id = mDeviceConnector.getDeviceIdForType(deviceType);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAntDeviceListViewAdapter.add(new AntPlusSensor(deviceType1, id, "Device: " + id, AntPlusSensor.AntAddType.EXISTING_AND_MISSING));
                    }
                });
            } catch (NoDeviceConfiguredException e) {

            }
        }
    }

    public void stopFindDevices(){
        mMultiDeviceSearch.close();
    }


    public class MdsSearchCallbacks implements MultiDeviceSearch.SearchCallbacks{
        @Override
        public void onSearchStarted(MultiDeviceSearch.RssiSupport rssiSupport) {
            if(rssiSupport == MultiDeviceSearch.RssiSupport.UNAVAILABLE)
            {
                Toast.makeText(mActivity, "Rssi information not available.", Toast.LENGTH_SHORT).show();
            } else if(rssiSupport == MultiDeviceSearch.RssiSupport.UNKNOWN_OLDSERVICE)
            {
                Toast.makeText(mActivity, "Rssi might be supported. Please upgrade the plugin service.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDeviceFound(com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult multiDeviceSearchResult) {
            boolean itemAlreadyInList = false;

            for (AntPlusSensor sensor: mSensors){
                if (multiDeviceSearchResult.getAntDeviceNumber() == sensor.getDeviceNumber()){
                    sensor.setAntAddType(AntPlusSensor.AntAddType.EXISTING_AND_FOUND);
                    itemAlreadyInList = true;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAntDeviceListViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            if (!itemAlreadyInList){
                final AntPlusSensor foundSensor = new AntPlusSensor(multiDeviceSearchResult, AntPlusSensor.AntAddType.NEW);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAntDeviceListViewAdapter.add(foundSensor);
                    }
                });
            }
        }

        @Override
        public void onSearchStopped(RequestAccessResult requestAccessResult) {

        }
    }
}
