package nl.easthome.ebikereader.Helpers;
public class SystemTime {

    public static long getSystemTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static double convertMStoKMS(double speedInMeterPerSeconds) {
        return speedInMeterPerSeconds * 3.6;
    }

}
