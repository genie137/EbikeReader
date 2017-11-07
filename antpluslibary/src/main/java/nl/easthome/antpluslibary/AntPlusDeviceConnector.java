package nl.easthome.antpluslibary;
import android.content.Context;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;

public class AntPlusDeviceConnector {
    Context mActivity;

    public AntPlusDeviceConnector(Context activity) {
        mActivity = activity;
    }

    public boolean removeSensorForType(DeviceType deviceType){
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);
        int deviceNum = sharedPreferences.getInt(deviceType.toString(), 0);

        if (deviceNum == 0){
            return false;
        }
        else {
            sharedPreferences.edit().putInt(deviceType.toString(), 0);
            return true;
        }
    }

    public AntPlusSensorDeviceSaveResult saveSensorForType(DeviceType deviceType, int antDeviceNumber){
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

        switch (deviceType){
            case BIKE_SPDCAD:
            case BIKE_SPD:
            case HEARTRATE:
            case BIKE_POWER :
                try {
                    int data = sharedPreferences.getInt(deviceType.toString(), 0);
                    if (data == 0) {
                        sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                        return AntPlusSensorDeviceSaveResult.NEW_SENSOR_SAVED;
                    }
                    else {
                        sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                        return AntPlusSensorDeviceSaveResult.REPLACED_OLD_SENSOR;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    return AntPlusSensorDeviceSaveResult.ERROR;
                }
            default:
                return AntPlusSensorDeviceSaveResult.NOT_SUPPORTED_SENSOR;
        }
    }

    public AntPlusSensorDeviceState getDeviceState(MultiDeviceSearch.MultiDeviceSearchResult mDevice) {
        int typeID = mActivity.getSharedPreferences(mActivity.getPackageName(),0).getInt(mDevice.getAntDeviceType().toString(), 0);

        if (typeID == mDevice.getAntDeviceNumber()){
            return AntPlusSensorDeviceState.CONNECTED;
        }
        else {
            return AntPlusSensorDeviceState.NEW;
        }

    }

    public enum AntPlusSensorDeviceSaveResult{
        NEW_SENSOR_SAVED,
        REPLACED_OLD_SENSOR,
        NOT_SUPPORTED_SENSOR,
        ERROR
    }

    public enum AntPlusSensorDeviceState{
        NEW,
        CONNECTED,
        MISSING
    }


}
