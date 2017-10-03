package nl.easthome.ebikereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity {
	private GoogleApiClient mGAC;
	private static final int RC_SIGN_IN = 9001;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
		mGAC = new GoogleApiClient.Builder(this).enableAutoManage(this, null).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.sign_in_button:
						Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGAC);
						startActivityForResult(signInIntent, RC_SIGN_IN);
						break;
				}
			}
		});
		findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.sign_out_button:
						TextView textView = (TextView) findViewById(R.id.loginResult);
						textView.setText("Signed Out");
						Auth.GoogleSignInApi.signOut(mGAC);
						Auth.GoogleSignInApi.revokeAccess(mGAC);
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				GoogleSignInAccount acct = result.getSignInAccount();
				TextView textView = (TextView) findViewById(R.id.loginResult);
				textView.setText("Signed in as: " + acct.getGivenName() + " " + acct.getFamilyName());
			}
		}
	}
}
