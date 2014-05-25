package edu.dartmouth.cs65;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Useful functions for BudgetFragment and MyStatsFragment
 * 
 * @author kimberlystrauch
 * 
 */

public class Utils {

	/**
	 * Function to figure out how much money has been spent at each dining
	 * location Each location will be given an integer constant in Globals. This
	 * integer constant value will be used to index into the returned arraylist
	 */
	public static ArrayList<Double> getLocationSpending(Context context) {
		ArrayList<Double> locationSpending = new ArrayList<Double>();
		for (int i = 0; i < 10; i++) {
			locationSpending.add(0.0);
		}

		// fetch all of the DBA transaction entries
		// also determine current term's start/end date
		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper.fetchEntries();

		// if there's no data in the database, return the default zero arraylist
		if (entryList.size() == 0) {
			Log.d("DATABASE","NOTHING IN DATABASE AGH!");
			return locationSpending;
		}
		Calendar[] cal = getTermStartEnd();

		int location = 0;
		for (TransactionEntry entry : entryList) {
			// if the transaction was before the start of the current term,
			// ignore it.
			if (entry.getDateTimeinMillis() < cal[0].getTimeInMillis()) {
				continue;
			}
			// find out location and increment the associated arrayList value
			location = entry.getLocation();
			locationSpending.set(location, locationSpending.get(location)
					+ entry.getAmount());
		}

		return locationSpending;
	}

	/**
	 * Function to figure out how much has been spent each week for MyStates
	 * 
	 * @param context
	 * @return the weekly spending for the current term
	 */
	public static ArrayList<Double> getWeeklySpending(Context context) {
		ArrayList<Double> weeklySpending = new ArrayList<Double>();
		for (int i = 0; i < 10; i++) {
			weeklySpending.add(0.0);
		}

		// fetch all DBA entries
		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper.fetchEntries();

		// if database is empty, entryList will be of size 0, we want to return
		// the default all-zero arraylist
		if (entryList.size() == 0) {
			Log.d("DATABASE","NOTHING IN DATABASE AGH!");
			return weeklySpending;
		}

		// find out current term's start/end date
		// figure out term's start day in year/
		Calendar[] cal = getTermStartEnd();
		int firstDay = cal[0].DAY_OF_YEAR;

		int week = 0;
		for (TransactionEntry entry : entryList) {
			// ignore all transactions before term's start
			if (entry.getDateTimeinMillis() < cal[0].getTimeInMillis()) {
				continue;
			}

			// find out which week in the term the transaction corresponds to
			// increment the appropriate index in the weeklySpending arraylist
			week = (entry.getDateTimeDay() - firstDay) / 7;
			weeklySpending.set(week,
					weeklySpending.get(week) + entry.getAmount());
		}

		return weeklySpending;
	}

	/**
	 * Figure out how many days in the term has passed, based on the most recent
	 * transaction entry
	 * 
	 * @param context
	 * @return number of days in term that have elapsed
	 */
	public static int getDaysElapsed(Context context) {
		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper.fetchEntries();
		Calendar[] cal = getTermStartEnd();
		Log.d("CS65", "CALENDAR FIRST DAY: " + cal[0]);
		int firstDay = cal[0].get(Calendar.DAY_OF_YEAR);
		Log.d("CS65", "FIRST DAY: " + firstDay);
		// access last entry in entry list
		int lastDay;
		if (entryList.size() > 0) {
			TransactionEntry lastEntry = entryList.get(entryList.size());
			lastDay = lastEntry.getDateTimeDay();
		} else {
			Calendar c = Calendar.getInstance();
			lastDay = c.get(Calendar.DAY_OF_YEAR);
			;
			Log.d("CS65", "LAST DAY: " + lastDay);
		}
		int daysPassed = lastDay - firstDay + 1;
		Log.d("CS65", "Days passed: " + daysPassed);
		return daysPassed;
	}

	/**
	 * Function for BudgetFragment to get a user's past average daily or weekly
	 * spending
	 * 
	 * @param context
	 * @param interval
	 *            , either an integer corresponding to a weekly or daily budget
	 * @return amt spent average per interval
	 */
	public static String getPastAverageSpending(Context context, int interval) {
		// get DBA balance from shared prefs
		String mKey = context.getString(R.string.preference_name);
		SharedPreferences mPrefs = context.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);
		mKey = context.getString(R.string.preference_key_balance);
		double bal = Double.parseDouble(mPrefs.getString(mKey, "0.0"));
		double amt = Globals.TOTAL_DBA_AMOUNT - bal;

		Log.d("CS65", "amount spent = " + amt);
		int numDays = getDaysElapsed(context);
		double weeksPassed = numDays / 7.0;

