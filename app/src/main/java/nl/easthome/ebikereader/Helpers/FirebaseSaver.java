package nl.easthome.ebikereader.Helpers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.Objects.UserDetails;

public class FirebaseSaver {

    private static final String FIREBASE_USER_DIRECTORY = "users";
    private static final String FIREBASE_RIDES_DIRECTORY = "rides";
    private static final String FIREBASE_USER_DETAILS_DIRECTORY = "details";

    public static void setUserDetails(String uid, UserDetails userDetails) {
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid + "/" + FIREBASE_USER_DETAILS_DIRECTORY;
        FirebaseDatabase.getInstance().getReference(ref).setValue(userDetails);
    }

    public static void getUserDetails(String uid, ValueEventListener valueEventListener) {
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid + "/" + FIREBASE_USER_DETAILS_DIRECTORY;
        FirebaseDatabase.getInstance().getReference(ref).addListenerForSingleValueEvent(valueEventListener);
    }

    public static void removeUserData(String uid, DatabaseReference.CompletionListener completionListener) {
        String ref = FIREBASE_USER_DIRECTORY + "/" + uid;
        FirebaseDatabase.getInstance().getReference(ref).removeValue(completionListener);
    }

    public static String addNewRide(RideRecording rideRecording, String uid) {
        String RefNewRideID = FIREBASE_RIDES_DIRECTORY;
        String newRideId = FirebaseDatabase.getInstance().getReference(RefNewRideID).push().getKey();
        String refRide = FIREBASE_RIDES_DIRECTORY + "/" + newRideId;
        FirebaseDatabase.getInstance().getReference(refRide).setValue(rideRecording);
        String refUser = FIREBASE_USER_DIRECTORY + "/" + uid + "/" + FIREBASE_RIDES_DIRECTORY;
        FirebaseDatabase.getInstance().getReference(refUser).push().setValue(newRideId);
        return newRideId;
    }

    public static void updateRideRecording(RideRecording rideRecording) {
        String ref = FIREBASE_RIDES_DIRECTORY + "/" + rideRecording.getRideId();
        FirebaseDatabase.getInstance().getReference(ref).setValue(rideRecording);
    }
}
