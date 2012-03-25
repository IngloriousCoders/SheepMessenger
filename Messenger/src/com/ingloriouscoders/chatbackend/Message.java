package com.ingloriouscoders.chatbackend;

import android.graphics.Color;
import android.util.Log;

public class Message {
	
	private String msgtext;
	private String sender;
	private boolean incoming;
	private int color;
	
	public Message(String _msgtext, String _sender, boolean _incoming, int _color) {
		this.msgtext = _msgtext;
		this.sender  = _sender;
		this.incoming = _incoming;
		this.color = _color;
	}
	
	public Message() {
		this.msgtext = "null";
		this.sender  = "null";
	}
	

	public String getMessageText()
	{
		return msgtext;
	}
	public String getSender()
	{
		return sender;
	}
	public boolean getIncoming()
	{
		return incoming;
	}
	public int getColor()
	{
		return color;
	}
	
	
	public void setMessageText(String _msgtext) {
		this.msgtext = _msgtext;
	}
	public void setSender(String _sender) {
		this.sender = _sender;
	}
	
	public static Message getPlaceholder()
	{
		return new Message("null","null name",false,0);
	}
	
}
