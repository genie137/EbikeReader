package nl.easthome.ebikereader.Helpers;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;
public class Constants {

    public static final String CSV_COL_SEPERATOR = ",";
    public static final String CSV_ROW_SEPERATOR = "\n";

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
        String date = DateFormat.format("dd-MM-yyyy @ HH:mm", cal).toString();
        return date;
    }




}
