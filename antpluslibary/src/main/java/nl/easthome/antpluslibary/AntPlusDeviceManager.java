package nl.easthome.antpluslibary;
import android.app.Activity;
import android.content.SharedPreferences;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikePowerPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.antpluslibary.Objects.AntPlusSensorConnection;

public class AntPlusDeviceManager {
    private Activity mActivity;
    private static ArrayList<AntPlusSensorConnection> mConnectedSensors = new ArrayList<>();
    private static final int PROXIMITY = 10;

    public AntPlusDeviceManager(Activity activity) {
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

    public int getDeviceIdForType(DeviceType type) throws NoDeviceConfiguredException {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), 0);

        int result = sharedPreferences.getInt(type.toString(), 0);

        if (result == 0){
            throw new NoDeviceConfiguredException(type);
        }

        return result;
    }

    public AntPlusSensorConnection getConnectionFromSavedDevice(DeviceType type) throws NotImplementedException, NoDeviceConfiguredException {
        switch (type){
            case BIKE_POWER:
                AntPlusSensorConnection<AntPlusBikePowerPcc> conn = new AntPlusSensorConnection<>(mActivity);
                conn.setReleaseHandle(AntPlusBikePowerPcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, conn.getSensorResultReceiver(), conn.getSensorStateChangeReceiver()));
                mConnectedSensors.add(conn);
                return conn;
            case HEARTRATE:
                AntPlusSensorConnection<AntPlusHeartRatePcc> conn1 = new AntPlusSensorConnection<>(mActivity);
                conn1.setReleaseHandle(AntPlusHeartRatePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, conn1.getSensorResultReceiver(), conn1.getSensorStateChangeReceiver()));
                mConnectedSensors.add(conn1);
                return conn1;
            case BIKE_CADENCE:
                AntPlusSensorConnection<AntPlusBikeCadencePcc> conn2 = new AntPlusSensorConnection<>(mActivity);
                conn2.setReleaseHandle(AntPlusBikeCadencePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, false, conn2.getSensorResultReceiver(), conn2.getSensorStateChangeReceiver()));
                mConnectedSensors.add(conn2);
                return conn2;
            case BIKE_SPD:
                AntPlusSensorConnection<AntPlusBikeSpeedDistancePcc> conn3 = new AntPlusSensorConnection<>(mActivity);
                conn3.setReleaseHandle(AntPlusBikeSpeedDistancePcc.requestAccess(mActivity, getDeviceIdForType(type), PROXIMITY, false, conn3.getSensorResultReceiver(), conn3.getSensorStateChangeReceiver()));
                mConnectedSensors.add(conn3);
                return conn3;
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
    }

    public static ArrayList<AntPlusSensorConnection> getConnectedSensors() {
        return mConnectedSensors;
    }

    public enum AntPlusSensorDeviceSaveResult{
        NEW_SENSOR_SAVED,
        REPLACED_OLD_SENSOR,
        ERROR
    }

}
