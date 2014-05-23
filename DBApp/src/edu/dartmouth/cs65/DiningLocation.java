package edu.dartmouth.cs65;

import java.util.HashMap;

public class DiningLocation{
	private String name;
	private HashMap<String, String[]> times;
	private final static String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private final static int MILITARY_CONVERSION = 12;

	public DiningLocation(String n){
		name = n;
		times = new HashMap<String, String[]>();
	}
	
	public void addTime(int dayStart, int dayEnd, String[] hours){
		for (int i = dayStart; i <= dayEnd; i++){
			times.put(DAYS[i], hours);
		}
	}

	public boolean isOpen(int currHour, int currMinute, int day){ //
		String[] openHours = times.get(DAYS[day]);
		Time current = new Time(currHour, currMinute);
		boolean isOpen = false;
		
		//for each time range, you need to make a Time object for start and end and compare with current. 
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