		// depending upon interval (either daily or weekly), determine the
		// user's past average spending
		switch (interval) {
		case Globals.AVG_DAILY_SPENDING:
			amt /= numDays;
			break;
		case Globals.AVG_WEEKLY_SPENDING:
			amt /= weeksPassed;
			break;
		default:
			amt = 0.0;
			break;
		}
		Log.d("CS65", "Amount spent per week" + amt);
		return "$" + String.format("%.2f", amt);
	}

	/**
	 * Function for BudgetFragment to get a user's projected average spending
	 * per interval to keep balance from going negative
	 * 
	 * @param context
	 * @param interval
	 *            either per week or per day
	 * @return amt, amount to spend per interval to keep balance afloat
	 */
	public static String getProjectedAverageSpending(Context context,
			int interval) {

		double amt = 0.0;

		// get DBA balance from shared prefs
		String mKey = context.getString(R.string.preference_name);
		SharedPreferences mPrefs = context.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);
		mKey = context.getString(R.string.preference_key_balance);
		double remainingDBA = Double.parseDouble(mPrefs.getString(mKey, "0.0"));
		Calendar[] cal = getTermStartEnd();
		int daysInTerm = cal[1].get(Calendar.DAY_OF_YEAR)
				- cal[0].get(Calendar.DAY_OF_YEAR);
		int daysRemaining = daysInTerm - getDaysElapsed(context);
		Log.d("CS65", "Days Remaining in term" + daysRemaining);

		// depending upon interval (either daily or weekly), determine the
		// user's past average spending
		switch (interval) {
		case Globals.AVG_DAILY_SPENDING:
			amt = remainingDBA /= daysRemaining;
			break;
		case Globals.AVG_WEEKLY_SPENDING:
			amt = remainingDBA /= (daysRemaining / 7.0);
			break;
		default:
			break;
		}
		return "$" + String.format("%.2f", amt);
	}

	// uses the current time to figure out the current term!
	public static String getCurrentTerm() {
		long currTime = System.currentTimeMillis();
		String term = "Not a valid term";
		if (Globals.FOURTEEN_SPRING_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FOURTEEN_SUMMER_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_SPRING;
			// Log.d("CS65", "TERM IS: " + "14S");
		} else if (Globals.FOURTEEN_SUMMER_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FOURTEEN_FALL_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_SUMMER;
			// Log.d("CS65", "TERM IS: " + "14X");

		} else if (Globals.FOURTEEN_FALL_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FIFTEEN_WINTER_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_FALL;
			// Log.d("CS65", "TERM IS: " + "14F");

		} else if (Globals.FIFTEEN_WINTER_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FIFTEEN_SPRING_START.getTimeInMillis()) {
			term = Globals.FIFTEEN_WINTER;
			// Log.d("CS65", "TERM IS: " + "15W");

		} else if (Globals.FIFTEEN_SPRING_START.getTimeInMillis() <= currTime) {
			term = Globals.FIFTEEN_SPRING;
			// Log.d("CS65", "TERM IS: " + "15S");

		}

		return term;
	}

	// uses the current time to figure out the current term!
	public static Calendar[] getTermStartEnd() {
		Calendar[] calendarDates = new Calendar[2];
		String term = getCurrentTerm();
		if (term == Globals.FOURTEEN_SPRING) {
			calendarDates[0] = Globals.FOURTEEN_SPRING_START;
			calendarDates[1] = Globals.FOURTEEN_SPRING_END;
			// Log.d("CS65", "TERM IS: " + "14S");
		} else if (term.equals(Globals.FOURTEEN_SUMMER)) {
			calendarDates[0] = Globals.FOURTEEN_SUMMER_START;
			calendarDates[1] = Globals.FOURTEEN_SUMMER_END;
			// Log.d("CS65", "TERM IS: " + "14X");

		} else if (term.equals(Globals.FOURTEEN_FALL)) {
			calendarDates[0] = Globals.FOURTEEN_FALL_START;
			calendarDates[1] = Globals.FOURTEEN_FALL_END;
			// Log.d("CS65", "TERM IS: " + "14F");

		} else if (term.equals(Globals.FIFTEEN_WINTER)) {
			calendarDates[0] = Globals.FIFTEEN_WINTER_START;
			calendarDates[1] = Globals.FIFTEEN_WINTER_END;
			// Log.d("CS65", "TERM IS: " + "15W");

		} else if (term.equals(Globals.FIFTEEN_SPRING)) {
			calendarDates[0] = Globals.FIFTEEN_SPRING_START;
			calendarDates[1] = Globals.FIFTEEN_SPRING_END;
			// Log.d("CS65", "TERM IS: " + "15S");

		}

		return calendarDates;
	}

}
