package com.ingloriouscoders.sheep;

import com.ingloriouscoders.chatbackend.Contact;

import android.util.AttributeSet;
import android.widget.GridView;
import android.content.Context;
import android.widget.ListAdapter;
import android.util.Log;

public class ContactGrid extends GridView {
	
	private boolean adapter_set = false;
	
	protected ContactAdapter mAdapter;
	public ContactGrid(Context ctx)
	{
		super(ctx);
		initAdapter();
		
	}
	public ContactGrid(Context ctx,AttributeSet attrs)
	{
		super(ctx,attrs);
		initAdapter();
	}
	public ContactGrid(Context ctx,AttributeSet attrs,int defStyle)
	{
		super(ctx,attrs,defStyle);
		initAdapter();
	}
	private void initAdapter()
	{
		ContactAdapter adp = new ContactAdapter(getContext());
		this.setAdapter(adp);
		mAdapter = adp;
		((ContactAdapter)adp).mListener = mListener;
	}
	
	private final ContactGrid thisobj = this;
	public OnContactStatedDataChanged mListener = new OnContactStatedDataChanged() {
		
		@Override
		public void dataChanged(ContactStated _contact) {						
			Log.v("ContactGrid","Redraw called, unread=" + _contact.getUnreadMessages());
			
			if (thisobj.getAdapter() != null)
			{
				ContactAdapter adp = (ContactAdapter)thisobj.getAdapter();
				int index = adp.mContacts.indexOf(_contact);
				if (index != -1)
				{
					ContactBox cb = (ContactBox)thisobj.getChildAt(index);
					Log.v("ContactGrid","CB to invalidate unread_count=" + cb.getContact().getUnreadMessages());
					thisobj.invalidateChild(cb, null);
				}
			}
		}
	};
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		if (!adapter_set)
		{
			super.setAdapter(adapter);
			adapter_set = true;
		}
	}
	
}
