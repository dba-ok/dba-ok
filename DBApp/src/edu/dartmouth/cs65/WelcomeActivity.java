/**
 * DBA-OK
 * 
 * This file defines the welcome screen that pops up when the user first uses DBA-OK or if the user logs out. The file also
 * saves the username and password entered by the user on "Sign In" clicked. 
 */
package edu.dartmouth.cs65;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
    private EditText email;
    private EditText password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        //Get the email and password EditText objects from the layout
        email = (EditText) findViewById(R.id.EnterEmail);
        password =  (EditText) findViewById(R.id.EnterPassword);
        
        //Let the user navigate to the ManageMyID website if they don't have an account
        TextView textView =(TextView)findViewById(R.id.GetManageMyId);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://dartmouth.managemyid.com/student/login.php'> Get one here </a>";
        textView.setText(Html.fromHtml(text));

    }

    public void onSignInClicked(View v) {
        // Save the user profile to shared preferences and save photo to
        // internal storage and finish the activity
        String mKey = getString(R.string.preference_name);

        SharedPreferences mPrefs = this.getSharedPreferences(mKey,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor mEditor = mPrefs.edit();

        mKey = getString(R.string.preference_key_welcome_screen);
        mEditor.putBoolean(mKey, true);
        
        //Save entered email to SharedPreferences
        mKey = getString(R.string.preference_key_username);
        String mValue = email.getText().toString();
        mEditor.putString(mKey, mValue);

        //Save entered password to SharedPreferences
        mKey = getString(R.string.preference_key_password);
        mValue = password.getText().toString();
        mEditor.putString(mKey, mValue);

        mEditor.commit();
        
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish(); 
    }
    
    @Override
	public void onBackPressed() {
    	//Do nothing if back button is pressed
    }

}
