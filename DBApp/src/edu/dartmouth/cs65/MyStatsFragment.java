package edu.dartmouth.cs65;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MyStatsFragment extends Fragment {
		
	
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
    
    private Context context;
    
	public MyStatsFragment(){	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	context = this.getActivity();
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
		ArrayList<Double> locationSpending =  Utils.getLocationSpending(context);
		foco = locationSpending.get(Globals.FOCO_LOCATION_INT);
		collis = locationSpending.get(Globals.COLLIS_LOCATION_INT);
		hop = locationSpending.get(Globals.HOP_LOCATION_INT);
		novack = locationSpending.get(Globals.NOVACK_LOCATION_INT);
		kaf = locationSpending.get(Globals.KAF_LOCATION_INT);
		ewsnackbar = locationSpending.get(Globals.SNACKBAR_LOCATION_INT);
		collismarket = locationSpending.get(Globals.COLLIS_MARKET_LOCATION_INT);
		
		/*
	    foco = 3; 
	    collis = 1; 
	    hop = 1; 
	    novack = 1; 
	    kaf = 1; 
	    ewsnackbar = 1; 
	    collismarket = 1; 
	    */
		
//	    ArrayList<Double> allAmounts = Utils.getLocationSpending(getActivity()); 
//	    for (Double amount : allAmounts) {
//	    	
//	    }
	    
	    // put the dba by week usage here 
		ArrayList<Double> weeklySpending =  Utils.getWeeklySpending(context);
		week1 = weeklySpending.get(0);
		week2 = weeklySpending.get(1);
		week3 = weeklySpending.get(2);
		week4 = weeklySpending.get(3);
		week5 = weeklySpending.get(4);
		week6 = weeklySpending.get(5);
		week7 = weeklySpending.get(6);
		week8 = weeklySpending.get(7);
		week9 = weeklySpending.get(8);
		week10 = weeklySpending.get(9);

		/*
	    week1 = 110; 
	    week2 = 120; 
	    week3 = 100; 
	    week4 = 140; 
	    week5 = 90; 
	    week6 = 110; 
	    week7 = 120; 
	    week8 = 100; 
	    week9 = 70; 
	    week10 = 80; 
	    */
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

