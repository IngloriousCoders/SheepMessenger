package com.ingloriouscoders.chatbackend;

import java.util.List;
import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.pubsub.packet.PubSubNamespace;
import org.jivesoftware.smackx.pubsub.provider.SubscriptionProvider;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class ChatContext {
	
	private String mUsername = "";
	private String mUserShowname = "";
	
	private String mPassword = "";
	
	private String mError = "";
	private boolean initiated = false;
	
	private XMPPConnection mConnection;
	
	private static List<Conversation> activeConversations = new ArrayList<Conversation>(); 
	
	public final static String default_server = "talk.google.com";
	public final static int default_port = 5222 ;
	public final static String default_resource = "SHEEP_MOBILE1";
		
	private List<OnNewMessageListener> mListeners = new ArrayList<OnNewMessageListener>();
	
	public ChatContext(String username,String password)
	{
		mUsername = username;
		mUserShowname = username;
		mPassword = password;
		
		initConnection(username, password, default_server, default_port);
	}
	public void setUserShowname(String _showname)
	{
		mUserShowname = _showname;
	}
	public boolean reconnect()
	{
		return true;
	}
	public boolean isConnected()
	{
		if (mConnection == null)
		{
			return false;
		}
		return mConnection.isConnected();
	}
	public boolean connect()
	{
		if (!initiated){
			return false;
		}
		if (mConnection.isConnected())
		{
			return false;
		}
		try
		{
			mConnection.connect();
		}
		catch (XMPPException e)
		{
			mError = "Verbindungsfehler:" + e.getMessage();
			return false;
		}	
		ProviderManager pm = ProviderManager.getInstance();

		// Add extension provider
		pm.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
		
		try
		{
			mConnection.login(mUsername,mPassword);
			
		}
		catch (XMPPException e)
		{
			mError = "Login Fehler:" + e.getMessage();
			return false;
		}
		catch (NetworkOnMainThreadException e)
		{
			mError = e.getMessage();
			return false;			
		}
		final ChatContext thisctx = this;
		ChatManager chatmanager = mConnection.getChatManager();
		chatmanager.addChatListener(
			    new ChatManagerListener() {
			        @Override
			        public void chatCreated(Chat chat, boolean createdLocally)
			        {
			            if (!createdLocally)
			            {
			            	Contact newcontact = Contact.getPlaceholder();
			            	newcontact.setAddress(chat.getParticipant());
			            	Log.v("chatbackend","Chat Prompet from address" + newcontact.getAddress());
			            	Conversation newconv = Conversation.spawnConversation(newcontact, thisctx);
			            	newconv.mChat = chat;
			            	newconv.mChat.addMessageListener(newconv.smack_listener);
			            }
			                
			        }
			    });
		
		
		return true;
	}
	public void setStatus(String status)
	{
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		// Send the packet (assume we have a Connection instance called "con").
		mConnection.sendPacket(presence);
	}
	public boolean setAccount(String username,String password)
	{
		if (!disconnect())
		{
			return false;
		}
		
		
		return true;
	}
	public boolean disconnect()
	{
		if (mConnection != null && mConnection.isConnected())
		{
			mConnection.disconnect();
		}
		return true;
	}
	public String getUsername()
	{
		return mUsername;
	}
	public String getUserShowname()
	{
		return mUserShowname;
	}
	
	private boolean initConnection(String username,String password,String server_address,int server_port)
	{
		if (server_address == null)
		{
			server_address = default_server;
		}
		if (server_port == -1)
		{
			server_port = default_port;
		}
        
		
		ConnectionConfiguration config = new ConnectionConfiguration(server_address,server_port,"googlemail.com");
		
		config.setKeystoreType("bks");
		SASLAuthentication.unsupportSASLMechanism("PLAIN");
		mConnection = new XMPPConnection(config);
		config.setSASLAuthenticationEnabled(true); 
		initiated = true;
		return initiated;
	}
	public String getLastErrorMessage()
	{
		return mError;
	}
	public boolean isInitiated()
	{
		return initiated;
	}
	public List<Conversation> getActiveConversations()
	{
		return activeConversations;
	}
	public boolean etablishConversation(Conversation _conv)
	{
		ChatManager cm = mConnection.getChatManager();
		if (_conv.getOpposite().getAddress() == null)
		{
			Log.v("chatbackend","Address is " + _conv.getOpposite().getAddress());
			Log.v("chatbackend","Username is " + _conv.getOpposite().getUsername());
			Log.v("chatbackend","Conversation address is null");
			return false;
		}
		//
		_conv.mChat = cm.createChat(_conv.getOpposite().getAddress(),_conv.smack_listener);
		//Log.v("chatbackend","Conversation with" + _conv.getOpposite().getShowname() + " etablished successfully");
		activeConversations.add(_conv);
		return true;
	}
	public void addOnNewMessageListener(OnNewMessageListener _listener)
	{
		mListeners.add(_listener);
	}
	public void removeOnNewMessageListener(OnNewMessageListener _listener)
	{
		mListeners.remove(_listener);
	}
	
	protected OnNewMessageListener childNewMessageListener = new OnNewMessageListener() {
		
		@Override
		public void onNewMessage(Conversation _conversation, Message _newmessage) {
			for ( OnNewMessageListener currentListener : ChatContext.this.mListeners )
			{
				currentListener.onNewMessage(_conversation,_newmessage);
			}
			
		}
	};
	
	
}
