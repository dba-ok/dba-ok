package edu.dartmouth.cs65;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationsFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	Calendar now = Calendar.getInstance();
        DiningLocations locations = new DiningLocations();
        
        /*
        ArrayList<DiningLocation> openLocations = locations.getOpenLocations(now.HOUR_OF_DAY, now.MINUTE, now.DAY_OF_WEEK);
        ArrayList<DiningLocation> closedLocations = locations.getClosedLocations(now.HOUR_OF_DAY, now.MINUTE, now.DAY_OF_WEEK);
        */
        
        return inflater.inflate(R.layout.locations, container, false);
    }

}

