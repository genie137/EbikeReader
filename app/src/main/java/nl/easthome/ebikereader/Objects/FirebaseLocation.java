package nl.easthome.ebikereader.Objects;
import android.location.Location;

public class FirebaseLocation {

    float accuracy;
    double altitude;
    float bearing;
    long elapsedRealTimeNanos;
    double latitude;
    double longitude;
    String provider;
    float speed;
    long time;


    public FirebaseLocation() {
    }

    public FirebaseLocation(Location location) {
        accuracy = location.getAccuracy();
        altitude = location.getAltitude();
        bearing = location.getBearing();
        elapsedRealTimeNanos = location.getElapsedRealtimeNanos();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        provider = location.getProvider();
        speed = location.getSpeed();
        time = location.getTime();
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public long getElapsedRealTimeNanos() {
        return elapsedRealTimeNanos;
    }

    public void setElapsedRealTimeNanos(long elapsedRealTimeNanos) {
        this.elapsedRealTimeNanos = elapsedRealTimeNanos;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FirebaseLocation{" +
                "accuracy=" + accuracy +
                ", altitude=" + altitude +
                ", bearing=" + bearing +
                ", elapsedRealTimeNanos=" + elapsedRealTimeNanos +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", provider='" + provider + '\'' +
                ", speed=" + speed +
                ", time=" + time +
                '}';
    }
}
