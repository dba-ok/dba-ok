package edu.dartmouth.cs65;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private EditText FocoField;
    private EditText CollisField;
    private EditText HopField;
    private EditText NovackField;
    private EditText KafField; 
    private EditText EWSnackBarField;
    private EditText CollisMarketField; 
    
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
        webSettings.setBuiltInZoomControls(true);

        FocoField = (EditText) getView().findViewById(R.id.foco);
        CollisField = (EditText) getView().findViewById(R.id.collis);
        HopField = (EditText) getView().findViewById(R.id.hop);
        NovackField = (EditText) getView().findViewById(R.id.novack);
        KafField = (EditText) getView().findViewById(R.id.kaf);
        EWSnackBarField = (EditText) getView().findViewById(R.id.ewsnackbar);
        CollisMarketField = (EditText) getView().findViewById(R.id.collismarket);

        initPieButton();
        initColumnButton(); 
        initImeActionReload();

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
	
	private void initImeActionReload() {
		KafField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	        @Override
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	            if (actionId == EditorInfo.IME_NULL) {
	                loadChart("column");
	                return true;
	            }
	
	            return false;
	        }
	    });
	}
	
	private void loadChart(String chart) {
	    if (TextUtils.isEmpty(FocoField.getText())) {
	    	FocoField.setText("0");
	        return;
	    }
	
	    if (TextUtils.isEmpty(CollisField.getText())) {
	    	CollisField.setText("0");
	        return;
	    }

	    if (TextUtils.isEmpty(HopField.getText())) {
	    	NovackField.setText("0");
	        return;
	    }
	    
	    if (TextUtils.isEmpty(NovackField.getText())) {
	    	NovackField.setText("0");
	        return;
	    }
	
	    if (TextUtils.isEmpty(KafField.getText())) {
	    	KafField.setText("0");
	        return;
	    }
	    
	    if (TextUtils.isEmpty(EWSnackBarField.getText())) {
	    	EWSnackBarField.setText("0");
	        return;
	    }
	
	    if (TextUtils.isEmpty(CollisMarketField.getText())) {
	    	CollisMarketField.setText("0");
	        return;
	    }

	    
	    hideSoftKeyboard(FocoField);
	    hideSoftKeyboard(CollisField);
	    hideSoftKeyboard(HopField);
	    hideSoftKeyboard(NovackField);
	    hideSoftKeyboard(KafField);
	    hideSoftKeyboard(EWSnackBarField);
	    hideSoftKeyboard(CollisMarketField);
	    
	    int foco = Integer.parseInt(FocoField.getText().toString());
	    int collis = Integer.parseInt(CollisField.getText().toString());
	    int hop = Integer.parseInt(HopField.getText().toString());
	    int novack = Integer.parseInt(NovackField.getText().toString());
	    int kaf = Integer.parseInt(KafField.getText().toString());
	    int ewsnackbar = Integer.parseInt(EWSnackBarField.getText().toString());
	    int collismarket = Integer.parseInt(EWSnackBarField.getText().toString());
	    
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
	
	    String formattedContent = String.format(content, foco, collis, hop, novack, kaf, ewsnackbar, collismarket);
	    webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);
	    webView.requestFocusFromTouch();
	}
	
	private void hideSoftKeyboard(TextView v) {
	    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	} */
	
	private static byte[] readFully(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    for (int count; (count = in.read(buffer)) != -1; ) {
	        out.write(buffer, 0, count);
	    }
	    return out.toByteArray();
	}
	
	
    
}

