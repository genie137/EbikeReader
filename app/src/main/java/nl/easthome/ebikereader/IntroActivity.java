package nl.easthome.ebikereader;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import nl.easthome.ebikereader.Fragments.AntPlusSupportSlideFragment;
import nl.easthome.ebikereader.Fragments.BikeInfoSlideFragment;
import nl.easthome.ebikereader.Fragments.BodyMeasurementSlideFragment;
import nl.easthome.ebikereader.Fragments.GpsPermissionSlideFragment;
import nl.easthome.ebikereader.Fragments.LoginSlideFragment;
import nl.easthome.ebikereader.Fragments.WelcomeSlideFragment;

public class IntroActivity extends AppIntro {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            showSkipButton(false);
            setBackButtonVisibilityWithDone(true);
            createSlides();
        }

        private void createSlides() {
            setOffScreenPageLimit(10);
            addSlide(new WelcomeSlideFragment());
            addSlide(new AntPlusSupportSlideFragment());
            addSlide(new LoginSlideFragment());
            addSlide(new GpsPermissionSlideFragment());
            addSlide(new BodyMeasurementSlideFragment());
            addSlide(new BikeInfoSlideFragment());
        }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}