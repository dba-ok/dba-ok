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
	 * Adds opening hours for a given day(s)
	 */
	public void addTime(int dayStart, int dayEnd, String[] hours) {
		for (int i = dayStart; i <= dayEnd; i++) {
			times.put(Globals.DAYS[i], hours);
		}
	}

	/*
	 * Given a day and time, is this dining location open?
	 */
	public boolean isOpen(int currHour, int currMinute, int day) { //
		String[] openHours = times.get(Globals.DAYS[day]);
		Time current = new Time(currHour, currMinute);
		boolean isOpen = false;

		// Iterate through all opening times
		for (String hours : openHours) {
			String[] split_hours = hours.split("-");
			Time start = convertStringToTime(split_hours[0]);
			Time end = convertStringToTime(split_hours[1]);

			
			Log.d("CS65", "Start " + start.getHour() + ":" + start.getMinute());
			Log.d("CS65", "End" + end.getHour() + ":" + end.getMinute());
			
			/*Log.d("CS65", "current isPast start? " + current.isPast(start));
			Log.d("CS65", "current is equal start? " + current.isEqual(start));
			Log.d("CS65", "current is before end? " + current.isBefore(end));
			*/
			Log.d("CS65","Time is in interval?? " + start.isInInterval(start, end, current));
			if( start.isInInterval(start,end,current)){
				isOpen = true;
				break;
			}}
		return isOpen;
			
			/*
			if ((current.isPast(start) || current.isEqual(start))
					&& current.isBefore(end)) {
				isOpen = true;
				break;
			}
		}
		Log.d("CS65", "value of isOpen + " + isOpen);
		return isOpen;
		*/
	}

	/*
	 * Parses "x:xx am" String into a Time object
	 */
	private Time convertStringToTime(String s) {
		String[] splitWhiteSpace, splitColon;
		String AMPM;
		int hour, minute;
		Time time;

		Log.d("CS65", "error Time was " + s);
		splitWhiteSpace = s.split(" ");
		AMPM = splitWhiteSpace[1];

		splitColon = splitWhiteSpace[0].split(":");
		hour = Integer.parseInt(splitColon[0]);
		if (splitColon[1].equals("00")) {
			minute = 0;
		} else {
			minute = Integer.parseInt(splitColon[1]);
		}
		// Convert to military time
		if (AMPM.equals("pm")) {
			hour += MILITARY_CONVERSION;
		}

		time = new Time(hour, minute);

		return time;
	}

	public String getName() {
		return name;
	}

}