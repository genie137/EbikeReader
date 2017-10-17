package nl.easthome.ebikereader.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import nl.easthome.ebikereader.DashboardActivity;
import nl.easthome.ebikereader.R;

public class RideRecordingService extends Service {
    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    private NotificationManager mNotificationManager;
	private int mNotificationID = R.string.notification_id;

	@Override
	public void onCreate() {
        HandlerThread thread = new HandlerThread("rideRecordingService", Thread.NORM_PRIORITY);
        thread.start();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = mServiceHandler.obtainMessage();
        showNotification();
        mServiceHandler.sendMessage(message);
        return START_STICKY;
    }



    @Override
	public void onDestroy() {
		mNotificationManager.cancel(mNotificationID);
	}

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, mNotificationID, new Intent(this, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_id_channel));
		Notification notification = builder
				.setContentTitle(getString(R.string.recording_notification_title))
				.setContentText(getString(R.string.recording_notification_text))
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_action_record)
				.setWhen(System.currentTimeMillis())
				.build();
		notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(mNotificationID, notification);
	}

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Well calling mServiceHandler.sendMessage(message); from onStartCommand,
            // this method will be called.

            // Add your cpu-blocking activity here


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }
}
