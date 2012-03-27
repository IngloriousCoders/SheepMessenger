package com.ingloriouscoders.chatbackend;

import android.graphics.Color;
import android.util.Log;

public class Message {
	
	private String msgtext;
	private String sender;
	private boolean incoming;
	private int color;
	
	protected Conversation mConversation;
	
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
	public void setIncoming(boolean _state)
	{
		this.incoming = true;
	}
	public void setColor(int _color)
	{
		this.color = _color;
	}
	public void setConversation(Conversation _conv)
	{
		this.mConversation = _conv;
	}
	
	public boolean send()
	{
		if (mConversation == null)
		{
			return false;
		}
		return mConversation.sendMessage(this);
	}
	
	public static Message getPlaceholder()
	{
		return new Message("null","null name",false,0);
	}
	
	
	
}
