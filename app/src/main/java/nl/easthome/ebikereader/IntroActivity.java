package nl.easthome.ebikereader;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import nl.easthome.ebikereader.Fragments.AntPlusSupportSlideFragment;
import nl.easthome.ebikereader.Fragments.BikeInfoSlideFragment;
import nl.easthome.ebikereader.Fragments.BodyMeasurementSlideFragment;
import nl.easthome.ebikereader.Fragments.GpsPermissionSlideFragment;
import nl.easthome.ebikereader.Fragments.LoginSlideFragment;
import nl.easthome.ebikereader.Fragments.WelcomeSlideFragment;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Helpers.SharedPrefsSaver;
import nl.easthome.ebikereader.Interfaces.ILoginCompletedListener;
import nl.easthome.ebikereader.Objects.UserDetails;

public class IntroActivity extends AppIntro implements ILoginCompletedListener {
    LoginSlideFragment loginSlideFragment = new LoginSlideFragment();
    BodyMeasurementSlideFragment bodyMeasurementSlideFragment = new BodyMeasurementSlideFragment();
    BikeInfoSlideFragment bikeInfoSlideFragment = new BikeInfoSlideFragment();

    boolean introFinished = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSkipButton(false);
        setBackButtonVisibilityWithDone(true);
        createSlides();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        UserDetails userDetails = new UserDetails();
        userDetails.fillUserBodyFields(bodyMeasurementSlideFragment);
        userDetails.fillBikeInfoFields(bikeInfoSlideFragment);
        FirebaseSaver.setUserDetails(FirebaseAuth.getInstance().getUid(), userDetails);
        SharedPrefsSaver.saveUserDetails(this, userDetails);
        introFinished = true;
        startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void createSlides() {
        setOffScreenPageLimit(6);
        addSlide(new WelcomeSlideFragment());
        addSlide(new AntPlusSupportSlideFragment());
        addSlide(new GpsPermissionSlideFragment());
        addSlide(loginSlideFragment);
        addSlide(bodyMeasurementSlideFragment);
        addSlide(bikeInfoSlideFragment);
        loginSlideFragment.addLoginCompleteListener(this);

    }

    @Override
    protected void onDestroy() {
        FirebaseAuth instance = FirebaseAuth.getInstance();
        if (!introFinished && instance.getCurrentUser() != null){
            instance.signOut();
        }

        super.onDestroy();
    }

    @Override
    public void onLoginComplete() {
        FirebaseSaver.getUserDetails(FirebaseAuth.getInstance().getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                if (userDetails != null) {
                    bodyMeasurementSlideFragment.fillFields(userDetails);
                    bikeInfoSlideFragment.fillFields(userDetails);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
