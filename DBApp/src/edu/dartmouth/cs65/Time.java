/**
 * DBA-OK
 * 
 * This file defines the Time class. The Time class contains an hour and minute value and defines several functions that determine
 * whether or not the time stored in the Time class is before, after, or at the same time as another Time object. It also
 * determines whether or not the time stored in the Time object is between an interval of time. 
 */
package edu.dartmouth.cs65;

public class Time {
	private int hour;
	private int minute;

	public Time(int h, int m) {
		hour = h;
		minute = m;
	}

	/*
	 * Returns true is this time is after other's time
	 */
	public boolean isPast(Time other) {
		if (other.getHour() < this.hour) {
			return true;
		} 
		else if ((other.getHour() == this.hour)
				&& (other.getMinute() < this.minute)) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	/*
	 * Returns true if this Time object is between the time of two others Time objects
	 */
	public boolean isInInterval(Time startInterval, Time endInterval, Time other) {
		int startHour = startInterval.getHour();
		int startMinute = startInterval.getMinute();

		int endHour = endInterval.getHour();
		int endMinute = endInterval.getMinute();

		int intervalLength = 0;
		int startInMinutes = startHour * 60 + startMinute;
		int endInMinutes;
		
		//Calculate length of interval
		if (endHour < startHour) {
			endInMinutes = (endHour + 24) * 60 + endMinute;
			intervalLength = endInMinutes - startInMinutes;
		} else {
			endInMinutes = endHour * 60 + endMinute;
			intervalLength = endInMinutes - startInMinutes;
		}
		
		int otherHour = other.getHour();
		int otherMinute = other.getMinute();
		int otherInMinutes = 0;
		
		//Treat this hour as the same day, even if it's between midnight
		//and 3AM (the value of Globals.LATEST_CLOSE_TIME) because that's how
		//meal sqipes are treated
		if(otherHour < Globals.LATEST_CLOSE_TIME){
			otherInMinutes = (otherHour  + 24) * 60 + otherMinute;
		}
		else{
			otherInMinutes = otherHour*60 + otherMinute;
		}
		
		return (startInMinutes <= otherInMinutes && otherInMinutes <= endInMinutes);
	}
	

	/*
	 * Returns true if other and this are the same time
	 */
	public boolean isEqual(Time other) {
		return ((other.getHour() == this.hour) && (other.getMinute() == this.minute));
	}

	/*
	 * Returns true is this time is before other's time
	 */
	public boolean isBefore(Time other) {
		if (other.getHour() > this.hour) {
			return true;
		} else if ((other.getHour() == this.hour)
				&& (other.getMinute() > this.minute)) {
			return true;
		} else {
			return false;
		}
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

}