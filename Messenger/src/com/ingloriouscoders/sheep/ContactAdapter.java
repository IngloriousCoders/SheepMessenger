package com.ingloriouscoders.sheep;

import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import android.widget.GridView;

import android.content.Intent;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class ContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<Contact> mContacts = new ArrayList<Contact>();

    public ContactAdapter(Context c) {
        mContext = c;
    }

    public void addContact(Contact _contact)
    {
    	 mContacts.add(_contact);
    }
    
    public int getCount() {
        return mContacts.size();
    }

    public Object getItem(int position) {
        Contact obj = mContacts.get(position);
        return obj;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	ContactBox cb;
    	if (convertView == null)
    	{
    		   	
        	cb = new ContactBox(mContext);
        	cb.setLayoutParams(new GridView.LayoutParams(200,250));
    	}
    	else
    	{
    		cb = (ContactBox)convertView;
    	}
    	Contact thiscontact = mContacts.get(position);
    	cb.setContact(thiscontact);
    	cb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SingleChat.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ((Activity)mContext).startActivityForResult(myIntent, 0);
                
                ((Activity)mContext).overridePendingTransition(R.anim.enterfromright, R.anim.leavetoleft);
                 

        	}
    	});
    	
    	return cb;
    }

}