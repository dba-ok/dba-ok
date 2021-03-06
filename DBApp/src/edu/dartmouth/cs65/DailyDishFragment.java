/**
 * DBA-OK
 * 
 * This file defines the DailyDishFragment class. The DailyDishFragment class uses a WebView widget to display the Dartmouth Dining Services
 * menus from http://www.dartmouth.edu/dining/menus/. 
 */
package edu.dartmouth.cs65;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.dartmouth.cs65.R;

public class DailyDishFragment extends Fragment {

	private WebView wv;

	public DailyDishFragment() {
	}

	private String currentURL = "http://www.dartmouth.edu/dining/menus/";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// load the DDS url in the webview
		wv = (WebView) getView().findViewById(R.id.webPage);
		wv.setWebViewClient(new MyBrowser());
		wv.getSettings().setLoadsImagesAutomatically(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wv.loadUrl(currentURL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate the layout
		return inflater.inflate(R.layout.dailydish, container, false);
	}

	private class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
}