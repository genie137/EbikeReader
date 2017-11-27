package nl.easthome.antpluslibary.Implementations;
import android.app.Activity;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Adapters.AntDeviceListViewAdapter;
import nl.easthome.antpluslibary.Objects.AntPlusFoundSensor;


public class MdsSearchCallback implements MultiDeviceSearch.SearchCallbacks {
    private Activity mActivity;
    private ArrayList<AntPlusFoundSensor> mSensors;
    private AntDeviceListViewAdapter mListViewAdapter;


    public MdsSearchCallback(Activity activity, ArrayList<AntPlusFoundSensor> sensors, AntDeviceListViewAdapter adapter) {
        mActivity = activity;
        mSensors = sensors;
        mListViewAdapter = adapter;
    }

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

        for (AntPlusFoundSensor sensor : mSensors) {
            if (multiDeviceSearchResult.getAntDeviceNumber() == sensor.getDeviceNumber()){
                sensor.setAntAddType(AntPlusFoundSensor.AntAddType.EXISTING_AND_FOUND);
                itemAlreadyInList = true;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        if (!itemAlreadyInList){
            final AntPlusFoundSensor foundSensor = new AntPlusFoundSensor(multiDeviceSearchResult, AntPlusFoundSensor.AntAddType.NEW);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListViewAdapter.add(foundSensor);
                }
            });
        }
    }

    @Override
    public void onSearchStopped(RequestAccessResult requestAccessResult) {

    }
}
