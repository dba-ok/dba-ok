package edu.dartmouth.cs65;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * // Set up the action bar. // final ActionBar actionBar =
		 * getActionBar(); mActionBar = getActionBar();
		 * mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 * 
		 * // Create the adapter that will return a fragment for each of the //
		 * primary sections of the app mSectionsPagerAdapter = new
		 * SectionsPagerAdapter( getSupportFragmentManager());
		 * 
		 * // Set up the ViewPager with the sections adapter. mViewPager =
		 * (ViewPager) findViewById(R.id.pager);
		 * mViewPager.setAdapter(mSectionsPagerAdapter);
		 * 
		 * // When swiping between different sections, select the corresponding
		 * // tab. mViewPager .setOnPageChangeListener(new
		 * ViewPager.SimpleOnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int position) {
		 * mActionBar.setSelectedNavigationItem(position); } });
		 * 
		 * // For each of the sections in the app, add a tab to the action bar.
		 * for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) { //
		 * Create a tab with text corresponding to the page title defined by //
		 * the adapter. Also specify this Activity object, which implements //
		 * the TabListener interface, as the callback (listener) for when //
		 * this tab is selected. mActionBar.addTab(mActionBar.newTab()
		 * .setText(mSectionsPagerAdapter.getPageTitle(i))
		 * .setTabListener(this)); }
		 */

		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// find out if this is the first time app has been run
		// if it's the first time, show welcome screen,
		// otherwise
		// String mKey = getString(R.string.preference_name);

		String mKey = getString(R.string.preference_name);

		SharedPreferences mPrefs = this
				.getSharedPreferences(mKey, MODE_PRIVATE);
		// SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();

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

		if (!welcomeScreenShown || (mUsername == "" || mPassword == "")) {
			mEditor.putBoolean(mKey, true); // welcome screen has been shown!
			mEditor.commit();
			showWelcome();
		}

		// start notifications when app is opened..
		Intent i = new Intent(this, NotificationSetter.class);
		this.sendBroadcast(i);

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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showWelcome() {
		// this.removePages();
		Intent i = new Intent(this, WelcomeActivity.class);
		startActivity(i);
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
}