package nl.easthome.ebikereader.Helpers;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.easthome.ebikereader.Objects.UserMeasurements;
/**
 * Created by Joris Oosterhuis on 11/4/2017.
 */

public class FirebaseSaver {

    private static final String FIREBASE_USER_DIRECTORY = "users";
    private static final String FIREBASE_USER_BODY_SETTINGS_DIRECTORY = FIREBASE_USER_DIRECTORY + "/body/";



    public static void setUserMeasurements(String uid, UserMeasurements userMeasurements){
        String ref = FIREBASE_USER_BODY_SETTINGS_DIRECTORY + uid;
        FirebaseDatabase.getInstance().getReference(ref).setValue(userMeasurements);
    }

    public static void getUserMeasurements(String uid, ValueEventListener valueEventListener) {
        String ref = FIREBASE_USER_BODY_SETTINGS_DIRECTORY + uid;
        FirebaseDatabase.getInstance().getReference(ref).addListenerForSingleValueEvent(valueEventListener);
    }

}
