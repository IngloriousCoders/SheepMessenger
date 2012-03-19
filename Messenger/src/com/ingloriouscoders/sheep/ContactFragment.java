package com.ingloriouscoders.sheep;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.view.LayoutInflater;
import android.widget.GridView;

public class ContactFragment extends Fragment {
	private View mContentView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.contacts, null);
		
		GridView gv = (GridView)mContentView.findViewById(R.id.contactGridView);
		ContactAdapter adp = new ContactAdapter(getActivity());
		gv.setAdapter(adp);
		for(int i=0;i<10;i++)
		{
			Contact contact = new Contact("user","Person" + i);
			adp.addContact(contact);
		}
		return mContentView;

    }

}
