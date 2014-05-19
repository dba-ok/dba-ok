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

import com.example.swiping.R;

public class DailyDishFragment extends Fragment {

	private WebView wv;

	public DailyDishFragment() {
	}

	private String currentURL = "http://www.dartmouth.edu/dining/menus/";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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