package nl.easthome.ebikereader.Helpers;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Objects.UserMeasurements;

public class FirebaseSaver {

    private static final String FIREBASE_USER_DIRECTORY = "users";
    private static final String FIREBASE_RIDES_DIRECTORY = "rides";
    private static final String FIREBASE_BODY_DIRCTORY = "body";


    public static void setUserMeasurements(String uid, UserMeasurements userMeasurements){
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid + "/" + FIREBASE_BODY_DIRCTORY;
        FirebaseDatabase.getInstance().getReference(ref).setValue(userMeasurements);
    }
    public static void addRideMeasurement(String rideID, RideMeasurement rideMeasurement){
        String ref = FIREBASE_RIDES_DIRECTORY + "/" + rideID + "/" + Long.toString(System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference(ref).setValue(rideMeasurement);
    }

    public static void addRideToUser(String rideID, String uid){
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid + "/" +FIREBASE_RIDES_DIRECTORY;
        FirebaseDatabase.getInstance().getReference(ref).push().setValue(rideID);
    }

    public static void getUserMeasurements(String uid, ValueEventListener valueEventListener) {
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid + "/" + FIREBASE_BODY_DIRCTORY;
        FirebaseDatabase.getInstance().getReference(ref).addListenerForSingleValueEvent(valueEventListener);
    }

}
