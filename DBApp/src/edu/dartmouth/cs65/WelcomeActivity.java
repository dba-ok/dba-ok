package edu.dartmouth.cs65;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.dartmouth.cs65.R;

public class WelcomeActivity extends Activity{
    /*
     * public WelcomeActivity() { }
     */

    private EditText email;
    private EditText password;
    private static final String TAG = "WelcomeActvitiy.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        email = (EditText) findViewById(R.id.EnterEmail);
        password =  (EditText) findViewById(R.id.EnterPassword);
        
        TextView textView =(TextView)findViewById(R.id.GetManageMyId);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://dartmouth.managemyid.com/student/login.php'> Get one here </a>";
        textView.setText(Html.fromHtml(text));

    }

    public void onSignInClicked(View v) {
        // save the user profile to shared preferences and save photo to
        // internal storage and finish the activity
        Toast.makeText(getApplicationContext(), "sign in pressed!", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(getApplicationContext(), "Username saved: " + mValue,
                Toast.LENGTH_SHORT).show();

        mKey = getString(R.string.preference_key_password);
        mValue = password.getText().toString();
        mEditor.putString(mKey, mValue);
        
        String pwd =""; 
        for(int i = 0; i < mValue.length(); i++){
            pwd+="*";
        }
        
        Toast.makeText(getApplicationContext(), "Password saved: " + pwd,
                Toast.LENGTH_SHORT).show();

        mEditor.commit();
        

        finish();
    }
    
    @Override
	public void onBackPressed() {
    	String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = this.getSharedPreferences(mKey, Context.MODE_PRIVATE);

        mKey = getString(R.string.preference_logged_in);
        boolean loggedIn = mPrefs.getBoolean(mKey, false);
        
        if (loggedIn){
        	super.onBackPressed();
        }
        
    }
    
    @Override
    public void onDestroy(){
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = this.getSharedPreferences(mKey, Context.MODE_PRIVATE);
        
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mKey = getString(R.string.preference_logged_in);
        
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_logged_in);
        
        if (checkbox.isChecked()){
            mEditor.putBoolean(mKey, true);
            Log.d(TAG, "User checked save username and password");
        }
        else{
            mEditor.putBoolean(mKey, false);
            Log.d(TAG, "User did not check save username and password");
        }
        
        mEditor.commit();
        super.onDestroy();
    }

}
