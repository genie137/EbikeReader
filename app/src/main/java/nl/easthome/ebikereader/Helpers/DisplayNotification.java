package nl.easthome.ebikereader.Helpers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.R;

import java.util.Random;

public class DisplayNotification {
	private Activity mActivity;
	private NotificationManager mNotificationManager;

	public DisplayNotification(Activity activity) {
		mActivity = activity;
		mNotificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public int displayOngoingNotification() {
		int notificationID = new Random().nextInt(9999 - 1000) + 1000;
		PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, notificationID, new Intent(mActivity, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity, mActivity.getResources().getString(R.string.notification_id_channel));
		Notification notification = builder
				.setContentTitle("Recording...")
				.setContentText("A ride is currently being recorded in the background. Click on me to go back to the app.")
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_action_record)
				.build();
		notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(notificationID, notification);
		return notificationID;
	}

	public void cancelNotification(int mNotificationID) {
		mNotificationManager.cancel(mNotificationID);
	}
}
