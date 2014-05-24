package edu.dartmouth.cs65;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationSetter extends BroadcastReceiver {

	/**
	 * Listens for Android's BOOT_COMPLETED broadcast and then executes the
	 * onReceive() method.
	 */
	@Override
	public void onReceive(Context context, Intent i) {
		Log.d("Autostart",
				"BOOT_COMPLETED broadcast received. Executing starter service.");
		/*
		Intent intent = new Intent(context, ReminderService.class);
		if (i.getExtras() != null) {
			intent.putExtra("PREF_KEY", i.getExtras().getString("PREF_KEY"));
		}
		context.startService(intent);
		*/
	}
}