package com.ingloriouscoders.sheep;

import com.ingloriouscoders.chatbackend.Contact;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.view.LayoutInflater;

import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;


public class ContactFragment extends Fragment {
	private View mContentView;
	private Context mContext;
	protected ContactAdapter adp;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.contacts, null);
		mContext = container.getContext();
		
		ContactGrid gv = (ContactGrid)mContentView.findViewById(R.id.contactGridView);
		
		adp = (ContactAdapter)gv.getAdapter();
		
		/*Contact special_contact = new Contact("user","Hans Peter","content://com.android.contacts/contacts/66/photo");
		special_contact.setAddress("thebkfamily@googlemail.com");
		special_contact.setUnreadMessages(5);
		adp.addContact(special_contact);*/
		
		ContentResolver cr = mContext.getContentResolver();
		Log.v("ContentBox",Data.DISPLAY_NAME);
		Cursor c = cr.query(Data.CONTENT_URI,
		          new String[] {Data._ID,Data.PHOTO_URI ,Data.DISPLAY_NAME,Email.ADDRESS},
		          null,
		          null, 
		          "Data.DISPLAY_NAME ASC");
		 

		int display_name_index = c.getColumnIndex(Data.DISPLAY_NAME);
		int photo_file_index = c.getColumnIndex(Data.PHOTO_URI );
		int email_index = c.getColumnIndex(Email.ADDRESS);
		String last_string = "";
		
		while(c.moveToNext())
		{
			if (!c.getString(display_name_index).equals(last_string))
			{
				String photo_uri = c.getString(photo_file_index);
				String email_address = c.getString(email_index);
				if ( (email_address != null && !email_address.contains("googlemail.com") ) ||
					  email_address == null)
				{
					continue;
				}
				
				Contact contact;
				if (photo_uri != null)
				{
					contact = new Contact("user",c.getString(display_name_index),photo_uri);
				}
				else
				{
					contact = new Contact("user",c.getString(display_name_index));
				}
				contact.setAddress(email_address);
				adp.addContact(contact);
				
				last_string = c.getString(display_name_index);
			}
			
		}
		c.close();
		return mContentView;

    }
	

}
