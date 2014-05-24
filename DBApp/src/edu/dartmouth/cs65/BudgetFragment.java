package edu.dartmouth.cs65;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class BudgetFragment extends Fragment implements OnItemSelectedListener {
	private Context context;
	private TextView pastAvg;
	private TextView futureAvg;
	private TextView pastInterval;
	private TextView futureInterval;


	public BudgetFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		context = this.getActivity();

		View view = inflater.inflate(R.layout.budget, container, false);
		pastAvg = (TextView) view.findViewById(R.id.UserAvgNum);
		futureAvg = (TextView) view.findViewById(R.id.IdealAvgNum);
		pastInterval = (TextView) view.findViewById(R.id.pastInterval);
		futureInterval =  (TextView) view.findViewById(R.id.futureInterval);
		
		// set up spinner and get 
		Spinner intervalSpinner = (Spinner) view
				.findViewById(R.id.intervalSpinner);
		intervalSpinner.setOnItemSelectedListener(this);
		int interval = intervalSpinner.getSelectedItemPosition();

		// get avg past and future spending
		setTextFields(interval);
		
		return view;

	}
	
	// set text fields with appropriate budget info
	public void setTextFields(int interval){
		String past = Utils.getPastAverageSpending(context, interval);
		String future = Utils.getProjectedAverageSpending(context, interval);

		pastAvg.setText(past);
		futureAvg.setText(future);
		
		// show either "/day" or "/week" depending upon user's choice
		switch(interval){
		case Globals.AVG_WEEKLY_SPENDING:
			pastInterval.setText("/week");
			futureInterval.setText("/week");
			break;
		case Globals.AVG_DAILY_SPENDING:
			pastInterval.setText("/day");
			futureInterval.setText("/day");

		}
	}

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
