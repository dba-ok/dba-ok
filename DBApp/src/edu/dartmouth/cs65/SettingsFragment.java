package edu.dartmouth.cs65;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	private Context context;

	public SettingsFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get the context of this fragment
		context = getActivity();

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.layout.preferences);
	}
	
	public boolean allowBackPressed(){
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences P, String K) {
		//String mKey = getString(R.string.preference_name);
		//SharedPreferences mPrefs = this.getActivity().getSharedPreferences(mKey,Context.MODE_PRIVATE);
		//SharedPreferences.Editor mEditor = mPrefs.edit();
		//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		
		Context context = this.getActivity();
		Intent i = new Intent(context, NotificationSetter.class);
		i.putExtra("PREF_KEY", K);
		context.sendBroadcast(i);

		/*
		if(K.equals(R.string.closing_kaf_key)){
			
		}
		else if(K.equals(R.string.closing_collis_key)){
			
		}
		else if(K.equals(R.string.closing_foco_key)){
			
		}
		else if(K.equals(R.string.closing_hop_key)){
			
		}
		else if(K.equals(R.string.closing_novack_key)){
			
		}
		// TODO Auto-generated method stub
		
		*/
	}
}
