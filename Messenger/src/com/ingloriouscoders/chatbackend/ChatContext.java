package com.ingloriouscoders.chatbackend;

import java.util.List;
import java.util.Vector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.packet.Presence;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class ChatContext {
	
	private String mUsername = "";
	private String mUserShowname = "";
	
	private String mPassword = "";
	
	private String mError = "";
	private boolean initiated = false;
	
	private XMPPConnection mConnection;
	
	private static List<Conversation> activeConversations = new Vector<Conversation>(); 
	
	public final static String default_server = "talk.google.com";
	public final static int default_port = 5222 ;
	public final static String default_resource = "SHEEP_MOBILE1";
	
	private static ChatContext mInstance;
	
	public static ChatContext getChatContext(String _username,String _password)
	{
		if (mInstance == null)
		{
			Log.v("chatbackend","Context newly created");
			mInstance = new ChatContext(_username,_password);
			return mInstance;
		}
		if (mInstance.getUsername() != _username)
		{
			Log.v("chatbackend","Context is there but with another user");
			mInstance.disconnect();
			mInstance = new ChatContext(_username,_password);
			return mInstance;
		}
		Log.v("chatbackend","Context got from Ram");
		return mInstance;		
	}
	private ChatContext(String username,String password)
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
		
		
		
		return true;
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
		XMPPConnection.DEBUG_ENABLED = true;		
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
	public final List<Conversation> getActiveConversations()
	{
		return activeConversations;
	}
	
	public boolean etablishConversation(Conversation _conv)
	{
		ChatManager cm = mConnection.getChatManager();
		_conv.mChat = cm.createChat(_conv.getOpposite().getAddress(),_conv.smack_listener);
		return true;
	}
	
}
