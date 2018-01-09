package nl.easthome.ebikereader;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.google.firebase.auth.FirebaseAuth;

import nl.easthome.ebikereader.Fragments.AntPlusSupportSlideFragment;
import nl.easthome.ebikereader.Fragments.BikeInfoSlideFragment;
import nl.easthome.ebikereader.Fragments.BodyMeasurementSlideFragment;
import nl.easthome.ebikereader.Fragments.GpsPermissionSlideFragment;
import nl.easthome.ebikereader.Fragments.LoginSlideFragment;
import nl.easthome.ebikereader.Fragments.WelcomeSlideFragment;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;
import nl.easthome.ebikereader.Objects.UserDetails;

public class IntroActivity extends AppIntro{
    BodyMeasurementSlideFragment bodyMeasurementSlideFragment = new BodyMeasurementSlideFragment();
    BikeInfoSlideFragment bikeInfoSlideFragment = new BikeInfoSlideFragment();

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
        startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void createSlides() {
        setOffScreenPageLimit(6);
        addSlide(new WelcomeSlideFragment());
        addSlide(new AntPlusSupportSlideFragment());
        addSlide(new GpsPermissionSlideFragment());
        addSlide(new LoginSlideFragment());
        addSlide(bodyMeasurementSlideFragment);
        addSlide(bikeInfoSlideFragment);
    }
}
