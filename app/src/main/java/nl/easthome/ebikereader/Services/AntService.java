package nl.easthome.ebikereader.Services;
import android.app.Activity;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.util.EnumSet;

public class AntService {
    private boolean mHasAntSupport;
    private boolean mHasAntDependencies;
    private Activity mActivity;
    private MultiDeviceSearch mMultiDeviceSearch;
    private EnumSet<DeviceType> mDevices;
    private MultiDeviceSearch.SearchCallbacks mDeviceSearchCallbacks;
    private MultiDeviceSearch.RssiCallback mRssiCallback;
    private AntPluginPcc.IPluginAccessResultReceiver mSimplePluginAccessResultReceiver;


    public AntService(Activity activity) {
        mActivity = activity;
        mDeviceSearchCallbacks = getMultiDeviceSearchCallbackObject();
        mSimplePluginAccessResultReceiver = getSimplePluginAccessResultReceiverObject();
        mRssiCallback = getRssiCallbackObject();
        mDevices = EnumSet.noneOf(DeviceType.class);
        mDevices.add(DeviceType.BIKE_SPD);
        mDevices.add(DeviceType.BIKE_SPDCAD);
        mDevices.add(DeviceType.BIKE_POWER);
        mDevices.add(DeviceType.HEARTRATE);
    }

    private MultiDeviceSearch.SearchCallbacks getMultiDeviceSearchCallbackObject() {
        return new MultiDeviceSearch.SearchCallbacks() {
            @Override
            public void onSearchStarted(MultiDeviceSearch.RssiSupport rssiSupport) {

            }

            @Override
            public void onDeviceFound(com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult multiDeviceSearchResult) {

            }

            @Override
            public void onSearchStopped(RequestAccessResult requestAccessResult) {

            }
        };
    }

    private MultiDeviceSearch.RssiCallback getRssiCallbackObject() {
        return new MultiDeviceSearch.RssiCallback() {
            @Override
            public void onRssiUpdate(int i, int i1) {

            }
        };
    }

    private AntPluginPcc.IPluginAccessResultReceiver getSimplePluginAccessResultReceiverObject() {
        return new AntPluginPcc.IPluginAccessResultReceiver() {
            @Override
            public void onResultReceived(AntPluginPcc result, RequestAccessResult resultCode, DeviceState initialDeviceState) {
                if (resultCode == RequestAccessResult.ADAPTER_NOT_DETECTED) {
                    mHasAntSupport = false;
                    mHasAntDependencies = false;
                }
                else if(resultCode == RequestAccessResult.DEPENDENCY_NOT_INSTALLED) {
                    mHasAntSupport = true;
                    mHasAntDependencies = false;
                    //TODO implement link to store
                }
                else {
                    mHasAntDependencies = true;
                    mHasAntSupport = true;
                }
            }
        };
    }



    public boolean hasDeviceAntSupport(){
        PccReleaseHandle pccReleaseHandle = AntPlusBikeCadencePcc.requestAccess(mActivity, mActivity, mSimplePluginAccessResultReceiver, null);


        return false;
    }

    public void searchDevice() {
        mMultiDeviceSearch = new MultiDeviceSearch(mActivity, mDevices, mDeviceSearchCallbacks, mRssiCallback);
    }
}
