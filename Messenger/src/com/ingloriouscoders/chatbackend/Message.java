package com.ingloriouscoders.chatbackend;

import android.graphics.Color;
import android.util.Log;

public class Message {
	
	private String msgtext;
	private String sender;
	private boolean incoming;
	private int color;
	public boolean placeholder;
	private OnNewMessageListener mListener;
	
	protected Conversation mConversation;
	
	public Message(String _msgtext, String _sender, boolean _incoming, int _color) {
		this.msgtext = _msgtext;
		this.sender  = _sender;
		this.incoming = _incoming;
		this.color = _color;
		this.placeholder = false;
	}
	
	public Message() {
		this.msgtext = "";
		this.sender  = "";
		this.incoming = true;
		this.color = Color.BLACK;
		this.placeholder = true;
	}
	
	public boolean isPlaceholder()
	{
		return this.placeholder;
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
		this.incoming = _state;
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
	
	public void setOnNewMessageListener(OnNewMessageListener _listener)
	{
		this.mListener = _listener;
	}
	
	public OnNewMessageListener unreadListener = new OnNewMessageListener() {
		
		@Override
		public void onNewMessage(Conversation _conv, Message _msg) {
			Log.v("Custom","New unread message");
		}
	};
}
