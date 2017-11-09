package nl.easthome.antpluslibary;
import android.content.Context;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

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
            sharedPreferences.edit().putInt(deviceType.toString(), 0).apply();
            return true;
        }
    }

    public AntPlusSensorDeviceSaveResult saveSensorForType(DeviceType deviceType, int antDeviceNumber){
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

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

    }

    public int getDeviceIdForType(DeviceType type){
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

        return sharedPreferences.getInt(type.toString(), 0);
    }


    public enum AntPlusSensorDeviceSaveResult{
        NEW_SENSOR_SAVED,
        REPLACED_OLD_SENSOR,
        ERROR
    }

}
