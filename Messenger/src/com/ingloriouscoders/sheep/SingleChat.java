package com.ingloriouscoders.sheep;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.ingloriouscoders.chatbackend.ChatContext;
import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Conversation;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnContactDataChanged;
import com.ingloriouscoders.chatbackend.OnNewMessageListener;
import com.ingloriouscoders.chatbackend.OnServiceNewMessageListener;
import com.ingloriouscoders.chatbackend.ServiceChatContext;
import com.ingloriouscoders.chatbackend.ServiceConversation;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ActionBar.Tab;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView	;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.Window;

import android.widget.ImageView;

import android.view.Menu;
import android.view.MenuInflater;

import com.ingloriouscoders.chatbackend.Conversation;

import com.ingloriouscoders.util.Debug;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.OnEditorActionListener;

import android.widget.Toast;

public class SingleChat extends FragmentActivity {
    /** Called when the activity is first created. */
	private ServiceConversation mConversation;
	private OnServiceNewMessageListener mListener;
	
	protected ChatService mChatService;
	protected ServiceChatContext mServiceChatContext;
	private ActionBar mActionBar;
	
	int mNotificationIDToKill = -1;
	
	private ContactStated mOpposite;
	
	final static int default_incomingColor = Color.rgb(50,166,166);
	final static int default_outgoingColor = Color.rgb(109,217,110);
	
	MessageAdapter msga;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        
        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        ab.setDisplayHomeAsUpEnabled(true);
        mActionBar = ab;
        
        setContentView(R.layout.singlechat);
        
        
        
        
   }
        
