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
		} else if ((other.getHour() == this.hour)
				&& (other.getMinute() < this.minute)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInInterval(Time startInterval, Time endInterval, Time other) {
		int startHour = startInterval.getHour();
		int startMinute = startInterval.getMinute();

		int endHour = endInterval.getHour();
		int endMinute = endInterval.getMinute();

		int intervalLength = 0;
		int startInMinutes = startHour * 60 + startMinute;
		int endInMinutes;
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
		if(otherHour < Globals.LATEST_CLOSE_TIME){
			otherInMinutes = (otherHour  + 24) * 60 + otherMinute;
		}
		else{
			otherInMinutes = otherHour*60 + otherMinute;
		}
		
		return (startInMinutes <= otherInMinutes && otherInMinutes <= endInMinutes);
		
		//int otherInMinutes = 

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