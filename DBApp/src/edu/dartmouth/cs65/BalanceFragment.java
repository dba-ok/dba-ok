package edu.dartmouth.cs65;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BalanceFragment extends Fragment implements View.OnClickListener {
	private Context context;
	private Button refreshButton;
	private TextView swipeText, dbaText;

	public BalanceFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		context = this.getActivity();
		View view = inflater.inflate(R.layout.balance, container, false);

		// find appropriate views by ID
		refreshButton = (Button) view.findViewById(R.id.refresh);
		refreshButton.setOnClickListener(this);

		swipeText = (TextView) view.findViewById(R.id.SwipeNumber);
		dbaText = (TextView) view.findViewById(R.id.DBAAmount);

		// get balance/swipe info from shared prefs
		/*
		 * String mKey = getString(R.string.preference_name); SharedPreferences
		 * mPrefs = context.getSharedPreferences(mKey, Context.MODE_PRIVATE);
		 * mKey = getString(R.string.preference_key_balance); String balance =
		 * mPrefs.getString(mKey, "0.0"); mKey =
		 * getString(R.string.preference_key_swipes); String swipes =
		 * mPrefs.getString(mKey, "5");
		 * 
		 * 
		 * swipeText.setText(swipes); dbaText.setText("$" + balance);
		 */
		setText();

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refresh:
			Log.d("CS65", "Refresh Button Pressed");
			refreshButton.setEnabled(false);
			((MainActivity) context).manageMyIDInBackground();
			//setText();
			break;
		default:
			Log.d("CS65", "Something clicked");
			break;
		}
	}

	public void setText() {
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = context.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);
		mKey = getString(R.string.preference_key_balance);
		String balance = mPrefs.getString(mKey, "0.0");
		mKey = getString(R.string.preference_key_swipes);
		String swipes = mPrefs.getString(mKey, "5");
		swipeText.setText(swipes);
		dbaText.setText("$" + balance);
		refreshButton.setEnabled(true);
	}
}
