package nl.easthome.antpluslibary;
import android.app.Activity;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import nl.easthome.antpluslibary.Enums.AntPlusSensorDeviceSaveResult;
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

/**
 * Class that handles the saving, retrieving and connecting of saved devices.
 */
@SuppressWarnings("unchecked")
public class AntPlusDeviceManager {
    private static final int PROXIMITY = 10;
    private static final String SP_PAGE_NAME = "ANT+DeviceManager";
    private static AntPlusSensorList mAntPlusSensorList = new AntPlusSensorList();
    private Activity mActivity;

    /**
     * Constructor
     *
     * @param activity The activity where the object was created.
     */
    public AntPlusDeviceManager(Activity activity) {
        mActivity = activity;
    }


    /**
     * Removes the saved sensor for the specified deviceType from the shared preferences.
     * @param deviceType The devicetype.
     * @return true if deleted, false if there was no sensor id saved.
     */
    public boolean removeSensorForType(DeviceType deviceType) {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(SP_PAGE_NAME, 0);
        int deviceNum = sharedPreferences.getInt(deviceType.toString(), 0);

        if (deviceNum == 0){
            return false;
        } else {
            sharedPreferences.edit().putInt(deviceType.toString(), 0).apply();
            return true;
        }
    }

    /**
     * Save an ant+ device number to the shared preferences.
     * @param deviceType The device type that needs to be saved.
     * @param antDeviceNumber The device id that needs to be saved
     * @return The result of saving.
     */
    public AntPlusSensorDeviceSaveResult saveSensorForType(DeviceType deviceType, int antDeviceNumber) {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(SP_PAGE_NAME, 0);

        try {
            int data = sharedPreferences.getInt(deviceType.toString(), 0);
            if (data == 0) {
                sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                return AntPlusSensorDeviceSaveResult.NEW_SENSOR_SAVED;
            } else {
                sharedPreferences.edit().putInt(deviceType.toString(), antDeviceNumber).apply();
                return AntPlusSensorDeviceSaveResult.REPLACED_OLD_SENSOR;
            }
        } catch (Exception e){
            e.printStackTrace();
            return AntPlusSensorDeviceSaveResult.ERROR;
        }

    }

    /**
     * Returns the saved id from a specified device type.
     * @param type The device type.
     * @return The id for the device.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public int getDeviceIdForType(DeviceType type) throws NoDeviceConfiguredException {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

        int result = sharedPreferences.getInt(type.toString(), 0);

        if (result == 0){
            throw new NoDeviceConfiguredException(type);
        }

        return result;
    }

    /**
     * Connects to the power sensor.
     * @param sensorHandler The sensor handler for the specified sensor.
     * @return The power sensor object.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public AntPlusPowerSensor initConnectionToPowerSensor(ISensorHandler<AntPlusBikePowerPcc, AntPlusPowerSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusPowerSensor antPlusPowerSensor = new AntPlusPowerSensor(sensorHandler);
        antPlusPowerSensor.setReleaseHandle(AntPlusBikePowerPcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_POWER), PROXIMITY, antPlusPowerSensor.getSensorResultReceiver(), antPlusPowerSensor.getSensorStateChangeReceiver()));
        sensorHandler.setDataDeque(antPlusPowerSensor.getSensorDataDeque());
        mAntPlusSensorList.setAntPlusPowerSensor(antPlusPowerSensor);
        return antPlusPowerSensor;
    }

    /**
     * Connects to the heart sensor.
     * @param sensorHandler The sensor handler for the specified sensor.
     * @return The heart sensor object.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public AntPlusHeartSensor initConnectionToHeartRateSensor(ISensorHandler<AntPlusHeartRatePcc, AntPlusHeartSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusHeartSensor antPlusHeartSensor = new AntPlusHeartSensor(sensorHandler);
        antPlusHeartSensor.setReleaseHandle(AntPlusHeartRatePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.HEARTRATE), PROXIMITY, antPlusHeartSensor.getSensorResultReceiver(), antPlusHeartSensor.getSensorStateChangeReceiver()));
        sensorHandler.setDataDeque(antPlusHeartSensor.getSensorDataDeque());
        mAntPlusSensorList.setAntPlusHeartSensor(antPlusHeartSensor);
        return antPlusHeartSensor;
    }

    /**
     * Connects to the cadence sensor.
     * @param sensorHandler The sensor handler for the specified sensor.
     * @return The cadence sensor object.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public AntPlusCadenceSensor initConnectionToCadenceSensor(ISensorHandler<AntPlusBikeCadencePcc, AntPlusCadenceSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusCadenceSensor antPlusCadenceSensor = new AntPlusCadenceSensor(sensorHandler);
        antPlusCadenceSensor.setReleaseHandle(AntPlusBikeCadencePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_CADENCE), PROXIMITY, false, antPlusCadenceSensor.getSensorResultReceiver(), antPlusCadenceSensor.getSensorStateChangeReceiver()));
        sensorHandler.setDataDeque(antPlusCadenceSensor.getSensorDataDeque());
        mAntPlusSensorList.setAntPlusCadenceSensor(antPlusCadenceSensor);
        return antPlusCadenceSensor;
    }

    /**
     * Connects to the speed sensor.
     * @param sensorHandler The sensor handler for the specified sensor.
     * @return The speed sensor object.
     * @throws NoDeviceConfiguredException Is thrown when no device id from that device type was saved.
     */
    public AntPlusSpeedSensor initConnectionToSpeedSensor(ISensorHandler<AntPlusBikeSpeedDistancePcc, AntPlusSpeedSensorData> sensorHandler) throws NoDeviceConfiguredException {
        AntPlusSpeedSensor antPlusSpeedSensor = new AntPlusSpeedSensor(sensorHandler);
        antPlusSpeedSensor.setReleaseHandle(AntPlusBikeSpeedDistancePcc.requestAccess(mActivity, getDeviceIdForType(DeviceType.BIKE_SPD), PROXIMITY, false, antPlusSpeedSensor.getSensorResultReceiver(), antPlusSpeedSensor.getSensorStateChangeReceiver()));
        sensorHandler.setDataDeque(antPlusSpeedSensor.getSensorDataDeque());
        mAntPlusSensorList.setAntPlusSpeedSensor(antPlusSpeedSensor);
        return antPlusSpeedSensor;
    }

    /**
     * Disconnect from all sensors, if connected.
     */
    public void disconnectAllSensors() {
        try{
            AntPlusPowerSensor powerSensor = mAntPlusSensorList.getAntPlusPowerSensor();
            if (powerSensor != null) {
                powerSensor.getResultConnection().releaseAccess();
            }
        } catch (NullPointerException ignored){}
        try {
            AntPlusCadenceSensor cadenceSensor = mAntPlusSensorList.getAntPlusCadenceSensor();
            if (cadenceSensor != null) {
                cadenceSensor.getResultConnection().releaseAccess();
            }
        } catch (NullPointerException ignored){}
        try{
            AntPlusHeartSensor heartSensor = mAntPlusSensorList.getAntPlusHeartSensor();
            if (heartSensor != null) {
                heartSensor.getResultConnection().releaseAccess();
            }
        } catch (NullPointerException ignored){}
        try{
            AntPlusSpeedSensor speedSensor = mAntPlusSensorList.getAntPlusSpeedSensor();
            if (speedSensor != null) {
                speedSensor.getResultConnection().releaseAccess();
            }
        } catch (NullPointerException ignored){}

    }
}
