/**
 * DBA-OK
 * 
 * This file contains all Dartmouth dining locations saved as DiningLocation objects. It defines their hours of operations, which is
 * provided manually. It can also return an ArrayList of open locations or closed locations, using the 'isOpen' function in
 * the DiningLocation class.
 */

package edu.dartmouth.cs65;

import java.util.ArrayList;

import android.util.Log;

public class DiningLocations{
	private ArrayList<DiningLocation> locations;
	
	public DiningLocations(){
		locations = new ArrayList<DiningLocation>();
		populateArrayList();
	}
	
	/*
	 * Iterates through all DiningLocation objects and saves the ones that
	 * are currently open in an ArrayList.
	 */
	public ArrayList<DiningLocation> getOpenLocations(int hour, int minute, int day){
		ArrayList<DiningLocation> open = new ArrayList<DiningLocation>();
		
		for (DiningLocation location: locations){
			if (location.isOpen(hour, minute, day)){
				open.add(location);
			}
		}
		
		return open;
	}
	
	/*
	 * Iterates through all DiningLocation objects and saves the ones that
	 * are currently closed in an ArrayList.
	 */
	public ArrayList<DiningLocation> getClosedLocations(int hour, int minute, int day){
		ArrayList<DiningLocation> closed = new ArrayList<DiningLocation>();
		
		for (DiningLocation location: locations){
			if (!location.isOpen(hour, minute, day)){
				closed.add(location);
			}
		}
		
		return closed;
	}
	/*
	 * Add all DiningLocation objects to an ArrayList
	 */
	private void populateArrayList(){
		locations.add(addFiftyThree());
		locations.add(addCollis());
		locations.add(addCollisMarket());
		locations.add(addNovack());
		locations.add(addCourtyardCafe());
		locations.add(addKAF());
		locations.add(addSnackBar());
	}
	
	/*
	 * Defines the hours of operation for '53 Commons 
	 */
	private DiningLocation addFiftyThree(){
		DiningLocation fiftyThree = new DiningLocation("53 Commons");
		String[] mondayFridayHours = {"7:30 am -10:30 am ", "11:00 am -3:00 pm", "5:00 pm -8:30 pm"};
		String[] saturdayHours = {"8:00 am -10:30 am", "11:00 am -2:30 pm", "5:00 pm -8:30 pm"};
		String[] sundayHours = {"8:00 am -2:30 pm", "5:00 pm -8:30 pm"};
				
		fiftyThree.addTime(1, 5, mondayFridayHours);
		fiftyThree.addTime(6, 6, saturdayHours);
		fiftyThree.addTime(0, 0, sundayHours);
		return fiftyThree;
	}
	
	/*
	 * Defines the hours of operation for Collis Cafe, which includes Late Night Collis
	 */
	private DiningLocation addCollis(){
		DiningLocation collis = new DiningLocation("Collis Cafe");
		String[] mondayFridayHours = {"7:00 am -8:00 pm","9:30 pm-1:30 am"};
		String[] weekendHours = {"9:30 pm -2:00 am"};
		
		collis.addTime(0, 0, weekendHours);
		collis.addTime(1, 5, mondayFridayHours);
		collis.addTime(6,6, weekendHours);
		return collis;
	}
	
	/*
	 * Defines the hours of operation for Collis Market
	 */
	private DiningLocation addCollisMarket(){
		DiningLocation collisMarket = new DiningLocation("Collis Market");
		String[] mondayFridayHours = {"11:00 am -11:30 pm"};
		String[] saturdaySundayHours = {"12:00 pm -11:30 pm"};
		
		collisMarket.addTime(0, 0, saturdaySundayHours);
		collisMarket.addTime(1, 5, mondayFridayHours);
		collisMarket.addTime(6, 6, saturdaySundayHours);
		return collisMarket;
	}
	
	/*
	 * Defines the hours of operation for Novack
	 */
	private DiningLocation addNovack(){
		DiningLocation novack = new DiningLocation("Novack");
		String[] mondayFridayHours = {"7:30 am -2:00 am"};
		String[] saturdayHours = {"1:00 pm -2:00 am"};
		String[] sundayHours = {"11:00 am -11:30 pm"};
		
		novack.addTime(0, 0, sundayHours);
		novack.addTime(1, 5, mondayFridayHours);
		novack.addTime(6, 6, saturdayHours);
		return novack;
	}
	
	/*
	 * Defines the hours of operation for Courtyard Cafe in the Hopkins Center
	 */
	private DiningLocation addCourtyardCafe(){
		DiningLocation courtyard = new DiningLocation("Courtyard Cafe");
		String[] allHours = {"11:00 am -12:30 am"};
		
		courtyard.addTime(0, 6, allHours);
		return courtyard;
	}
	
	/*
	 * Defines the hours of operation for King Arthur Flour in Baker library
	 */
	private DiningLocation addKAF(){
		DiningLocation KAF = new DiningLocation("King Arthur Flour");
		String[] mondayFridayHours = {"8:00 am -8:00 pm"};
		String[] saturdaySundayHours = {"10:00 am -8:00 pm"};
		
		KAF.addTime(0, 0, saturdaySundayHours);
		KAF.addTime(1, 5, mondayFridayHours);
		KAF.addTime(6, 6, saturdaySundayHours);
		return KAF;
	}
	
	/*
	 * Defines the hours of operation for the East Wheelock snackbar
	 */
	private DiningLocation addSnackBar(){
		DiningLocation snackBar = new DiningLocation("EW Snack Bar");
		String[] mondayFridayHours = {"8:00 am -11:00 am","8:00 pm -2:00 am"};
		String[] saturdaySundayHours = {"8:00 pm-2:00 am"};
		
		snackBar.addTime(0,0, saturdaySundayHours);
		snackBar.addTime(1, 5, mondayFridayHours);
		snackBar.addTime(6, 6, saturdaySundayHours);
		return snackBar;
	}
}