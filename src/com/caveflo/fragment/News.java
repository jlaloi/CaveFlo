package com.caveflo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.caveflo.R;

public class News extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.news, container, false);
		return mainView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		WebView webView = (WebView) getActivity().findViewById(R.id.web_view);
		webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient());
		Toast.makeText(getActivity(), getString(R.string.loadingpage, getString(R.string.news_url)), Toast.LENGTH_SHORT).show();
		webView.loadUrl(getString(R.string.news_url));
	}

}
