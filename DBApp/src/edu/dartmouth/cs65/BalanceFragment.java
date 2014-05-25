package edu.dartmouth.cs65;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BalanceFragment extends Fragment  {
	private Context context; 
	
	public BalanceFragment(){
		
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	
    	context = this.getActivity();
        View view =  inflater.inflate(R.layout.balance, container, false);

        // get balance/swipe info from shared prefs
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = context
				.getSharedPreferences(mKey, Context.MODE_PRIVATE);
		mKey = getString(R.string.preference_key_balance);
		String balance = mPrefs.getString(mKey, "0.0");
		mKey = getString(R.string.preference_key_swipes);
		String swipes = mPrefs.getString(mKey, "5");
		
		TextView swipeText = (TextView) view.findViewById(R.id.SwipeNumber);
		TextView dbaText = (TextView) view.findViewById(R.id.DBAAmount);
		
		swipeText.setText(swipes);
		dbaText.setText("$" + balance);
		
		return view;
    }
    
    
}

