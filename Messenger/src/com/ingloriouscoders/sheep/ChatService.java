package com.ingloriouscoders.sheep;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


import android.util.Log;

import com.ingloriouscoders.chatbackend.*;

public class ChatService extends Service {
    private NotificationManager mNM;
    private ChatContext mChatContext;
        
    private String username = "";
    private String password = "";
    private int unique_not_id = 1;
    private boolean showNotifications = false;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        ChatService getService() {
            return ChatService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        //showNotification("Service started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ChatService", "Received start id " + startId + ": " + intent);
        
        Bundle extras = intent.getExtras();
        username = extras.getString("username");
        password = extras.getString("password");
        
        
        return START_NOT_STICKY;
    }
    private void showNotification(String simpleText)
    {
    	showNotification(simpleText, simpleText, simpleText,null);
    }
    
    private void showNotification(String text,String title,String content,Intent targetIntent)
    {
    	
     	long when = System.currentTimeMillis();
    	int icon = android.R.drawable.ic_dialog_email;

    	Notification notification = new Notification(icon, text, when);
    	Context context = getApplicationContext();
    	CharSequence contentTitle = title;
    	CharSequence contentText = content;
    	
    	if (targetIntent == null)
    		notification.setLatestEventInfo(context, contentTitle, contentText, null);
    	else
    	{
    		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);
    		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	}
    		
    	mNM.notify(unique_not_id, notification);
    	unique_not_id++;
    }
    
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.

        // Tell the user we stopped.
    	Log.v("ChatService","ChatService gestoppt");
        Toast.makeText(this, "Service gestoppt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private final ChatService thisclass = this;
    OnNewMessageListener mListener = new OnNewMessageListener() {
		
		@Override
		public void onNewMessage(Conversation _conversation, Message _newmessage) {
			if (thisclass.showNotifications)
			{
				Intent notificationIntent = new Intent(thisclass, SingleChat.class);
				Contact opposite = _conversation.getOpposite();
				notificationIntent.putExtra("username",opposite.getUsername());
				notificationIntent.putExtra("showname",opposite.getShowname());
				notificationIntent.putExtra("address",opposite.getAddress());
				notificationIntent.putExtra("photoURI",opposite.getPhotoURI());
				thisclass.showNotification("Neue Nachricht von " + _conversation.getOpposite().getShowname() + ":","Neue Nachricht von " + _conversation.getOpposite().getShowname(),_newmessage.getMessageText(),notificationIntent);
			}
			
		}
	};
    
    public ChatContext getChatContext(String _username,String _password)
    {
    	if (mChatContext == null)
    	{
    		mChatContext = ChatContext.getChatContext(_username, _password);
    	}
    	
    	if (!mChatContext.isInitiated())
    	{
    		Log.v("ChatService","Fehler beim initiieren:" + mChatContext.getLastErrorMessage());
    		return null;
    	}
    	
    	
    	if (!mChatContext.connect())
    	{
    		Log.v("ChatService","Fehler beim verbinden:" + mChatContext.getLastErrorMessage());
    		return null;
    	}
    	
    	mChatContext.setOnNewMessageListener(mListener);
    	return this.mChatContext;
    }
    public void setShowNofication(boolean state)
    {
    	showNotifications = state;
    }
}