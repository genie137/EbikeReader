package nl.easthome.ebikereader.Objects;
import io.realm.RealmObject;


public class UserMeasurements extends RealmObject {
    private String UserHeight;
    private String UserWeight;

    public UserMeasurements() {

    }

    public UserMeasurements(String userHeight, String userWeight) {
        UserHeight = userHeight;
        UserWeight = userWeight;
    }

    @Override
    public String toString() {
        return "UserMeasurements{" +
                "UserHeight='" + UserHeight + '\'' +
                ", UserWeight='" + UserWeight + '\'' +
                '}';
    }
}
