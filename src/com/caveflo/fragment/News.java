package com.caveflo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.caveflo.R;

public class News extends Fragment {

    private String name;
    private String layout;
    public static final String url = "http://www.cave-flo.com/actus/";

    public News(String name, String layout) {
		super();
		this.name = name;
		this.layout = layout;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(getResources().getIdentifier(layout, "layout", container.getContext().getPackageName()), container, false);
    }

    public String getName() {
    	return name;
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	WebView webView = (WebView) getActivity().findViewById(R.id.web_view); 
    	webView.getSettings().setJavaScriptEnabled(true);
    	webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
    	webView.getSettings().setBuiltInZoomControls(true);  
    	webView.setWebViewClient(new WebViewClient());
    	webView.loadUrl(url);
    }
}
