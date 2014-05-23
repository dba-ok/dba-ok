package edu.dartmouth.cs65;

public class Time{
	private int hour;
	private int minute;
	
	public Time(int h, int m){
		hour = h;
		minute = m;
	}
	
	/*
	 * Returns true is this time is after other's time
	 */
	public boolean isPast(Time other){ 
		if (other.getHour() < this.hour){
			return true;
		}
		else if ((other.getHour() == this.hour) && (other.getMinute() < this.minute)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * Returns true if other and this are the same time
	 */
	public boolean isEqual(Time other){
		if ((other.getHour() == this.hour) && (other.getMinute() == this.minute)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * Returns true is this time is before other's time
	 */
	public boolean isBefore(Time other){
		if (other.getHour() > this.hour){
			return true;
		}
		else if ((other.getHour() == this.hour) && (other.getMinute() > this.minute)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
}