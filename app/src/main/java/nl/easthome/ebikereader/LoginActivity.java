package nl.easthome.ebikereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
	private static final int RC_SIGN_IN = 9001;
	@BindView(R.id.login_layout) RelativeLayout mLoginLayout;
	@OnClick(R.id.sign_in_button) public void onClickSignInButton(){
		startActivityForResult(
				AuthUI.getInstance().createSignInIntentBuilder()
						.setAvailableProviders(
								Arrays.asList(
										new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
										new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
						.setTosUrl("www.google.com")
						.setPrivacyPolicyUrl("www.google.nl")
						.setIsSmartLockEnabled(!BuildConfig.DEBUG)
						.setTheme(R.style.AppTheme)
						.setLogo(R.drawable.ic_ebike)
						.build(), RC_SIGN_IN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		FirebaseApp.initializeApp(this);
		ButterKnife.bind(this);

		FirebaseAuth auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() != null) {
			startActivity(new Intent(this, DashboardActivity.class).putExtra("my_token", auth.getCurrentUser().getUid()));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);

			if (resultCode == RESULT_OK) {
				startActivity(new Intent(this, DashboardActivity.class).putExtra("my_token", response.getIdpToken()));
				return;
			}
			else {
				if (response == null) {
					showSnackbar(R.string.sign_in_cancelled);
					return;
				}
				if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
					showSnackbar(R.string.no_internet_connection);
					return;
				}
				if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					showSnackbar(R.string.unknown_error);
					return;
				}
			}
			showSnackbar(R.string.unknown_sign_in_response);
		}
	}

	private void showSnackbar(int textId) {
		Snackbar.make(mLoginLayout, getResources().getString(textId), Snackbar.LENGTH_SHORT).show();
	}


}
