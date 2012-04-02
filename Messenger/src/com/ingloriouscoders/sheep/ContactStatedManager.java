package com.ingloriouscoders.sheep;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;

public class ContactStatedManager {
	
	private static ContactStatedManager instance; 
	
	private List<ContactStated> contactList;

	private static ContactStatedManager getInstance()
	{
		if (instance == null)
		{
			instance = new ContactStatedManager();
		}
		
		return instance;
	}
	
	private ContactStatedManager()
	{
		contactList = new ArrayList<ContactStated>();
		Log.v("ContactStatedManager","Instanz erzeugt");

	}
	
	public static ContactStated getSingletonInstance(ContactStated _contact)
	{
		if (_contact.listID != -1)
		{
			ContactStated existing_contact = getContact(_contact.listID);
			if (existing_contact != null)
			{
				return existing_contact;
			}
		}
		ContactStated existing_contact = getContact(_contact.getAddress());
		if (existing_contact != null)
		{
			return existing_contact;
		}
		return null;
	}
	
	public static ContactStated addContact(ContactStated _contact)
	{
		ContactStated existing_contact = getSingletonInstance(_contact);
		if (existing_contact != null)
		{
			Log.v("ContactStatedManager","Got " + existing_contact + " from cache");
			return existing_contact;
		}
		
		ContactStatedManager mgr = getInstance();
		Log.v("ContactStatedManager",_contact.getAddress() + " was added, because it has not been here ever");
		mgr.contactList.add(_contact);
		_contact.listID = mgr.contactList.size()-1;	
		return _contact;
	}
	public static ContactStated getContact(int listID)
	{
		ContactStatedManager mgr = getInstance();
		if (listID > mgr.contactList.size() && listID <= 0)
		{
			return null;
		}
		Log.v("ContactStatedManager","Got " + mgr.contactList.get(listID).getAddress() + " from cache");
		return mgr.contactList.get(listID);
	}
	public static ContactStated getContact(String _address)
	{
		ContactStatedManager mgr = getInstance();
		
		String contactAddress = _address;
		for (ContactStated currentContact : mgr.contactList)
		{
			if (currentContact.getAddress().equals(contactAddress))
			{
				Log.v("ContactStatedManager","Got " + currentContact.getAddress() + " from cache");
				return currentContact;
			}
		}
		return null;
	}

}
