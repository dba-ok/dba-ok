package edu.dartmouth.cs65;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {
	private static final String TAG = "MyService";
	private static final int  dailySeconds = 24*3600*1000;
	private static final String mealswipe = "Mealswipe";
	private static final String closing = "closing";
	/**
	 * The started service starts the AlarmManager.
	 */
	@Override
	public void onStart(Intent intent, int startid) {
		Intent i = new Intent(this, NotificationReceiver.class);
		i.putExtra("MSG", mealswipe);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent j = new Intent(this, NotificationReceiver.class);
		j.putExtra("MSG", closing);
		j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent pi_mealswipe = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pi_closing = PendingIntent.getBroadcast(this, 0, j, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 4);
		c.set(Calendar.MINUTE,25);
		c.set(Calendar.SECOND,30);
		
		// Repeat the notification every 15 seconds
		// WE CAN FIGURE OUT WHAT USER WANTS (from settings prefs) TO MAKE NOTIFICATIONS HAPPEN EVERY DAY RIGHT BEFORE FAV PLACE IS GOING TO CLOSE
		// CAN ALERT USER AT BEGINNING OF MEAL SWIPE TIMES/AT END OF MEAL SWIPE TIMES (30 mins before = most helpful).
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, /*c.getTimeInMillis()*/ System.currentTimeMillis(), 30*1000, pi_mealswipe);
		
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30*1000, pi_closing);
		
		//am.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), dailySeconds, pi);
		
		Toast.makeText(this, "My Service started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}
}