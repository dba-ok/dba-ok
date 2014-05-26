/**
 * DBA-OK
 * 
 * This file defines the BalanceFragment class. The BalanceFragment class displays the user's remaining meal swipes and DBA balance.
 * There is a refresh button that calls a function in MainActivity, which refreshes the swipe, balance, and transaction history information 
 * from the ManageMyId website. The UI is dynamically reset to reflect the new data.
 */
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
		context = this.getActivity();

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.balance, container, false);

		// get the refresh button reference and set up the click listener
		refreshButton = (Button) view.findViewById(R.id.refresh);
		refreshButton.setOnClickListener(this);

		// get references to the TextViews
		swipeText = (TextView) view.findViewById(R.id.SwipeNumber);
		dbaText = (TextView) view.findViewById(R.id.DBAAmount);

		// populate the TextViews with the newest swipe/balance information
		setText();

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// when refresh button is clicked, disable the button and call
		// manageMyIDinBackground() on the context
		// this will refresh the swipe/balance information
		case R.id.refresh:
			refreshButton.setEnabled(false);
			((MainActivity) context).manageMyIDInBackground();
			break;
		default:
			break;
		}
	}

	/**
	 * This function retrieves the most up-to-date balance and swipe information from SharedPreferences
	 * The UI is populated with this new information, and the refresh button is enabled. 
	 */
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
