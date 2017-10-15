package nl.easthome.ebikereader.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.R;

public class RideRecordingService extends Service {
	private final IBinder mBinder = new LocalBinder();
	private NotificationManager mNotificationManager;
	private int mNotificationID = R.string.notification_id;

	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
	}

	@Override
	public void onDestroy() {
		mNotificationManager.cancel(mNotificationID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void showNotification() {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationID, new Intent(this, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_id_channel));
		Notification notification = builder
				.setContentTitle("Recording...")
				.setContentText("A ride is currently being recorded in the background. Click on me to go back to the app.")
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_action_record)
				.setWhen(System.currentTimeMillis())
				.build();
		notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(mNotificationID, notification);
	}

	public class LocalBinder extends Binder {
		RideRecordingService getService() {
			return RideRecordingService.this;
		}
	}
}
