package edu.dartmouth.cs65;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import edu.dartmouth.cs65.scraper.ManageMyIDScraper;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * Adapter for the Pager
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * Necessary for swiping between fragments
	 */
	ViewPager mViewPager;
	ActionBar mActionBar;
	final String PREF_TAG = "PrefFrag";
	final String WELCOME_TAG = "WelcomeFrag";
	private String TAG = "CS65";

	private String mUsername;
	private String mPassword;
	private Context context;
	private BalanceFragment bal;
	
	private static final int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;

		// set up the actionbar and view pager
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// access the shared prefs
		String mKey = getString(R.string.preference_name);

		SharedPreferences mPrefs = this
				.getSharedPreferences(mKey, MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();

		// if there is nothing in shared prefs for balance, swipes, initial bal,
		// set manually.
		mKey = getString(R.string.preference_key_balance);
		if (mPrefs.getString(mKey, "").equals("")) {
			mEditor.putString(mKey, "0.0");
		}
		mKey = getString(R.string.preference_key_swipes);
		if (mPrefs.getString(mKey, "").equals("")) {
			mEditor.putString(mKey, "0");
		}
		mKey = getString(R.string.preference_key_dba_initial);
		if (mPrefs.getString(mKey, "").equals("")) {
			mEditor.putString(mKey, "920.00");
		}
		mEditor.commit();

		mKey = getString(R.string.preference_key_username);
		mUsername = mPrefs.getString(mKey, "");
		mKey = getString(R.string.preference_key_password);
		mPassword = mPrefs.getString(mKey, "");
		mKey = getString(R.string.preference_key_welcome_screen);
		boolean welcomeScreenShown = mPrefs.getBoolean(mKey, false);

		Log.d(TAG, "welcomeScreenShown =" + welcomeScreenShown);

		this.setUpActionBar();
		/*
		 * Bundle extras = getIntent().getExtras(); if (extras != null) {
		 * mEditor.putString(getString(R.string.preference_key_username),
		 * extras.getString("username"));
		 * mEditor.putString(getString(R.string.preference_key_password),
		 * extras.getString("password")); mEditor.commit(); }
		 */
		Log.d(TAG, " mUsername not blank: " + mUsername);
		Log.d(TAG, "mPassword not blank: " + mPassword);

		if (!welcomeScreenShown
				|| (mUsername.equals("") || mPassword.equals(""))) {
			mEditor.putBoolean(mKey, true); // welcome screen has been shown!
			mEditor.commit();
			showWelcome();
		}

		if (!mUsername.equals("") && !mPassword.equals("")) {
			manageMyIDInBackground();
		}

	}


	/***
	 * On resume, cancel all notifications!
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Clear the Notification Bar
		NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancelAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showWelcome() {
		// this.removePages();
		Intent i = new Intent(this, WelcomeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	public void startSettings() {
		this.removePages();
		SettingsFragment prefsFragment = new SettingsFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, prefsFragment,
				PREF_TAG);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "MAIN ACTIVITY: onBackPressed");
		try {
			final SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager()
					.findFragmentByTag(PREF_TAG);
			// for either fragment, if back button is pressed, we want to set up
			// the
			// action bar!
			if (fragment.allowBackPressed()) {
				super.onBackPressed();
				this.setUpActionBar();
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.onBackPressed();
		}
	}

	/**
	 * Sets up action bar and swipey-swipe interface :)
	 */
	public void setUpActionBar() {

		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						mActionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			mActionBar.addTab(mActionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			this.startSettings();
			return true;
		default:
			return true;
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * This method removes the pages from ViewPager
	 */
	public void removePages() {
		mActionBar.removeAllTabs();

		// call to ViewPage to remove the pages
		mViewPager.removeAllViews();
		// mFragments.clear();

		// make this to update the pager
		mViewPager.setAdapter(null);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new Fragment();

			switch (position) {
			case 0:
				fragment = new BalanceFragment();
				bal = (BalanceFragment) fragment;
				break;
			case 1:
				fragment = new DailyDishFragment();
				break;
			case 2:
				fragment = new LocationsFragment();
				break;
			case 3:
				fragment = new BudgetFragment();
				break;
			case 4:
				fragment = new MyStatsFragment();
			default:
				break;

			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
				// case 5:
				// return getString(R.string.title_section6).toUpperCase(l);
			}
			return null;
		}
	}

	public void manageMyIDInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String mKey = getString(R.string.preference_name);
				TransactionEntryDbHelper dbHelper = new TransactionEntryDbHelper(
						context);
				SharedPreferences mPrefs = context.getSharedPreferences(mKey,
						MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mPrefs.edit();

				mKey = getString(R.string.preference_key_username);
				mUsername = mPrefs.getString(mKey, "");
				mKey = getString(R.string.preference_key_password);
				mPassword = mPrefs.getString(mKey, "");

				ManageMyIDScraper scraper = new ManageMyIDScraper(mUsername,
						mPassword);
				Log.d(TAG,
						"scraper's loggedIn boolean value is:"
								+ scraper.isLoggedIn());
				if (!scraper.isLoggedIn()) {
					String success = "invalid data";
					return success;
				}
				mKey = getString(R.string.preference_key_balance);

				String balance = "0.0";
				String swipes = "0";
				String totalDBA = "920.00";
				try {
					balance = scraper.getDBABalance();
					swipes = scraper.getSwipeBalance();
					totalDBA = scraper.getTotalDBA();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mEditor.putString(mKey, balance);
				mKey = getString(R.string.preference_key_swipes);
				mEditor.putString(mKey, swipes);
				mKey = getString(R.string.preference_key_dba_initial);
				mEditor.putString(mKey, totalDBA);
				mEditor.commit();
				String success = "";
				// get the transaction history, delete all rows currently in db
				// and replace with new entries from the web
				Calendar[] startEndDates = Utils.getTermStartEnd();
				try {
					scraper.getTransactionHistoryPage(startEndDates[0],
							startEndDates[1]);
					ArrayList<TransactionEntry> entryList = scraper
							.getTransactionHistory();
					Log.d("ASYNCTASK", "Size of entryList: " + entryList.size());
					dbHelper.deleteAllEntries();
					dbHelper.insertEntryList(entryList);
					success = "YAY!";
				} catch (Exception e) {
					e.printStackTrace();
					success = "boooo error!";
				}
				return success;
			}

			@Override
			protected void onPostExecute(String success) {
				if (success.equals("YAY!")) {
					Toast.makeText(getApplicationContext(),
							"Data has been updated!", Toast.LENGTH_SHORT)
							.show();
					bal.setText();

				} else if (success.equals("invalid data")) {
					Toast.makeText(getApplicationContext(),
							"Invalid username and/or password.",
							Toast.LENGTH_SHORT).show();
					logoutUser();
					// showWelcome();
				}
				Log.d("CS65", "Executing");
			}
		}.execute(null, null, null);
	}

	// refresh data when refresh is clicked
	public void onRefreshClicked(View v) {
		manageMyIDInBackground();

	}

	public void onLogoutClicked(View v) {
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = this.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor mEditor = mPrefs.edit();
		mKey = getString(R.string.preference_logged_in);
		mEditor.putBoolean(mKey, false);
		mEditor.commit();

		logoutUser();
		// showWelcome();
	}

	// logs the user out.
	// overwrites username and password in shared prefs
	private void logoutUser() {
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = this.getSharedPreferences(mKey,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();
		mKey = getString(R.string.preference_key_username);
		mEditor.putString(mKey, "");
		mKey = getString(R.string.preference_key_password);
		mEditor.putString(mKey, "");
		mEditor.commit();
		showWelcome();
	}

	@Override
	public void onDestroy() {
		// logoutUser();
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == REQUEST_CODE) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	Toast.makeText(getApplicationContext(),
						"in onActivityResult!", Toast.LENGTH_SHORT)
						.show();
	        	Log.d("CS65","in onActivityResult, resultOK!");
	        	manageMyIDInBackground();
	        }
	    }
	}
}