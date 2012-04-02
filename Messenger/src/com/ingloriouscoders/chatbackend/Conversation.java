package com.ingloriouscoders.chatbackend;

import java.util.List;
import java.util.Vector;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;


public class Conversation{
	
	private List<OnNewMessageListener> newMessageListeners = new ArrayList<OnNewMessageListener>();
	private OnUnreadMessagesListener mUnreadListener = null;
	
	private ChatContext mContext;
	private Contact mOpposite;
	protected Chat mChat;
	
	private int unreadCount = 0;
	private boolean notificationMuted = false;
	
	private List<Message> history = new Vector<Message>();
	
	public static Conversation spawnConversation(Contact _opposite,ChatContext _ctx)
	{
		List<Conversation> active_conv = _ctx.getActiveConversations();
		for (int i=0;i<active_conv.size();i++)
		{
			if (active_conv.get(i).getOpposite().getAddress().equals(_opposite.getAddress()) )
			{
				return _ctx.getActiveConversations().get(i);
			}
		}
		return new Conversation(_opposite, _ctx);
	}
	
	private Conversation(Contact _opposite,ChatContext _ctx)
	{
		mContext = _ctx;
		mOpposite = _opposite;
		mContext.etablishConversation(this);	
		//this.setOnUnreadMessagesListener(mOpposite.unreadListener);
	}
	public Contact getOpposite()
	{
		return mOpposite;
	}
	public List<Message> getHistory(int timestamp)
	{
		this.unreadCount = 0;
		return history;
	}
	
	public ChatContext getContext()
	{
		return mContext;
	}
	public Message prepareMessage()
	{
		Message newmsg = new Message();
		newmsg.setSender(mContext.getUserShowname());
		newmsg.setIncoming(false);

		return newmsg;
	}	
	private void addMessage(Message _msg)
	{
		history.add(_msg);
		Log.v("chatbackend","added Message: Size=" + history.size());
	}
	public boolean sendMessage(Message _msg)
	{
		if (mChat == null)
		{
			Log.v("chatbackend","Chat is null");
			return false;
		}
		if (!mContext.isConnected())
		{
			Log.v("chatbackend","Nicht mit Server verbunden");
			return false;
		}
		try {
			org.jivesoftware.smack.packet.Message newMessage = new org.jivesoftware.smack.packet.Message();
			newMessage.setBody(_msg.getMessageText());
							
		    mChat.sendMessage(newMessage);
		}
		catch (XMPPException e) {
		    Log.v("chatbackend","Fehler beim Zustellen der Nachricht: '" + _msg.getMessageText() + "'");
		}
		finally
		{
			this.addMessage(_msg);
		}
		Log.v("ChatBackend","Nachricht: " + _msg.getMessageText() + " wurde versendet.");
		return true;
	}
	
	private final Conversation thisclass = this;
	protected org.jivesoftware.smack.MessageListener smack_listener =  new org.jivesoftware.smack.MessageListener() {
		@Override
		public void processMessage(Chat _chat,org.jivesoftware.smack.packet.Message _msg) {
			Message recieved_msg = new Message(_msg.getBody(),thisclass.getOpposite().getShowname(),true,1);
					
			if (_msg.getType() == org.jivesoftware.smack.packet.Message.Type.error)
			{
				
				
				XMPPError myerr = _msg.getError();
				Log.v("chatbackend","Fehler bei der Übermittlung: " + myerr.getMessage() + " | " + myerr.getCode());
				
				return;
			}
			
			thisclass.addMessage(recieved_msg);
			
			unreadCount++;
			
			for ( OnNewMessageListener mListener : Conversation.this.newMessageListeners )
			{
				mListener.onNewMessage(thisclass,recieved_msg );
			}
			
			Conversation.this.getContext().childNewMessageListener.onNewMessage(Conversation.this,recieved_msg);
		}
	};
	public void resetUnreadCount()
	{
		this.unreadCount = 0;
		if (this.mUnreadListener != null)
		{
			mUnreadListener.onNewMessage(this.getOpposite(), this.unreadCount);
		}
		Log.v("luca","reset count");
		
	}
	
	public void addOnNewMessageListener(OnNewMessageListener _listener)
	{
		this.newMessageListeners.add(_listener);
	}
	public void removeOnNewMessageListener(OnNewMessageListener _listener)
	{
		this.newMessageListeners.remove(_listener);
	}
	public void setNotificationMuted(boolean state)
	{
		this.notificationMuted = state;
	}
	public boolean getNotificationMuted()
	{
		return this.notificationMuted;
	}

}
