package nl.easthome.antpluslibary;
import android.app.Activity;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Interfaces.ISensorHandler;
import nl.easthome.antpluslibary.Objects.AntPlusSensorList;
import nl.easthome.antpluslibary.SensorData.AntPlusCadenceSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusHeartSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusPowerSensorData;
import nl.easthome.antpluslibary.SensorData.AntPlusSpeedSensorData;
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

    public AntPlusPowerSensor initConnectionToPowerSensor(ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusPowerSensor antPlusPowerSensor = new AntPlusPowerSensor(sensorHandler);
        antPlusPowerSensor.setReleaseHandle(AntPlusBikePowerPcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_POWER), PROXIMITY, antPlusPowerSensor.getSensorResultReceiver(), antPlusPowerSensor.getSensorStateChangeReceiver()));
        mAntPlusSensorList.setAntPlusPowerSensor(antPlusPowerSensor);
        return antPlusPowerSensor;
    }

    public AntPlusHeartSensor initConnectionToHeartRateSensor(ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusHeartSensor antPlusHeartSensor = new AntPlusHeartSensor(sensorHandler);
        antPlusHeartSensor.setReleaseHandle(AntPlusHeartRatePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.HEARTRATE), PROXIMITY, antPlusHeartSensor.getSensorResultReceiver(), antPlusHeartSensor.getSensorStateChangeReceiver()));
        mAntPlusSensorList.setAntPlusHeartSensor(antPlusHeartSensor);
        return antPlusHeartSensor;
    }

    public AntPlusCadenceSensor initConnectionToCadenceSensor(ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusCadenceSensor antPlusCadenceSensor = new AntPlusCadenceSensor(sensorHandler);
        antPlusCadenceSensor.setReleaseHandle(AntPlusBikeCadencePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_CADENCE), PROXIMITY, false, antPlusCadenceSensor.getSensorResultReceiver(), antPlusCadenceSensor.getSensorStateChangeReceiver()));
        mAntPlusSensorList.setAntPlusCadenceSensor(antPlusCadenceSensor);
        return antPlusCadenceSensor;
    }

    public AntPlusSpeedSensor initConnectionToSpeedSensor(ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusSpeedSensor antPlusSpeedSensor = new AntPlusSpeedSensor(sensorHandler);
        antPlusSpeedSensor.setReleaseHandle(AntPlusBikeSpeedDistancePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_SPD), PROXIMITY, false, antPlusSpeedSensor.getSensorResultReceiver(), antPlusSpeedSensor.getSensorStateChangeReceiver()));
        mAntPlusSensorList.setAntPlusSpeedSensor(antPlusSpeedSensor);
        return antPlusSpeedSensor;
    }

    public enum AntPlusSensorDeviceSaveResult{
        NEW_SENSOR_SAVED,
        REPLACED_OLD_SENSOR,
        ERROR
    }

}
