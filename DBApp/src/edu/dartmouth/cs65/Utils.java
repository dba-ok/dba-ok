package edu.dartmouth.cs65;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

/**
 * Add useful functions
 * 
 * @author kimberlystrauch
 * 
 */

public class Utils {

	// WHERE SPENDING MONEY. RETURN LIST OR HASHMAP WITH DOLLARS SPENT IN EACH
	// LOCATION FOR PIE CHART
	public static ArrayList<Double> getLocationSpending(Context context) {
		ArrayList<Double> locationSpending = new ArrayList<Double>();
		for (int i = 0; i < 10; i++) {
			locationSpending.set(i, 0.0);
		}

		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper
				.fetchAccountEntries(Globals.ACCOUNT_TYPE_DBA);
		Calendar[] cal = getTermStartEnd();
		int firstDay = cal[0].DAY_OF_YEAR;

		int location = 0;
		for (TransactionEntry entry : entryList) {
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

	// HOW MUCH MONEY SPENT PER WEEK... FOR LINE CHART :)
	@SuppressWarnings("static-access")
	public static ArrayList<Double> getWeeklySpending(Context context) {
		ArrayList<Double> weeklySpending = new ArrayList<Double>();
		for (int i = 0; i < 10; i++) {
			weeklySpending.set(i, 0.0);
		}
		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper
				.fetchAccountEntries(Globals.ACCOUNT_TYPE_DBA);
		Calendar[] cal = getTermStartEnd();
		int firstDay = cal[0].DAY_OF_YEAR;

		int week = 0;
		for (TransactionEntry entry : entryList) {
			if (entry.getDateTimeinMillis() < cal[0].getTimeInMillis()) {
				continue;
			}
			// find out week in term and increment the associated arrayList
			// value
			week = (entry.getDateTimeDay() - firstDay) / 7;
			weeklySpending.set(week,
					weeklySpending.get(week) + entry.getAmount());
		}

		return weeklySpending;
	}

	public static int getDaysElapsed(Context context) {

		TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
				context);
		ArrayList<TransactionEntry> entryList = dbHelper
				.fetchAccountEntries(Globals.ACCOUNT_TYPE_DBA);
		Calendar[] cal = getTermStartEnd();
		int firstDay = cal[0].DAY_OF_YEAR;

		// access last entry in entry list
		TransactionEntry lastEntry = entryList.get(entryList.size());
		int lastDay = lastEntry.getDateTimeDay();
		int daysPassed = lastDay - firstDay + 1;
		return daysPassed;
	}

	public double getPastAverageSpending(Context context, int interval) {
		double amt = 0.0;
		ArrayList<Double> weeklySpending = getWeeklySpending(context);
		for (int i = 0; i < weeklySpending.size(); i++) {
			amt += weeklySpending.get(i);
		}

		int numDays = getDaysElapsed(context);
		double weeksPassed = numDays / 7.0;
		
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
		return amt;
	}
	

	// get projected amount for daily or weekly spending in order to stay positive
	public double getProjectedAverageSpending(Context context, int interval){
		double amt = 0.0;
		ArrayList<Double> weeklySpending = getWeeklySpending(context);
		for (int i = 0; i < weeklySpending.size(); i++) {
			amt += weeklySpending.get(i);
		}

		double remainingDBA = Globals.TOTAL_DBA_AMOUNT - amt;
				 
		Calendar[] cal = getTermStartEnd();
		int daysInTerm = cal[1].DAY_OF_YEAR - cal[0].DAY_OF_YEAR;
		
		int daysRemaining = daysInTerm - getDaysElapsed(context);
		
		
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
		return amt;
	}

	// uses the current time to figure out the current term!
	public static String getCurrentTerm() {
		long currTime = System.currentTimeMillis();
		String term = "Not a valid term";
		if (Globals.FOURTEEN_SPRING_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FOURTEEN_SUMMER_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_SPRING;
		} else if (Globals.FOURTEEN_SUMMER_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FOURTEEN_FALL_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_SUMMER;
		} else if (Globals.FOURTEEN_FALL_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FIFTEEN_WINTER_START.getTimeInMillis()) {
			term = Globals.FOURTEEN_FALL;
		} else if (Globals.FIFTEEN_WINTER_START.getTimeInMillis() <= currTime
				&& currTime < Globals.FIFTEEN_SPRING_START.getTimeInMillis()) {
			term = Globals.FIFTEEN_WINTER;
		} else if (Globals.FIFTEEN_SPRING_START.getTimeInMillis() <= currTime) {
			term = Globals.FIFTEEN_SPRING;
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
		} else if (term.equals(Globals.FOURTEEN_SUMMER)) {
			calendarDates[0] = Globals.FOURTEEN_SUMMER_START;
			calendarDates[1] = Globals.FOURTEEN_SUMMER_END;
		} else if (term.equals(Globals.FOURTEEN_FALL)) {
			calendarDates[0] = Globals.FOURTEEN_FALL_START;
			calendarDates[1] = Globals.FOURTEEN_FALL_END;
		} else if (term.equals(Globals.FIFTEEN_WINTER)) {
			calendarDates[0] = Globals.FIFTEEN_WINTER_START;
			calendarDates[1] = Globals.FIFTEEN_WINTER_END;
		} else if (term.equals(Globals.FIFTEEN_SPRING)) {
			calendarDates[0] = Globals.FIFTEEN_SPRING_START;
			calendarDates[1] = Globals.FIFTEEN_SPRING_END;
		}

		return calendarDates;
	}

}
