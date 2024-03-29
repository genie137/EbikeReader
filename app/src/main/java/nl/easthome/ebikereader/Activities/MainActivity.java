package nl.easthome.ebikereader.Activities;

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
import nl.easthome.ebikereader.R;

public class MainActivity extends AppCompatActivity {

    /**
     * Setup of application tools (Fabric, Firebase and Notifications)
     * Sends the user to the Dashboard or Intro activity, based upon if the user has stored credentials.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
        setupNotificationChannel();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, IntroActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else {
            startActivity(new Intent(this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    /**
     * Setup the Notification Channel if the system is running Android Oreo.
     */
    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mNotificationChannel = new NotificationChannel(getString(R.string.notification_id_channel), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mNotificationChannel);
            }
        }
    }




}
