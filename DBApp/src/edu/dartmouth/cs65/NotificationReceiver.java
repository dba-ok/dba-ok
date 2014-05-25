package edu.dartmouth.cs65;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	NotificationManager notifyManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("NotificationAlarm", "onReceive");

		
/*		notifyManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// This Activity will be started when the user clicks the notification
		// in the notification bar
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		Bundle extras = intent.getExtras();

		Notification notif;

		if (extras.getString("MSG").equals("Mealswipe")) {

			notif = new Notification(R.drawable.ic_launcher,
					"USE A MEAL SWIPE!!", System.currentTimeMillis());
			
			notif.setLatestEventInfo(context, "DBA-OK", "Use a meal swipe!",
					contentIntent);
			notifyManager.notify(1, notif);

		} else {
			notif = new Notification(R.drawable.ic_launcher,
					extras.getString("Location") + " is closing!!", System.currentTimeMillis());

			notif.setLatestEventInfo(context, "DBA-OK", extras.getString("Location") + " is closing!",
					contentIntent);
			notifyManager.notify(1, notif);

		}
*/
	}

}