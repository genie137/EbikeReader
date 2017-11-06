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
import nl.easthome.antpluslibary.Objects.MultiDeviceSearchResultWithRSSI;

public class AntPlusDeviceScanner {
    private Activity mActivity;
    private ListView mListView;
    private AntDeviceListViewAdapter mAntDeviceListViewAdapter;
    private static EnumSet<DeviceType> mDeviceSet = EnumSet.allOf(DeviceType.class);
    private MdsSearchCallbacks mMdsSearchCallbacks;
    private MdsRssiCallback mMdsRssiCallback;
    private MultiDeviceSearch mMultiDeviceSearch;
    private ArrayList<MultiDeviceSearchResultWithRSSI> mDevices;



    public AntPlusDeviceScanner(Activity activity, ListView listView) {
        mActivity = activity;
        mListView = listView;
        mDevices = new ArrayList<>();
    }

    public boolean startFindDevices(){
        try{
            mAntDeviceListViewAdapter = new AntDeviceListViewAdapter(mActivity, mDevices);
            mListView.setAdapter(mAntDeviceListViewAdapter);
            mMdsSearchCallbacks = new MdsSearchCallbacks();
            mMdsRssiCallback = new MdsRssiCallback();
            mMultiDeviceSearch = new MultiDeviceSearch(mActivity, mDeviceSet, mMdsSearchCallbacks, mMdsRssiCallback);
            return true;
        }catch (Exception e) {
            return false;
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
            final MultiDeviceSearchResultWithRSSI result = new MultiDeviceSearchResultWithRSSI();
            result.mDevice = multiDeviceSearchResult;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAntDeviceListViewAdapter.add(result);
                }
            });
            System.out.println(result.mDevice.getAntDeviceType().toString());
        }

        @Override
        public void onSearchStopped(RequestAccessResult requestAccessResult) {

        }
    }

    public class MdsRssiCallback implements MultiDeviceSearch.RssiCallback {

        @Override
        public void onRssiUpdate(int resultID, int rssi) {
            System.out.println("RSSI: resultID= " + resultID + ", code= " + rssi);
        }
    }


}
