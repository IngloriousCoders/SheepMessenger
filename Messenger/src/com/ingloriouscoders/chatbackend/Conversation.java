package com.ingloriouscoders.chatbackend;

import java.util.List;
import java.util.Vector;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

public class Conversation {
	
	private OnNewMessageListener mListener = null;
	
	private ChatContext mContext;
	private Contact mOpposite;
	protected Chat mChat;
	
	private List<Message> history = new Vector<Message>();
	
	public static Conversation spawnConversation(Contact _opposite,ChatContext _ctx)
	{
		for (int i=0;i<_ctx.getActiveConversations().size();i++)
		{
			if (_ctx.getActiveConversations().get(i).getOpposite() == _opposite)
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
		
		
		
	}
	public Contact getOpposite()
	{
		return Contact.getPlaceholder();
	}
	public List<Message> getHistory(int timestamp)
	{
		return history;
	}
	public void setOnNewMessageListener(OnNewMessageListener _listener)
	{
		this.mListener = _listener;
	}
	public ChatContext getContext()
	{
		return mContext;
	}
	public Message prepareMessage()
	{
		Message newmsg = new Message();
		newmsg.setSender(getOpposite().getShowname());
		newmsg.setIncoming(false);
		newmsg.setColor(2);
		newmsg.setConversation(this);
		return newmsg;
	}	
	private void addMessage(Message _msg)
	{
		history.add(_msg);
	}
	protected boolean sendMessage(Message _msg)
	{
		if (_msg.mConversation != this)
		{
			return false;
		}
		try {
		    mChat.sendMessage(_msg.getMessageText());
		}
		catch (XMPPException e) {
		    Log.v("chatbackend","Fehler beim Zustellen der Nachricht: '" + _msg.getMessageText() + "'");
		}
		
		return true;
	}
	
	private final Conversation thisclass = this;
	protected org.jivesoftware.smack.MessageListener smack_listener =  new org.jivesoftware.smack.MessageListener() {
		@Override
		public void processMessage(Chat _chat,org.jivesoftware.smack.packet.Message _msg) {
			
			Message recieved_msg = new Message(_msg.getBody(),thisclass.getOpposite().getShowname(),true,1);
			
			thisclass.addMessage(recieved_msg);
			if (thisclass.mListener != null)
			{
				mListener.onNewMessage(thisclass,recieved_msg );
			}
		}
	};
	
}
