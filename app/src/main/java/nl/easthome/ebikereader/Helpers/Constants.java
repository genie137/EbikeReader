package nl.easthome.ebikereader.Helpers;

import android.text.format.DateFormat;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.Locale;

import nl.easthome.antpluslibary.AntPlusSensorScanner;

public class Constants {
    public static final int MAX_LOCATION_INTERVAL_MS = 5000;
    public static final int MIN_LOCATION_INTERVAL_MS = 3000;

    public static long getSystemTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static double convertMStoKMS(double speedInMeterPerSeconds) {
        return speedInMeterPerSeconds * 3.6;
    }

    public static String convertTimestampToDateTime(long timestamp){
        timestamp = timestamp * 1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd-MM-yyyy @ HH:mm", cal).toString();
    }

    public static String convertTimestampToSimpleDateTime(long timestamp){
        timestamp = timestamp * 1000;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("ddMMyyyyHHmm", cal).toString();
    }

    public static EnumSet<DeviceType> getBikeDeviceTypeSet() {
        EnumSet<DeviceType> deviceTypes = AntPlusSensorScanner.getEmptyDeviceTypeSet();
        deviceTypes.add(DeviceType.BIKE_POWER);
        deviceTypes.add(DeviceType.BIKE_CADENCE);
        deviceTypes.add(DeviceType.BIKE_SPD);
        deviceTypes.add(DeviceType.HEARTRATE);
        return deviceTypes;
    }

}
