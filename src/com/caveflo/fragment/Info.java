package com.caveflo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.caveflo.CaveDB;
import com.caveflo.R;

public class Info extends Fragment {

    private String name;
    private String layout;

    public Info(String name, String layout) {
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
		 Button buttonStart = (Button)getActivity().findViewById(R.id.buttonRefresh);        
	   	    buttonStart.setOnClickListener(new OnClickListener() {
	   			public void onClick(View v) {
	   				new CaveDB(getActivity()).update();
	   			}
	   		}); 	
    }
}
