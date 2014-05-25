package edu.dartmouth.cs65;

import java.util.HashMap;

public class DiningLocation{
	private String name;
	private HashMap<String, String[]> times;
	private final static int MILITARY_CONVERSION = 12;

	public DiningLocation(String n){
		name = n;
		times = new HashMap<String, String[]>();
	}
	
	public HashMap<String, String[]> getHoursMap(){
		return times;
	}
	/*
	 * Adds opening hours for a given day(s)
	 */
	public void addTime(int dayStart, int dayEnd, String[] hours){
		for (int i = dayStart; i <= dayEnd; i++){
			times.put(Globals.DAYS[i], hours);
		}
	}

	/*
	 * Given a day and time, is this dining location open?
	 */
	public boolean isOpen(int currHour, int currMinute, int day){ //
		String[] openHours = times.get(Globals.DAYS[day]);
		Time current = new Time(currHour, currMinute);
		boolean isOpen = false;
		
		//Iterate through all opening times
		for (String hours: openHours){
			Time start = convertStringToTime(hours.split("-")[0]);
			Time end = convertStringToTime(hours.split("-")[1]);
			
			if ((current.isPast(start) || current.isEqual(start)) && current.isBefore(end)){
				isOpen = true;
				break;
			}
		}
		
		return isOpen;
	}
	
	/*
	 * Parses "x:xx am" String into a Time object
	 */
	private Time convertStringToTime(String s){
		String[] splitWhiteSpace, splitColon;
		String AMPM;
		int hour, minute;
		Time time;
		
		splitWhiteSpace = s.split(" ");
		AMPM = splitWhiteSpace[1]; 
		
		splitColon = splitWhiteSpace[0].split(":");
		hour = Integer.parseInt(splitColon[0]);
		minute = Integer.parseInt(splitColon[1]);
		
		//Convert to military time
		if (AMPM.equals("pm")){
			hour += MILITARY_CONVERSION;
		}
		
		time = new Time(hour, minute);
		
		return time;	
	}

	public String getName(){
		return name;
	}
	
}