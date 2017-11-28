package nl.easthome.ebikereader.Helpers;
public class SystemTime {

    public static long getSystemTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

}
