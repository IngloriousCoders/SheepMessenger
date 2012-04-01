package com.ingloriouscoders.sheep;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnServiceNewMessageListener;
import com.ingloriouscoders.chatbackend.ServiceChatContext;
import com.ingloriouscoders.chatbackend.ServiceConversation;

import java.util.ArrayList;
import java.util.List;

public class ContactStated extends Contact {

	
	private OnServiceNewMessageListener mListener = new OnServiceNewMessageListener.Stub()
	{
		@Override
		public void onNewMessage(ServiceConversation _conversation,
				Message recieved_message) throws RemoteException {
			if ( !ContactStated.this.opened )
			{
				
				ContactStated.this.unreadCount++;
				Log.v("Contact","Der Kontakt " + ContactStated.this.getShowname() + " hat dir eine Nachricht geschrieben. count=" + ContactStated.this.unreadCount + ",size=" + ContactStated.this.dataChangeListeners.size());
				for (OnContactStatedDataChanged listener : ContactStated.this.dataChangeListeners)
				{
					listener.dataChanged(ContactStated.this);
				}
			
			}
		}
		
	};
	
	private ServiceConversation mConversation = null;
	private ServiceChatContext mContext = null;
	private List<OnContactStatedDataChanged> dataChangeListeners = new ArrayList<OnContactStatedDataChanged>();
	
	private int unreadCount = 0;
	private boolean opened = false;
	
	private boolean valid = true;
	
	public ContactStated(String _username,String address,ServiceChatContext context) {
		super(_username);
		initiateConnections();
		mContext = context;
						
	}
	public ContactStated(Contact _origin,ServiceChatContext context)
	{
		super(_origin.getUsername(),_origin.getShowname(),_origin.getPhotoURI(),_origin.getAddress());
		mContext = context;
		initiateConnections();
	}
	private void initiateConnections()
	{
		try
		{
			mConversation = mContext.spawnConversation((Contact)this);
		}
		catch ( RemoteException e )
		{
			valid = false;
			return;
		}
		try
		{
			mConversation.addOnServiceNewMessageListener(mListener);
		}
		catch ( RemoteException e )
		{
			valid = false;
			return;
		}
	}
	
	public boolean getValidity()
	{
		return valid;
	}
	public void setOpenState(boolean state)
	{
		this.opened = state;
	}
	public boolean getOpenState()
	{
		return this.opened;
	}
	public void resetUnreadCount()
	{
		this.unreadCount = 0;
	}
	public int getUnreadMessages()
	{
		return this.unreadCount;
	}
	
	public void addOnContactStatedDataChangedListener(OnContactStatedDataChanged _listener)
	{
		dataChangeListeners.add(_listener);
	}
	public void removeOnContactStatedDataChangedListener(OnContactStatedDataChanged _listener)
	{
		dataChangeListeners.remove(_listener);
	}
	
	public ServiceConversation getConversation()
	{
		return mConversation;
	}
	
	public void setContext(ServiceChatContext _ctx)
	{
		this.mContext = _ctx;
		initiateConnections();
	}
	
	//Parcelable Zeugs
	

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	   super.writeToParcel(dest, flags);
 
	   dest.writeBinderArray( new IBinder[] { mContext.asBinder() } );

	   dest.writeInt(unreadCount);
	   dest.writeInt(opened ? 0 : 1);
	}	
	public static final Parcelable.Creator<ContactStated> CREATOR
	    = new Parcelable.Creator<ContactStated>() {
	public ContactStated createFromParcel(Parcel in) {
	    return new ContactStated(in);
	}
	
	public ContactStated[] newArray(int size) {
	    return new ContactStated[size];
	}
	};
	
	private ContactStated(Parcel in) {
		super(in);
		
		IBinder[] binders = new IBinder[1];
		in.readBinderArray(binders);
		mContext = ServiceChatContext.Stub.asInterface(binders[0]);
		unreadCount = in.readInt();
		opened = in.readInt() == 0;
		initiateConnections();
		
		mContext = null;
		mConversation = null;
	}
	//Parcelable Zeugs ENDE

}
