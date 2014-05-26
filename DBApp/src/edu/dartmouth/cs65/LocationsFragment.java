/**
 * DBA-OK
 * 
 * This file defines the LocationsFragment class. The LocationsFragment class displays which DDS locations are currently open and which 
 * ones are currently closed, as well as their open hours for the day.
 */
package edu.dartmouth.cs65;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationsFragment extends Fragment {
	private TextView openTextView, closedTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate the fragment layout
		View view = inflater.inflate(R.layout.locations, container, false);

		// get references to text views
		openTextView = (TextView) view.findViewById(R.id.openLocations);
		closedTextView = (TextView) view.findViewById(R.id.closedLocations);

		// populate the TextViews
		setText();

		return view;
	}

	// return the text to fill in closed/open location textViews.
	public String getText(ArrayList<DiningLocation> locations, int today) {
		Calendar c = Calendar.getInstance();
		String text = "";

		// creates a formatted string with the location's names, followed by
		// their open hours
		for (DiningLocation l : locations) {
			String name = l.getName();
			text = text + name + "\n";
			HashMap<String, String[]> hoursMap = l.getHoursMap();
			String[] dailyHours = hoursMap.get(Globals.DAYS[today]);

			for (String block : dailyHours) {
				text = text + "                                " + block;
				text += "\n";
			}
		}
		return text;
	}

	// repopulates the TextViews when the fragment is resumed
	@Override
	public void onResume() {
		super.onResume();
		setText();
	}

	/**
	 * Populate the TextViews with the Open and Closed locations and open hours
	 */
	public void setText() {
		Calendar now = Calendar.getInstance();
		DiningLocations locations = new DiningLocations();
		int today = now.get(Calendar.DAY_OF_WEEK) - 1;

		// if it is before LATEST_CLOSE_TIME on a current day, we treat it as
		// the day before the current day. That is, if it's earlier than
		// LATEST_CLOSE_TIME on Sunday, we treat it as Saturday
		if ((now.get(Calendar.HOUR_OF_DAY)) <= Globals.LATEST_CLOSE_TIME) {
			if (today > 0) {
				today -= 1;
			} else {
				today = 6;
			}
		}

		// get arraylists of the open locations and the closed locations
		ArrayList<DiningLocation> openLocations = locations.getOpenLocations(
				now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), today);
		ArrayList<DiningLocation> closedLocations = locations
				.getClosedLocations(now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE), today);

		String openText = getText(openLocations, today);
		String closedText = getText(closedLocations, today);

		// set the text with the appropriate open and closed information
		openTextView.setText(openText);
		closedTextView.setText(closedText);

	}

}
