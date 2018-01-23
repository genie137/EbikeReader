package nl.easthome.antpluslibary.Implementations;
import android.app.Activity;
import android.util.Log;

import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Adapters.AntPlusDeviceListViewAdapter;
import nl.easthome.antpluslibary.Enums.AntAddType;
import nl.easthome.antpluslibary.Objects.AntPlusFoundSensor;

/**
 * Implementation of the MultiDeviceSearch.SearchCallbacks (MDS for short) interface of the AntPluginLib
 */
public class MdsSearchCallback implements MultiDeviceSearch.SearchCallbacks {
    private static final String LOGTAG = "ANT+MDS_SEARCH_CALLBACK";
    private Activity mActivity;
    private ArrayList<AntPlusFoundSensor> mSensors;
    private AntPlusDeviceListViewAdapter mListViewAdapter;

    /**
     * Constructor
     *
     * @param activity The activity in which the MDS was created.
     * @param sensors  ArrayList containing the found sensors.
     * @param adapter  ListViewAdapter for the UI to display the found sensors.
     */
    public MdsSearchCallback(Activity activity, ArrayList<AntPlusFoundSensor> sensors, AntPlusDeviceListViewAdapter adapter) {
        mActivity = activity;
        mSensors = sensors;
        mListViewAdapter = adapter;
    }

    /**
     * Method is called when the MDS was started.
     * @param rssiSupport Enum containing information about RSSI (ReceivedSignalStrengthIndication).
     */
    @Override
    public void onSearchStarted(MultiDeviceSearch.RssiSupport rssiSupport) {
        Log.d(LOGTAG, "MDS Search Started --> " + rssiSupport.toString());
    }

    /**
     * Method gets called when a new device has been found by the MDS.
     * @param multiDeviceSearchResult Object that contains information about the found device.
     */
    @Override
    public void onDeviceFound(MultiDeviceSearchResult multiDeviceSearchResult) {
        Log.d(LOGTAG, "MDS Device Found --> " + multiDeviceSearchResult.toString());
        boolean itemAlreadyInList = false;

        //Check if the item found was previously added and saved.
        for (AntPlusFoundSensor sensor : mSensors) {
            if (multiDeviceSearchResult.getAntDeviceNumber() == sensor.getDeviceNumber()) {
                sensor.setAntAddType(AntAddType.EXISTING_AND_FOUND);
                itemAlreadyInList = true;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        if (!itemAlreadyInList) {
            final AntPlusFoundSensor foundSensor = new AntPlusFoundSensor(multiDeviceSearchResult, AntAddType.NEW);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListViewAdapter.add(foundSensor);
                }
            });
        }
    }

    /**
     * Method gets called when the MDS stops searching.
     * @param requestAccessResult Object containing information about why it stopped?
     */
    @Override
    public void onSearchStopped(RequestAccessResult requestAccessResult) {
        Log.d(LOGTAG, "MDS Search Stopped --> " + requestAccessResult.toString());

    }
}
