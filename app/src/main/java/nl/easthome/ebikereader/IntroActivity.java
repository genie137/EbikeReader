package nl.easthome.ebikereader;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dsi.ant.AntSupportChecker;
import com.firebase.ui.auth.AuthUI;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import java.util.Arrays;
import java.util.List;

import nl.easthome.ebikereader.Objects.UserMeasurements;

public class IntroActivity extends AppIntro {
    private static final int RC_SIGN_IN = 9001;
    private UserMeasurements userMeasurements;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSkipButton(false);
        setWizardMode(true);
        setBackButtonVisibilityWithDone(true);
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
        createSlides();
    }

    private void createSlides() {
        SliderPage spWelcomeSlide = new SliderPage();
        spWelcomeSlide.setTitle(getString(R.string.intro_screen_welcome_title));
        spWelcomeSlide.setDescription(getString(R.string.intro_screen_welcome_description));
        spWelcomeSlide.setImageDrawable(R.drawable.ic_ebike);
        spWelcomeSlide.setBgColor(ContextCompat.getColor(this, R.color.intro_screen_welcome_slide));
        addSlide(AppIntroFragment.newInstance(spWelcomeSlide));
        SliderPage spAntSupportSlide = new SliderPage();
        spAntSupportSlide.setTitle(getString(R.string.intro_screen_antsupport_title));
        spAntSupportSlide.setDescription(getString(R.string.intro_screen_antsupport_description));
        spAntSupportSlide.setImageDrawable(R.drawable.ic_settings_input_antenna);
        spAntSupportSlide.setBgColor(ContextCompat.getColor(this,R.color.intro_screen_welcome_slide));
        addSlide(AppIntroFragment.newInstance(spAntSupportSlide));
        SliderPage spGpsPermSlide = new SliderPage();
        spGpsPermSlide.setTitle(getString(R.string.intro_screen_gpsperm_title));
        spGpsPermSlide.setDescription(getString(R.string.intro_screen_gpsperm_description));
        spGpsPermSlide.setImageDrawable(R.drawable.ic_my_location);
        spGpsPermSlide.setBgColor(ContextCompat.getColor(this, R.color.intro_screen_welcome_slide));
        addSlide(AppIntroFragment.newInstance(spGpsPermSlide));
        SliderPage spBodyMeasurementSlide = new SliderPage();
        spBodyMeasurementSlide.setTitle("Body Measurements");
        spBodyMeasurementSlide.setDescription("To improve measurements, we want to know more about you! Please fill in the data in the next window.");
        spBodyMeasurementSlide.setImageDrawable(R.drawable.ic_fitness_center);
        spBodyMeasurementSlide.setBgColor(ContextCompat.getColor(this, R.color.intro_screen_welcome_slide));
        addSlide(AppIntroFragment.newInstance(spBodyMeasurementSlide));
        SliderPage spLoginSlide = new SliderPage();
        spLoginSlide.setTitle(getString(R.string.intro_screen_login_title));
        spLoginSlide.setDescription(getString(R.string.intro_screen_login_description));
        spLoginSlide.setImageDrawable(R.drawable.ic_info_outline);
        spLoginSlide.setBgColor(ContextCompat.getColor(this, R.color.intro_screen_welcome_slide));
        addSlide(AppIntroFragment.newInstance(spLoginSlide));

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Log.d("Measurements", userMeasurements.toString());
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setTosUrl(getString(R.string.auth_tos_url))
                        .setPrivacyPolicyUrl(getString(R.string.auth_privacypolicy_url))
                        .setIsSmartLockEnabled(!android.support.design.BuildConfig.DEBUG)
                        .setTheme(R.style.AppThemeLogin)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return;
            }
        }
    }


    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        List<Fragment> slides = getSlides();
        if (oldFragment != null) {
            if (slides.get(1).equals(oldFragment) && slides.get(2).equals(newFragment)) {
                checkAntSupport();
            }
            if (slides.get(3).equals(oldFragment) && slides.get(4).equals(newFragment))
                askBodyMeasurements();
            }
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void askBodyMeasurements() {
        new MaterialDialog.Builder(this)
                .title("test")
                .positiveText("Done")
                .customView(R.layout.measurement_dialog, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ConstraintLayout layout = (ConstraintLayout) dialog.getCustomView();
                        EditText weightInput = layout.findViewById(R.id.weight_input);
                        EditText heightInput = layout.findViewById(R.id.height_input);
                        userMeasurements = new UserMeasurements(heightInput.getText().toString(), weightInput.getText().toString());
                    }
                })
                .show();
    }

    private void checkAntSupport() {
        if (AntSupportChecker.hasAntFeature(this)) {
            new MaterialDialog.Builder(this)
                    .title(R.string.intro_dialog_antsupport_title)
                    .content(R.string.intro_dialog_antsupport_positive_content)
                    .positiveText(R.string.intro_dialog_antsupport_positive_button_text)
                    .show();
        }
        else {
            new MaterialDialog.Builder(this)
                    .title(R.string.intro_dialog_antsupport_title)
                    .content(R.string.intro_dialog_antsupport_negative_content)
                    .positiveText(R.string.intro_dialog_antsupport_negative_finddevice_button_text)
                    .negativeText(R.string.intro_dialog_antsupport_negative_exit_button_text)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.intro_dialog_antsupport_negative_finddevice_link)));
                            startActivity(i);
                            finish();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).show();
        }
    }
}
