package com.ingloriouscoders.chatbackend;

import android.util.Log;

public class Contact {
	
	private String username;
	private String showname;
	private String photoURI = "";
	private int unread_messages = 0;
	
	public Contact(String _username)
	{
		this.username = _username;
		this.showname = _username;
		
	}
	public Contact(String _username,String _showname)
	{
		this.username = _username;
		this.showname = _showname;
		
	}
	public Contact(String _username,String _showname,String photo_uri)
	{
		this.username = _username;
		this.showname = _showname;
		if (photo_uri != null)
		{
			this.photoURI = photo_uri;
		}
		
	}
	public void setShowName(String _showname)
	{
		this.showname = _showname;
	}
	public String getUsername()
	{
		return username;
	}
	public String getShowname()
	{
		return showname;
	}
	public void setUnreadMessages(int value)
	{
		unread_messages = value; 
	}
	public int getUnreadMessages()
	{
		return unread_messages;
	}
	public String getPhotoURI()
	{
		return this.photoURI;
	}
	
	public static Contact getPlaceholder()
	{
		return new Contact("null","null name");
	}
	
}
