package nl.easthome.ebikereader.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Interfaces.ILoginCompletedListener;
import nl.easthome.ebikereader.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class LoginSlideFragment extends Fragment implements ISlidePolicy{
    private static final int RC_SIGN_IN = 9001;
    @BindView(R.id.loginButton) Button mLoginButton;
    private boolean loginState = false;
    private ILoginCompletedListener completeListner;

    public LoginSlideFragment() {
    }

    @OnClick(R.id.loginButton) public void loginButtonPressed(){
        mLoginButton.setText("Working...");
        mLoginButton.setEnabled(false);
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAllowNewEmailAccounts(true)
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

    //Todo move text to strings.xml
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            switch (resultCode){
                case RESULT_OK:
                    Toast.makeText(getContext(), "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "!", Toast.LENGTH_LONG).show();
                    mLoginButton.setText("Done!");
                    loginState = true;
                    if (completeListner != null){
                        completeListner.onLoginComplete();
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(getContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG).show();
                    mLoginButton.setText("Login");
                    mLoginButton.setEnabled(true);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_slide, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean isPolicyRespected() {
        return loginState;
    }

    //Todo move text to strings.xml
    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please login before continuing.", Toast.LENGTH_LONG).show();
    }


    public void addLoginCompleteListener(ILoginCompletedListener completedListener) {
        this.completeListner = completedListener;
    }
}
