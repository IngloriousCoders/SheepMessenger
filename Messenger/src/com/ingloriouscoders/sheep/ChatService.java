package com.ingloriouscoders.sheep;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.util.Log;

import com.ingloriouscoders.chatbackend.*;

public class ChatService extends Service {

	private String username;
	private String password;
	
	private List<OnServiceSpawnConversationListener> listeners = new ArrayList<OnServiceSpawnConversationListener>();
	private ChatContext mChatContext;
	
	private ServiceChatContext.Stub chatContextImplementation = new ServiceChatContext.Stub()
	{

		@Override
		public ServiceConversation spawnConversation(Contact _opposite) throws RemoteException 
		{
			synchronized (mChatContext)
			{
				final Conversation newConversation = Conversation.spawnConversation( _opposite, mChatContext);
				//Log.v("ChatService","Konversation mit Kontakt " + newConversation.getOpposite().getShowname() + " angefordert");
				return ServiceInterfaceFromObject(newConversation);
			}
		}
		
		@Override
		public void addOnServiceSpawnConversationListener( OnServiceSpawnConversationListener listener ) throws RemoteException 
		{
			synchronized ( listeners )
			{
				listeners.add(listener);
			}
		}

		@Override
		public void removeOnServiceSpawnConversationListener( OnServiceSpawnConversationListener listener ) throws RemoteException 
		{
			synchronized ( listeners )
			{
				listeners.remove(listener);
			}			
		}

		@Override
		public void setStatus(String status) throws RemoteException {
			synchronized ( mChatContext )
			{
				mChatContext.setStatus(status);
			}
			
		}
		
	};
	
	private ServiceConversation ServiceInterfaceFromObject(final Conversation _conversation)
	{
		return new ServiceConversation.Stub()
		{
			
			
			{
				final ServiceConversation thisobj = this;
				_conversation.addOnNewMessageListener(new OnNewMessageListener() {
					{
						mServiceConversation = thisobj;
					}
					final ServiceConversation mServiceConversation;
					@Override
					public void onNewMessage(Conversation _conversation, Message _newmessage) {
						synchronized ( mListeners)
						{
							//Log.v("ChatService","Conversation listener called - List size:" + mListeners.size() );
							try
							{
								for( OnServiceNewMessageListener currentListener : mListeners)
								{
									currentListener.onNewMessage(mServiceConversation, _newmessage);
								}
							}
							catch (RemoteException e)
							{
								Log.v("ChatService","Error calling the newMessageListeners");
							}
					
						}
						
					}
				});
			}
				
			public List<OnServiceNewMessageListener> mListeners = new ArrayList<OnServiceNewMessageListener>();
			@Override
			public Contact getOpposite() throws RemoteException {
				synchronized(_conversation)
				{
					return _conversation.getOpposite();
				}
			}

			@Override
			public List<Message> getHistory() throws RemoteException 
			{
				synchronized(_conversation)
				{	
					return _conversation.getHistory(0);
				}
			}

			@Override
			public Message prepareMessage() throws RemoteException {
				synchronized(_conversation)
				{
					Message msg = _conversation.prepareMessage();
					Log.v("ChatService","Nachricht an Kontakt " + _conversation.getOpposite().getShowname() + " angefordert:" +
							msg.getMessageText() + " von " + msg.getSender());
					return msg;
				}
			}

			@Override
			public boolean sendMessage(Message _msg) throws RemoteException {
				synchronized(_conversation)
				{
					Log.v("ChatService","Nachricht an Kontakt " + _conversation.getOpposite().getShowname() + " gesendet:" + _msg.getMessageText());
					return _conversation.sendMessage(_msg);
				}
			}

			@Override
			public void addOnServiceNewMessageListener(
					OnServiceNewMessageListener _listener)
					throws RemoteException {
				
				mListeners.add(_listener);
				
			}

			@Override
			public void removeOnServiceNewMessageListener(
					OnServiceNewMessageListener _listener)
					throws RemoteException {
				mListeners.add(_listener);				
			}
			
		};
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.v("ChatService","Verbindung zum Service hergestellt");
		return chatContextImplementation;
		
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		
				
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	Intent serviceIntent = new Intent(this, ChatService.class);
    	username = prefs.getString("account_username", "");
    	password = prefs.getString("account_password", "");
    	
    	this.mChatContext = new ChatContext(username, password);
    	if (this.mChatContext == null)
    	{
    		Log.v("ChatService","Feher beim initialisieren");
    		return;
    	}
    	if (!this.mChatContext.isInitiated())
    	{
    		Log.v("ChatService","Feher beim initialisieren" + mChatContext.getLastErrorMessage());
    		return;
    	}
    	
    	mChatContext.connect();
    	
    	if (!this.mChatContext.isConnected())
    	{
    		Log.v("ChatService","Feher beim verbinden" + mChatContext.getLastErrorMessage());
    		return;
    	}
    	
    	
    	Toast.makeText(ChatService.this, "Service gestartet. Benutzername:" + username,
				Toast.LENGTH_SHORT).show();
    	
    	
	}
	
    
}