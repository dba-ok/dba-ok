/**
 * DBA-OK
 * 
 * This file defines the BudgetFragment class. The BudgetFragment class displays the user's DBA health thermometer, similar to a fund-raising 
 * thermometer, where the level of green is proportional the the ratio of the user's spent amount to initial DBA balance. 
 * In addition, the UI shows the user's past average spending per interval and the average ideal amount they should spend per interval--weekly or daily, 
 * as selected by the user with a Spinner widget. 
 */
package edu.dartmouth.cs65;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class BudgetFragment extends Fragment implements OnItemSelectedListener {
	private Context context;
	private TextView pastAvg;
	private TextView futureAvg;
	private TextView pastInterval;
	private TextView futureInterval;
	private ImageView progressBar;
	private final int TOP_PROGRESS_LEVEL = 9900;

	public BudgetFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = this.getActivity();
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.budget, container, false);

		// get references to TextViews and ImageView
		pastAvg = (TextView) view.findViewById(R.id.UserAvgNum);
		futureAvg = (TextView) view.findViewById(R.id.IdealAvgNum);
		pastInterval = (TextView) view.findViewById(R.id.pastInterval);
		futureInterval = (TextView) view.findViewById(R.id.futureInterval);
		progressBar = (ImageView) view.findViewById(R.id.progressThermometer);

		// get reference to Spinner, set up listener, and determine
		// user-selected interval (weekly vs. daily)
		Spinner intervalSpinner = (Spinner) view
				.findViewById(R.id.intervalSpinner);
		intervalSpinner.setOnItemSelectedListener(this);
		int interval = intervalSpinner.getSelectedItemPosition();

		// get avg past and future spending, using user-provided interval
		setTextFields(interval);

		return view;

	}

	// set text fields with appropriate budget info
	public void setTextFields(int interval) {
		String past = Utils.getPastAverageSpending(context, interval);
		String future = Utils.getProjectedAverageSpending(context, interval);

		pastAvg.setText(past);
		futureAvg.setText(future);

		// show either "/day" averages or "/week" averages, depending upon the
		// value of interval
		switch (interval) {
		case Globals.AVG_WEEKLY_SPENDING:
			pastInterval.setText("/week");
			futureInterval.setText("/week");
			break;
		case Globals.AVG_DAILY_SPENDING:
			pastInterval.setText("/day");
			futureInterval.setText("/day");
			break;
		}

		progressBar.setImageResource(R.drawable.dba_progress);

		// get current and initial dba balance from SharedPrefs
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = context.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);
		mKey = getString(R.string.preference_key_balance);
		Double balance = Double.parseDouble(mPrefs.getString(mKey, "0.0"));
		mKey = getString(R.string.preference_key_dba_initial);
		Double initial = Double.parseDouble(mPrefs.getString(mKey, "920.00"));

		// Adjust the height of the green bar in the thermometer by adjusting
		// the drawable's level
		int level = (int) ((initial - balance) / initial * TOP_PROGRESS_LEVEL);
		progressBar.setImageLevel(level);
		progressBar.getDrawable().setLevel(level);

	}

	/**
	 * When a Spinner item is selected (to change the interval), update the UI by resetting the TextViews
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		setTextFields(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