public void onServiceConnected()
{
	try
	{
		mConversation = mServiceChatContext.spawnConversation(mOpposite);
		this.mServiceChatContext.resetNotificationCounter();
		if (this.mNotificationIDToKill != -1)
		{
			this.mServiceChatContext.killNotification(this.mNotificationIDToKill);
		}
	}
	catch (RemoteException e)
	{
		Log.v("SingleChat","Konversation konnte nicht geladen werden");
		return;
	}
	
	
	
	final MessageView listview = (MessageView) findViewById(R.id.messageslistview);
	msga = listview.getMessageAdapter();
	
        try
        {
        	List<Message> history = mConversation.getHistory();
        	for (int i=0;i<history.size();i++)
        	{
        		Message msg = history.get(i);
        		if (msg.getIncoming())
        		{
        			msg.setColor(default_incomingColor);
        		}
        		else 
        		{
        			msg.setColor(default_outgoingColor);
        		}
        		msga.addMessage(msg);
        	}
        	if (history.size() == 0)
        	{
        		Message first = new Message();
        		msga.addMessage(first);
        		//msga.removeMessage(first);
        	}
        }
        catch ( RemoteException e)
        {
        	Log.v("SingleChat","Der Verlauf konnte nicht geladen werden. Serviceverbindungsproblem");
        }
        
        
        
        mListener = new OnServiceNewMessageListener.Stub()
        {
			@Override
			public void onNewMessage(ServiceConversation _conversation,
					Message recieved_message) throws RemoteException {
				Log.v("SingleChat","Message recieved");
				recieved_message.setColor(default_incomingColor);
				msga.addScrolledMessage(recieved_message, listview);			
			}
        	
        };
		try
		{
			mConversation.removeOnServiceNewMessageListener(mListener);
        	mConversation.addOnServiceNewMessageListener(mListener);
		}
		catch (RemoteException e)
		{
			Log.v("SingleChat","Fehler, der OnServiceNewMessageListener konnte nicht geaddet werden.");
		}
		
        
        final Activity ownact = this;

        
        
        Button sendbutton = (Button)findViewById(R.id.send_button);
        sendbutton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SingleChat myact = ((SingleChat)ownact);
				ServiceConversation myconv = myact.mConversation;
				if (myconv == null)
				{
					return;
				}
				EditText sendfield = (EditText)myact.findViewById(R.id.msg_field);
				Editable content = sendfield.getText();
				sendfield.setText("");
				if (content.toString().equals("") )
				
				{
					return;
				}
				Message msg = new Message();
				try
				{
					Calendar c = Calendar.getInstance();
					String timestampStr = "&time=" + String.valueOf(c.getTimeInMillis());
					String parameters = timestampStr;
					
					msg = myconv.prepareMessage(parameters, false);
				}
				catch (RemoteException e)
				{
					Log.v("SingleChat","Fehler beim Vorbereiten der Nachricht ( lokal )");
				}
				finally
				{
					
					msg.setColor(default_outgoingColor);
					msg.setMessageText(content.toString());
					
					try
					{
						Calendar c = Calendar.getInstance();
						String timestampStr = "&time=" + String.valueOf(c.getTimeInMillis());
						String parameters = timestampStr;
						
						//NEUER SYNTAX: foo.sendMessage(Message nachricht, boolean internal, String[] parameter, boolean timestamp);
						myconv.sendMessage(msg, false, parameters, true);
					}
					catch (RemoteException e)
					{
						Log.v("SingleChat","Fehler beim Senden der Nachricht ( lokal )");
					}
					
					try {
						msga.addScrolledMessage(msg, listview);
					} catch (RemoteException e) {
						//e.printStackTrace();
					}
				}
			}
		});
        EditText sendfield = (EditText)findViewById(R.id.msg_field);
        sendfield.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				SingleChat myact = ((SingleChat)ownact);
				Button sendbutton = (Button)myact.findViewById(R.id.send_button);
				sendbutton.callOnClick();
				return false;
			}
		});

   }

    public void onDestroy()
    {
    	super.onDestroy();
    }	
    @Override
    public void onPause() {
        super.onPause();
        
       
        this.mOpposite.setOpenState(false);               
	   	try
	   	{
	   		this.mConversation.removeOnServiceNewMessageListener(mListener);
	   	}
	   	catch( RemoteException e)
	   	{
	   		
	   	}
	   	unbindService(mConnection);
        overridePendingTransition(R.anim.enterfromleft, R.anim.leavetoright);
   }
    
    @Override
    public void onStart() {
    	super.onStart();
     }
    @Override
    public void onResume() {
    	super.onResume();
    	
    	
    	Intent startIntent = this.getIntent();
        Bundle extras = startIntent.getExtras();
        
        if (extras.getInt("service_called") == 1)
        {
        	mOpposite = ContactStatedManager.addContact(new ContactStated( (Contact)extras.getParcelable("contact") ) );
        	if (mOpposite == null)
        	{
        		Toast.makeText(SingleChat.this, "Fehler: Der Kontakt konnte nicht geladen werden",Toast.LENGTH_SHORT).show();
        		finishActivity(0);
        	}
	        if (extras.containsKey("kill_notification_id"))
	        {
	        	
        		mNotificationIDToKill = extras.getInt("kill_notification_id");
        		Log.v("SingleChat","Got notification id to kill " + mNotificationIDToKill);
	        }
        }
        else
        {
        	int contactID = extras.getInt("contact_id");
        	mOpposite = ContactStatedManager.getContact(contactID);
        } 	
        if (mOpposite == null)
        { 
        	Log.v("SingleChat","Opposite is null");
        	finishActivity(0);
        }
        
        mActionBar.setTitle("Chat mit " + mOpposite.getShowname());   
        
                               
                
        final MessageView listview = (MessageView) findViewById(R.id.messageslistview);
        listview.smoothScrollBy(listview.getCount() * 500, 1000);
        listview.setDivider(null);
        
        final MessageAdapter msga = listview.getMessageAdapter();
        //final MessageAdapter msga = new MessageAdapter(this);
        //listview.setAdapter(msga);
        this.mOpposite.resetUnreadCount();
        this.mOpposite.setUnreadMessages(0);
        this.mOpposite.setOpenState(true);
        startChatService();
    	
    }
    
   @Override
   public void onStop() {
	   super.onStop();
	   this.mOpposite.setOpenState(false);
	   	try
	   	{
	   		this.mConversation.removeOnServiceNewMessageListener(mListener);
	   	}
	   	catch( RemoteException e)
	   	{
	   		
	   	}
	   	//unbindService(mConnection);
   }
   public void startChatService()
   {
   	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
   	
   	Intent serviceIntent = new Intent(this, ChatService.class);
   	this.startService(serviceIntent);
   	
   	
   	bindService(serviceIntent, mConnection,0);
   	
   }
   private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
		    mServiceChatContext = ServiceChatContext.Stub.asInterface(service);
		    if (mServiceChatContext == null)
		    {
		    	Log.v("","ChatContext = 0");
		    	return;
		    }
		    //SingleChat.this.mOpposite.setContext(mServiceChatContext);
		    SingleChat.this.onServiceConnected();
		}

		public void onServiceDisconnected(ComponentName className) {
			mChatService = null;
			mServiceChatContext = null;
		}
	};
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
    	switch (item.getItemId()) {
        	case android.R.id.home:
        		finish();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
    	}
   }
}
