package edu.dartmouth.cs65;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swiping.R;

public class WelcomeActivity extends Activity  {

	/*
	 * public WelcomeActivity() { }
	 */

	private EditText email;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		email = (EditText) findViewById(R.id.EnterEmail);
		password =  (EditText) findViewById(R.id.EnterPassword);

	}
	

	public void onSignInClicked(View v) {
		// save the user profile to shared preferences and save photo to
		// internal storage and finish the activity
		Toast.makeText(this, "sign in pressed!", Toast.LENGTH_SHORT).show();

		Log.d("CS65", "Sign in Button Pressed");
		String mKey = getString(R.string.preference_name);

		SharedPreferences mPrefs = this.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);

		// PreferenceManager.getDefaultSharedPreferences(getActivity());

		SharedPreferences.Editor mEditor = mPrefs.edit();
		//mEditor.clear();

		mKey = getString(R.string.preference_key_welcome_screen);
		mEditor.putBoolean(mKey, true);

		mKey = getString(R.string.preference_key_username);
		String mValue = email.getText().toString();
		mEditor.putString(mKey, mValue);

		mKey = getString(R.string.preference_key_password);
		mValue = password.getText().toString();
		mEditor.putString(mKey, mValue);
		// mEditor.apply();
		mEditor.commit();
		
		finish();
	    Intent intent  = new Intent(this,MainActivity.class );
	    intent.putExtra("username", email.getText().toString());
	    intent.putExtra("password", password.getText().toString());
	    //finish();
	}
	
	
}
