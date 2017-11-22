package nl.easthome.antpluslibary;
import android.app.Activity;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.antpluslibary.Objects.AntPlusSensorList;
import nl.easthome.antpluslibary.Sensors.AntPlusCadenceSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusHeartSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusPowerSensor;
import nl.easthome.antpluslibary.Sensors.AntPlusSpeedSensor;

public class AntPlusDeviceManager {
    private static final int PROXIMITY = 10;
    private static AntPlusSensorList mAntPlusSensorList = new AntPlusSensorList();
    private Activity mActivity;

    public AntPlusDeviceManager(Activity activity) {
        mActivity = activity;
    }

    public static AntPlusSensorList getAntPlusSensorList() {
        return mAntPlusSensorList;
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

    public int getDeviceIdForType(DeviceType type) throws NoDeviceConfiguredException {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

        int result = sharedPreferences.getInt(type.toString(), 0);

        if (result == 0){
            throw new NoDeviceConfiguredException(type);
        }

        return result;
    }

    public AntPlusSensorList initConnectionToDeviceType(DeviceType type) throws NotImplementedException, NoDeviceConfiguredException {
        switch (type){
            case BIKE_POWER:
                AntPlusPowerSensor antPlusPowerSensor = new AntPlusPowerSensor();
                antPlusPowerSensor.setReleaseHandle(AntPlusBikePowerPcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, antPlusPowerSensor.getSensorResultReceiver(), antPlusPowerSensor.getSensorStateChangeReceiver()));
                mAntPlusSensorList.setAntPlusPowerSensor(antPlusPowerSensor);
                break;
            case HEARTRATE:
                AntPlusHeartSensor antPlusHeartSensor = new AntPlusHeartSensor();
                antPlusHeartSensor.setReleaseHandle(AntPlusHeartRatePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, antPlusHeartSensor.getSensorResultReceiver(), antPlusHeartSensor.getSensorStateChangeReceiver()));
                mAntPlusSensorList.setAntPlusHeartSensor(antPlusHeartSensor);
                break;
            case BIKE_CADENCE:
                AntPlusCadenceSensor antPlusCadenceSensor = new AntPlusCadenceSensor();
                antPlusCadenceSensor.setReleaseHandle(AntPlusBikeCadencePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, false, antPlusCadenceSensor.getSensorResultReceiver(), antPlusCadenceSensor.getSensorStateChangeReceiver()));
                mAntPlusSensorList.setAntPlusCadenceSensor(antPlusCadenceSensor);
                break;
            case BIKE_SPD:
                AntPlusSpeedSensor antPlusSpeedSensor = new AntPlusSpeedSensor();
                antPlusSpeedSensor.setReleaseHandle(AntPlusBikeSpeedDistancePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, false, antPlusSpeedSensor.getSensorResultReceiver(), antPlusSpeedSensor.getSensorStateChangeReceiver()));
                mAntPlusSensorList.setAntPlusSpeedSensor(antPlusSpeedSensor);
                break;
            case BIKE_SPDCAD:
                throw new NotImplementedException();
            case CONTROLLABLE_DEVICE:
                throw new NotImplementedException();
            case FITNESS_EQUIPMENT:
                throw new NotImplementedException();
            case BLOOD_PRESSURE:
                throw new NotImplementedException();
            case GEOCACHE:
                throw new NotImplementedException();
            case ENVIRONMENT:
                throw new NotImplementedException();
            case WEIGHT_SCALE:
                throw new NotImplementedException();
            case STRIDE_SDM:
                throw new NotImplementedException();
            case UNKNOWN:
                throw new NotImplementedException();
            default:
                throw new NotImplementedException();
        }
        return mAntPlusSensorList;
    }

    public enum AntPlusSensorDeviceSaveResult{
        NEW_SENSOR_SAVED,
        REPLACED_OLD_SENSOR,
        ERROR
    }

}
