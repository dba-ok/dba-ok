/**
 * DBA-OK
 * 
 * This file defines a class for different dining locations. In particular, it saves the name of the dining location and its hours
 * of operation according to the day of the week. 
 * 
 * In addition, it has a useful 'isOpen' function that returns true or false (the location is or isn't open) based on a given time.
 * 
 */

package edu.dartmouth.cs65;

import java.util.HashMap;

import android.util.Log;

public class DiningLocation {
	private String name;
	private HashMap<String, String[]> times;
	private final static int MILITARY_CONVERSION = 12;

	public DiningLocation(String n) {
		name = n;
		times = new HashMap<String, String[]>();
	}

	public HashMap<String, String[]> getHoursMap() {
		return times;
	}

	/*
	 * Adds opening hours for a given time range. For example, addTime(0, 3, hours) would add the String[] hours
	 * as values for keys Sunday - Wednesday. 
	 */
	public void addTime(int dayStart, int dayEnd, String[] hours) {
		for (int i = dayStart; i <= dayEnd; i++) {
			times.put(Globals.DAYS[i], hours);
		}
	}

	/*
	 * Given a day and time, is this dining location open? Return true
	 * for open, false for closed. 
	 */
	public boolean isOpen(int currHour, int currMinute, int day) { 
		String[] openHours = times.get(Globals.DAYS[day]);
		Time current = new Time(currHour, currMinute);
		boolean isOpen = false;

		// Iterate through all opening times
		for (String hours : openHours) {
			String[] split_hours = hours.split("-");
			Time start = convertStringToTime(split_hours[0]);
			Time end = convertStringToTime(split_hours[1]);

			if (start.isInInterval(start, end, current)) {
				isOpen = true;
				break;
			}
		}
		return isOpen;
	}

	/*
	 * Parses "x:xx am" String into a Time object
	 */
	private Time convertStringToTime(String s) {
		String[] splitWhiteSpace, splitColon;
		String AMPM;
		int hour, minute;
		Time time;
		
		//Get AM or PM
		splitWhiteSpace = s.split(" ");
		AMPM = splitWhiteSpace[1];

		//Split hours and minutes
		splitColon = splitWhiteSpace[0].split(":");
		hour = Integer.parseInt(splitColon[0]);
		
		if (splitColon[1].equals("00")) { 
			minute = 0;
		} 
		else {
			minute = Integer.parseInt(splitColon[1]);
		}
		
		// Convert to military time
		if (AMPM.equals("pm") && hour != 12) {
			hour += MILITARY_CONVERSION;
		} 
		else if (AMPM.equals("am") && hour == 12) {
			hour = 0;
		}

		time = new Time(hour, minute);

		return time;
	}

	public String getName() {
		return name;
	}

}