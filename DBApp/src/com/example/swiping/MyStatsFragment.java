package com.example.swiping;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyStatsFragment extends Fragment {
	
	WebView mWebView;
	private String currentURL = "file:///android_asset/chart/stats.html"; 

/*	private String html = "<html> <head> " +
			"<title></title>" +
			"<style type=\"text/css\">
           
                body {
                    color:white;                    
                 }
                </style>
 </head>"; */
	

/*	StringBuilder contentBuilder = new StringBuilder();
	try {
	    BufferedReader in = new BufferedReader(new FileReader("file:///android_asset/chart/stats.html"));
	    String str;
	    while ((str = in.readLine()) != null) {
	        contentBuilder.append(str);
	    }
	    in.close();
	} catch (IOException e) {
	}
	String content = contentBuilder.toString();
*/			
			
	//private String html = ""
	
	public MyStatsFragment() {
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.mystats, container, false);
    	Log.d("dba", "in oncreateview()"); 
        
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork() // or .detectAll() for all detectable problems
		.penaltyLog().build());

		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.mystats, container, false);
 
    }
    


	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("dba", "in onactivitycreated()"); 
		
		mWebView = (WebView) getView().findViewById(R.id.mystatswebview);
//		mWebView.loadData(html, "text/html", "UTF-8");
		mWebView.setWebViewClient(new MyBrowser()); 
		mWebView.getSettings().setLoadsImagesAutomatically(true); 
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setBackgroundColor(Color.BLACK);
	   
	    mWebView.loadUrl(currentURL);
	    
	    
	    
	    Log.d("dba", "after loadurl"); 
	}
    
	private class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
    
    
    
}

