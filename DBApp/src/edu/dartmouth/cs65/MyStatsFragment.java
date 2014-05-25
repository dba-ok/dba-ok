package edu.dartmouth.cs65;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.dartmouth.cs65.R;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyStatsFragment extends Fragment {
	
/*	public WebView wv; 
	
	public MyStatsFragment(){	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mystats, container, false);
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		wv = (WebView) getView().findViewById(R.id.mystatswebview);
	    wv.getSettings().setJavaScriptEnabled(true);
	    wv.setBackgroundColor(Color.BLACK);
	     
		wv.loadUrl("file:///android_asset/chart/stats2.html");
	} */
	
	
    private final String TAG = getClass().getSimpleName();

    public static final String ASSET_PATH = "file:///android_asset/";

    private WebView webView;
    private double foco; 
    private double collis; 
    private double hop; 
    private double novack; 
    private double kaf; 
    private double ewsnackbar; 
    private double collismarket; 
    
    private double week1; 
    private double week2; 
    private double week3; 
    private double week4; 
    private double week5; 
    private double week6; 
    private double week7; 
    private double week8; 
    private double week9; 
    private double week10; 
    
    
	public MyStatsFragment(){	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mystats, container, false);
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        webView = (WebView) getView().findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);


        initPieButton();
        initColumnButton(); 

        loadChart("pie");
	}
	
	 private void initPieButton() {
	        Button pieButton = (Button) getView().findViewById(R.id.piebutton);
	        pieButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                loadChart("pie");
	            }
	        });
	}
	 
	private void initColumnButton() {
	        Button columnButton = (Button) getView().findViewById(R.id.columnbutton);
	        columnButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                loadChart("column");
	            }
	        });
	}

	
	private void loadChart(String chart) {
	    
	    // put the dba by location usage here 
	    foco = 3; 
	    collis = 1; 
	    hop = 1; 
	    novack = 1; 
	    kaf = 1; 
	    ewsnackbar = 1; 
	    collismarket = 1; 
	    
//	    ArrayList<Double> allAmounts = Utils.getLocationSpending(getActivity()); 
//	    for (Double amount : allAmounts) {
//	    	
//	    }
	    
	    // put the dba by week usage here 
	    week1 = 100; 
	    week2 = 100; 
	    week3 = 100; 
	    week4 = 100; 
	    week5 = 100; 
	    week6 = 100; 
	    week7 = 100; 
	    week8 = 100; 
	    week9 = 100; 
	    week10 = 100; 
	    
	    String content = "null";
	    try {
	        AssetManager assetManager = getActivity().getAssets();
	        InputStream in = null; 
	        if (chart.equals("pie")) {
		        in = assetManager.open("stats_pie.html");	        	
	        } else if (chart.equals("column")) {
		        in = assetManager.open("stats_column.html");
	        }
	        Log.d(TAG, "input " + in ); 
	        byte[] bytes = readFully(in);
	        content = new String(bytes, "UTF-8");
	    } catch (IOException e) {
	        Log.e(TAG, "An error occurred.", e);
	    }
	
	    if (chart.equals("pie")) {
		    String formattedContent = String.format(content, foco, collis, hop, novack, kaf, ewsnackbar, collismarket);
		    webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);	    	
	    } else if (chart.equals("column")) {
		    String formattedContent = String.format(content, week1, week2, week3, week4, week5, 
		    		week6, week7, week8, week9, week10);
		    webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);
	    }
	    webView.requestFocusFromTouch();
	}
	
	
	private static byte[] readFully(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    for (int count; (count = in.read(buffer)) != -1; ) {
	        out.write(buffer, 0, count);
	    }
	    return out.toByteArray();
	}
	
	
    
}

