package com.example.swiping;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {
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
}
