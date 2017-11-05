package nl.easthome.ebikereader.Helpers;
import com.google.firebase.database.FirebaseDatabase;

import nl.easthome.ebikereader.Objects.UserMeasurements;
/**
 * Created by Joris Oosterhuis on 11/4/2017.
 */

public class FirebaseSaver {

    private static final String FIREBASE_USER_DIRECTION = "users";
    private static final String FIREBASE_USER_BODY_SETTINGS_DIRECTION = "users/body";



    public static void saveUserMeasurements(String uid, UserMeasurements userMeasurements){
        FirebaseDatabase.getInstance().getReference(FIREBASE_USER_BODY_SETTINGS_DIRECTION);
    }

    public static void getUserMeasurements(String uid) {
        FirebaseDatabase.getInstance().getReference(FIREBASE_USER_DIRECTION).child(uid);
    }


}
