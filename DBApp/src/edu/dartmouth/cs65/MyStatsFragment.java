package edu.dartmouth.cs65;

import edu.dartmouth.cs65.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyStatsFragment extends Fragment {
	
	public MyStatsFragment(){
		
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mystats, container, false);
    }
    
}

