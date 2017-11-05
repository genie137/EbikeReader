package nl.easthome.ebikereader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import io.fabric.sdk.android.Fabric;
import nl.easthome.ebikereader.Helpers.FirebaseSaver;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		FirebaseApp.initializeApp(this);
		setupNotificationChannel();

		FirebaseAuth auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, IntroActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
        }
        else{
			FirebaseSaver.getUserMeasurements(FirebaseAuth.getInstance().getUid());
			startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
		}
	}

	private void setupNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel mNotificationChannel = new NotificationChannel(getString(R.string.notification_id_channel), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
			mNotificationManager.createNotificationChannel(mNotificationChannel);
		}
	}




}
