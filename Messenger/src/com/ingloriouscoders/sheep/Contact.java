package com.ingloriouscoders.sheep;

public class Contact {
	
	private String username;
	private String showname;
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
	
	public static Contact getPlaceholder()
	{
		return new Contact("null","null name");
	}
	
}
