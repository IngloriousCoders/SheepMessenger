package com.ingloriouscoders.sheep;


import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;

import com.ingloriouscoders.chatbackend.Contact;

import android.widget.GridView;
import android.os.Parcelable;

import android.content.Intent;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.util.Log;

public class ContactAdapter extends BaseAdapter {
    private Context mContext;
    protected List<ContactStated> mContacts = new ArrayList<ContactStated>();

    final private ContactAdapter thisclass = this;
       
    public ContactAdapter(Context c) {
        mContext = c;
    }

    public void addContact(ContactStated _contact)
    {
    	mContacts.add(_contact);
    	this.notifyDataSetChanged();
    }
    public void removeContact(Contact _contact)
    {
    	if (mContacts.contains(_contact))
    	{
    		mContacts.remove(_contact);
    		this.notifyDataSetChanged();
    	}
    	
    }
    
    public int getCount() {
        return mContacts.size();
    }

    public Object getItem(int position) {
        Contact obj = mContacts.get(position);
        return obj;
    }

    public long getItemId(int position) {
        return (long)position;
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
    	final ContactStated thiscontact = mContacts.get(position);
    	final ContactAdapter ca = this;
    	    	
    	cb.setContact(thiscontact);
    	thiscontact.addOnContactStatedDataChangedListener(mListener);
    	cb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SingleChat.class);
                
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                myIntent.putExtra("contact",thiscontact);
                ((Activity)mContext).startActivityForResult(myIntent, 0);
                ((Activity)mContext).overridePendingTransition(R.anim.enterfromright, R.anim.leavetoleft);
               
         	}
    	});
    	
    	
    	return cb;
    }
    
    protected OnContactStatedDataChanged mListener;
}