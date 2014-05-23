package edu.dartmouth.cs65;

import java.util.ArrayList;

public class DiningLocations{
	private ArrayList<DiningLocation> locations;
	
	public DiningLocations(){
		locations = new ArrayList<DiningLocation>();
		populateArrayList();
	}
	
	public ArrayList<DiningLocation> getOpenLocations(int hour, int minute, int day){
		ArrayList<DiningLocation> open = new ArrayList<DiningLocation>();
		
		for (DiningLocation location: locations){
			if (location.isOpen(hour, minute, day)){
				open.add(location);
			}
		}
		
		return open;
	}
	
	public ArrayList<DiningLocation> getClosedLocations(int hour, int minute, int day){
		ArrayList<DiningLocation> closed = new ArrayList<DiningLocation>();
		
		for (DiningLocation location: locations){
			if (!location.isOpen(hour, minute, day)){
				closed.add(location);
			}
		}
		
		return closed;
	}
	
	private void populateArrayList(){
		addFiftyThree();
		addCollis();
		addLateNight();
		addCollisMarket();
		addNovack();
		addCourtyardCafe();
		addKAF();
	}
	
	private void addFiftyThree(){
		DiningLocation fiftyThree = new DiningLocation("53 Commons");
		String[] mondayFridayHours = {"7:30 am - 10:30 am", "11:00 am - 3:00 pm", "5:00 pm - 8:30 pm"};
		String[] saturdayHours = {"8:00 am - 10:30 am", "11:00 am - 2:30 pm", "5:00 pm - 8:30 pm"};
		String[] sundayHours = {"8:00 am - 2:30 pm", "5:00 pm - 8:30 pm"};
				
		fiftyThree.addTime(1, 5, mondayFridayHours);
		fiftyThree.addTime(6, 6, saturdayHours);
		fiftyThree.addTime(0, 0, sundayHours);
	}
	
	private void addCollis(){
		DiningLocation collis = new DiningLocation("Collis Cafe");
		String[] allHours = {"7:00 am - 8:00 pm"};
		
		collis.addTime(0, 6, allHours);
	}
	
	private void addLateNight(){
		DiningLocation lateNight = new DiningLocation("Late Night Collis");
		String[] sundayThursdayHours = {"9:30 pm - 1:30 am"};
		String[] fridaySaturdayHours = {"9:30 pm - 2:00 am"};
		
		lateNight.addTime(0, 4, sundayThursdayHours);
		lateNight.addTime(5, 6, fridaySaturdayHours);
	}
	
	private void addCollisMarket(){
		DiningLocation collisMarket = new DiningLocation("Collis Market");
		String[] mondayFridayHours = {"11:00 am - 11:30 pm"};
		String[] saturdaySundayHours = {"12:00 pm - 11:30 pm"};
		
		collisMarket.addTime(1, 5, mondayFridayHours);
		collisMarket.addTime(0, 0, saturdaySundayHours);
		collisMarket.addTime(6, 6, saturdaySundayHours);
	}
	
	private void addNovack(){
		DiningLocation novack = new DiningLocation("Novack");
		String[] mondayFridayHours = {"7:30 am - 2:00 am"};
		String[] saturdayHours = {"1:00 pm - 2:00 am"};
		String[] sundayHours = {"11:00 am - 11:30 pm"};
		
		novack.addTime(1, 5, mondayFridayHours);
		novack.addTime(6, 6, saturdayHours);
		novack.addTime(0, 0, sundayHours);
	}
	
	private void addCourtyardCafe(){
		DiningLocation courtyard = new DiningLocation("Courtyard Cafe");
		String[] allHours = {"11:00 am - 12:30 am"};
		
		courtyard.addTime(0, 6, allHours);
	}
	
	private void addKAF(){
		DiningLocation KAF = new DiningLocation("King Arthur Flour");
		String[] mondayFridayHours = {"8:00 am - 8:00 pm"};
		String[] saturdaySundayHours = {"10:00 am - 8:00 pm"};
		
		KAF.addTime(1, 5, mondayFridayHours);
		KAF.addTime(6, 6, saturdaySundayHours);
		KAF.addTime(0, 0, saturdaySundayHours);
	}
}