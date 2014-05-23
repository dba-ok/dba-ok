package edu.dartmouth.cs65;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {
	private static final String TAG = "MyService";
	private static final int dailySeconds = 24*3600*1000;
	private static final String mealswipe = "Mealswipe";
	private static final String closing = "closing";

	private static final int KAF_ALARM = 0;
	private static final int NOVACK_ALARM = 1;
	private static final int COLLIS_ALARM = 2;
	private static final int FOCO_ALARM = 3;
	private static final int HOP_ALARM = 4;
	private static final int MEAL_ALARM = 5;

	/**
	 * The started service starts the AlarmManager.
	 */
	@Override
	public void onStart(Intent intent, int startid) {
		// SharedPreferences settings =
		// PreferenceManager.getDefaultSharedPreferences(this);

		Intent i;
		if (intent.getExtras() != null) {
			setAlarmsFromPrefs(intent);
		} else {
			setAlarmsOnStart();
		}

		/*
		 * if(pref_key.equals(getString(R.string.closing_collis_key))){
		 * 
		 * } else if(pref_key.equals(getString(R.string.closing_kaf)))
		 * if(settings.getBoolean(getString(R.string.closing_kaf_key),true)) { i
		 * = new Intent(this, NotificationReceiver.class); i.putExtra("MSG",
		 * closing); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); } else
		 * if(settings.getBoolean)
		 */

		/*
		 * Intent i = new Intent(this,NotificationReceiver.class);
		 * i.putExtra("MSG", mealswipe);
		 * i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * 
		 * Intent j = new Intent(this, NotificationReceiver.class);
		 * j.putExtra("MSG", closing);
		 * j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * 
		 * PendingIntent pi_mealswipe = PendingIntent.getBroadcast(this, 0, i,
		 * PendingIntent.FLAG_UPDATE_CURRENT);
		 * 
		 * Calendar c = Calendar.getInstance(); c.set(Calendar.HOUR, 4);
		 * c.set(Calendar.MINUTE,25); c.set(Calendar.SECOND,30);
		 * 
		 * 
		 * // Repeat the notification every 15 seconds // WE CAN FIGURE OUT WHAT
		 * USER WANTS (from settings prefs) TO MAKE NOTIFICATIONS HAPPEN EVERY
		 * DAY RIGHT BEFORE FAV PLACE IS GOING TO CLOSE // CAN ALERT USER AT
		 * BEGINNING OF MEAL SWIPE TIMES/AT END OF MEAL SWIPE TIMES (30 mins
		 * before = most helpful). AlarmManager am = (AlarmManager)
		 * getSystemService(Context.ALARM_SERVICE); /*
		 * am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
		 * System.currentTimeMillis(), 30*1000, pi_mealswipe);
		 * 
		 * //am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
		 * System.currentTimeMillis(), 30*1000, pi_closing);
		 * 
		 * //am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
		 * c.getTimeInMillis(), dailySeconds, pi);
		 */
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

	public void setAlarmsFromPrefs(Intent intent) {

		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String pref_key = intent.getExtras().getString("PREF_KEY");

		Intent i = new Intent(this, NotificationReceiver.class);
		i.putExtra("MSG", closing);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi;

		Calendar c = Calendar.getInstance();
		if (pref_key.equals(getString(R.string.closing_collis_key))) {
			i.putExtra("Location", "Collis");
			pi = PendingIntent.getBroadcast(this, COLLIS_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (settings.getBoolean(getString(R.string.closing_collis_key),
					true)) {

				// change later
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 48);
				c.set(Calendar.SECOND, 30);

				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						c.getTimeInMillis(), dailySeconds, pi);
			} else {
				am.cancel(pi);
			}
		} else if (pref_key.equals(getString(R.string.closing_kaf_key))) {
			i.putExtra("Location", "Kaf");
			pi = PendingIntent.getBroadcast(this, KAF_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);

			if (settings.getBoolean(getString(R.string.closing_kaf_key), true)) {
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 25);
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), dailySeconds, pi);
			} else {
				am.cancel(pi);
			}
		} else if (pref_key.equals(getString(R.string.closing_foco_key))) {
			i.putExtra("Location", "Foco");
			pi = PendingIntent.getBroadcast(this, FOCO_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (settings.getBoolean(getString(R.string.closing_foco_key), true)) {
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 27);
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						/*c.getTimeInMillis()*/ System.currentTimeMillis(), dailySeconds, pi);
			} else {
				am.cancel(pi);
			}
		} else if (pref_key.equals(getString(R.string.closing_hop_key))) {
			i.putExtra("Location", "Hop");
			pi = PendingIntent.getBroadcast(this, HOP_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (settings.getBoolean(getString(R.string.closing_hop_key), true)) {
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 32);
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						c.getTimeInMillis(), dailySeconds, pi);
			} else {
				am.cancel(pi);
			}
		} else if (pref_key.equals(getString(R.string.closing_novack_key))) {
			i.putExtra("Location", "Novack");
			pi = PendingIntent.getBroadcast(this, NOVACK_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (settings.getBoolean(getString(R.string.closing_novack_key),
					true)) {
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 25);
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						c.getTimeInMillis(), dailySeconds, pi);

			} else {
				am.cancel(pi);
			}
		} else if (pref_key
				.equals(getString(R.string.meal_reminder_preference_key))) {
			i.putExtra("MSG", mealswipe);
			pi = PendingIntent.getBroadcast(this, MEAL_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			if (settings.getBoolean(
					getString(R.string.meal_reminder_preference_key), true)) {
				c.set(Calendar.HOUR, 12);
				c.set(Calendar.MINUTE, 35);
				am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						c.getTimeInMillis(), dailySeconds, pi);
			} else {
				am.cancel(pi);
			}

		}
	}

	public void setAlarmsOnStart() {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(this, NotificationReceiver.class);
		i.putExtra("MSG", closing);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi;
		Calendar c = Calendar.getInstance();

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (settings.getBoolean(getString(R.string.closing_collis_key), true)) {
			i.putExtra("Location", "Collis");
			pi = PendingIntent.getBroadcast(this, COLLIS_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 20);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					c.getTimeInMillis(), dailySeconds, pi);
		}
		if (settings.getBoolean(getString(R.string.closing_kaf_key), true)) {
			i.putExtra("Location", "Kaf");
			pi = PendingIntent.getBroadcast(this, KAF_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 40);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					c.getTimeInMillis(), dailySeconds, pi);
		}
		if (settings.getBoolean(getString(R.string.closing_hop_key), true)) {
			i.putExtra("Location", "Hop");
			pi = PendingIntent.getBroadcast(this, HOP_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 32);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					c.getTimeInMillis(), dailySeconds, pi);
		}
		if (settings.getBoolean(getString(R.string.closing_foco_key), true)) {
			i.putExtra("Location", "Foco");
			pi = PendingIntent.getBroadcast(this, FOCO_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 27);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					c.getTimeInMillis(), dailySeconds, pi);

		}
		if (settings.getBoolean(
				getString(R.string.meal_reminder_preference_key), true)) {
			i.putExtra("MSG", mealswipe);
			pi = PendingIntent.getBroadcast(this, FOCO_ALARM, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 35);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					c.getTimeInMillis(), dailySeconds, pi);
		}
	}
}