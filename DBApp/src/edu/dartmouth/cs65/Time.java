package edu.dartmouth.cs65;

public class Time{
	private int hour;
	private int minute;
	
	public Time(int h, int m){
		hour = h;
		minute = m;
	}
	
	//Assumes other is in military time
	public boolean isPast(Time other){ //is this after other's time?
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
	
	public boolean isEqual(Time other){
		if ((other.getHour() == this.hour) && (other.getMinute() == this.minute)){
			return true;
		}
		else{
			return false;
		}
	}
	
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