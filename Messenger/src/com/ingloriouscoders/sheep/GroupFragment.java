package com.ingloriouscoders.sheep;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.GridView;

public class GroupFragment extends Fragment {
	
	private View mContentView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.groups, null);
		
		GridView gv = (GridView)mContentView.findViewById(R.id.groupGridView);
		ContactAdapter adp = new ContactAdapter(getActivity());
		gv.setAdapter(adp);
		for(int i=0;i<3;i++)
		{
			Contact contact = new Contact("user","Gruppe" + i);
			adp.addContact(contact);
		}
		return mContentView;

    }

}
