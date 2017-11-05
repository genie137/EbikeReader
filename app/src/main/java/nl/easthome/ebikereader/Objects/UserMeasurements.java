package nl.easthome.ebikereader.Objects;

public class UserMeasurements {
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

    public String getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(String userHeight) {
        UserHeight = userHeight;
    }

    public String getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(String userWeight) {
        UserWeight = userWeight;
    }
}
