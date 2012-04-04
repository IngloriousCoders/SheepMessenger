package com.ingloriouscoders.sheep;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
	
	String ns = Context.NOTIFICATION_SERVICE;
	NotificationManager mNotificationManager;

	
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
		@Override
		public void resetNotificationCounter()
		{
			ChatService.this.resetNotificationCount_service();
		}
		@Override
		public void killNotification(int id)
		{
			Log.v("ChatService","Cannelld called");
			mNotificationManager.cancel(id);
		}
		
	};
	private final List<String> conversationListIndex = new ArrayList<String>();
	private final List<ServiceConversation> conversationList = new ArrayList<ServiceConversation>();
	private ServiceConversation ServiceInterfaceFromObject(final Conversation _conversation) throws RemoteException
	{
		if (!ChatService.this.conversationListIndex.contains(_conversation.getOpposite().getAddress()))
		{
			_conversation.addOnNewMessageListener( new OnNewMessageListener() {
				
				@Override
				public void onNewMessage(Conversation _conversation, Message _newmessage) {
					if (!_conversation.getNotificationMuted())
						ChatService.this.addMessageNotification(_conversation.getOpposite(), _newmessage);					
				}
			});
					
			
			ChatService.this.conversationListIndex.add(_conversation.getOpposite().getAddress());
			
			
			ServiceConversation nServiceConversation =  new ServiceConversation.Stub()
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
									Log.v("ChatService","Conversation listener called - List size:" + mListeners.size() );
									
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
				public Message prepareMessage(String _params, boolean _internal) throws RemoteException {
					synchronized(_conversation)
					{
						Message msg = _conversation.prepareMessage(_params, _internal);
						Log.v("ChatService","Nachricht an Kontakt " + _conversation.getOpposite().getShowname() + " angefordert:" +
								msg.getMessageText() + " von " + msg.getSender());
						return msg;
					}
				}

				@Override
				public boolean sendMessage(Message _msg, boolean _internal, String _params, boolean _timestamp) throws RemoteException {
					synchronized(_conversation)
					{
						Log.v("ChatService","Nachricht an Kontakt " + _conversation.getOpposite().getShowname() + " gesendet:" + _msg.getMessageText());
						return _conversation.sendMessage(_msg, _internal, _params, _timestamp);
					}
				}

				@Override
				public void addOnServiceNewMessageListener(
						OnServiceNewMessageListener _listener)
						throws RemoteException {
					synchronized ( mListeners )
					{
						if (!mListeners.contains(_listener))
							mListeners.add(_listener);
					}
					
				}

				@Override
				public void removeOnServiceNewMessageListener(
						OnServiceNewMessageListener _listener)
						throws RemoteException {
					synchronized ( mListeners )
					{
						mListeners.remove(_listener);
					}
				}
				@Override 
				public void setNotificationMuted(boolean state)
				{
					_conversation.setNotificationMuted(state);
				}
			};
			
			
			
			
			conversationList.add(nServiceConversation);
			return nServiceConversation;
		}
		for (ServiceConversation currentConversation : conversationList)
		{
			if (currentConversation.getOpposite().getAddress() == _conversation.getOpposite().getAddress())
			{
				return currentConversation;
			}
						
		}
		return null;
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
		
		mNotificationManager = (NotificationManager) getSystemService(ns);
		
				
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	
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
	private int notification_id = 0;
	private String last_address = "";
	private int last_count = 1;
	private int contact_count = 1;
	
	private void resetNotificationCount_service() {
			Log.v("ChatService","Reset current Notificationcount");
			ChatService.this.last_count = 1;
			ChatService.this.contact_count = 1;
			ChatService.this.last_address = "";
			
		}
		
	
	
	private void addMessageNotification(Contact _contact,Message _msg)
	{
		
		CharSequence contentTitle;
		CharSequence contentText;
		if (!last_address.equals(_contact.getAddress()))
		{
			contentText = _msg.getMessageText();
			contentTitle = "Neue Nachricht von " + _contact.getShowname();
			if (last_count != 1)
			{
				
			}
		}
		else
		{
			last_count++;
			contentText = _msg.getMessageText();
			contentTitle = last_count + " neue Nachrichten von " + _contact.getShowname();
			mNotificationManager.cancel(notification_id-1);
		}
		
		int icon = R.drawable.notification;
		long when = System.currentTimeMillis();
	
		Notification notification = new Notification(icon,contentTitle,when);	
		
		Intent notificationIntent = new Intent(this, SingleChat.class);
		notificationIntent.putExtra("service_called", 1);
		notificationIntent.putExtra("contact", _contact);
		Log.v("ChatService","Sent notification id=" + notification_id);
		notificationIntent.putExtra("kill_notification_id", notification_id);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		
		Intent deleteIntent = new Intent(this, BroadcastReceiver.class);
		deleteIntent.putExtra("reset_notification_count", 1);
		notification.deleteIntent = PendingIntent.getService(this, 0, deleteIntent, 0);
		
				
		mNotificationManager.notify(notification_id, notification);
		last_address = _contact.getAddress();
		notification_id++;
		
	}
    
}
