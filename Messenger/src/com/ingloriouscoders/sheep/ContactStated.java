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

	
	protected OnServiceNewMessageListener mListener = new OnServiceNewMessageListener.Stub()
	{
		@Override
		public void onNewMessage(ServiceConversation _conversation,
				Message recieved_message) throws RemoteException {
			
			ContactStated ctc_stated = ContactStatedManager.getContact(_conversation.getOpposite().getAddress());
			if ( !ctc_stated.opened )
			{
				ctc_stated.unreadCount++;
				Log.v("Contact","Contact" + ctc_stated.mInstanceNumber + ": Der Kontakt " + ctc_stated.getShowname() + " hat dir eine Nachricht geschrieben. count=" + ctc_stated.unreadCount + ",size=" + ctc_stated.dataChangeListeners.size());
				for (OnContactStatedDataChanged listener : ctc_stated.dataChangeListeners)
				{
					listener.dataChanged(ctc_stated);
					
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
	
	protected int listID = -1;
	
	private static int instance_number = 0;
	private int mInstanceNumber;
	
	
	public ContactStated(String _username,String address,ServiceChatContext context) {
		super(_username);
		initiateConnections();
		mContext = context;
		mInstanceNumber = instance_number;
		instance_number++;
						
	}
	public ContactStated(Contact _origin,ServiceChatContext context)
	{
		super(_origin.getUsername(),_origin.getShowname(),_origin.getPhotoURI(),_origin.getAddress());
		mContext = context;
		initiateConnections();
	}
	
	public ContactStated(Contact _origin)
	{
		super(_origin.getUsername(),_origin.getShowname(),_origin.getPhotoURI(),_origin.getAddress());
	
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
			mConversation.removeOnServiceNewMessageListener(mListener);
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
		if (this.opened)
		{
			Log.v("ContactStated","Contact" + mInstanceNumber + ": Contact is now opened");
			this.unreadCount = 0;
		}
		else
			Log.v("ContactStated","Contact" + mInstanceNumber + ": Contact is now closed");
			
		if(this.mConversation != null) 
		{
			try
			{
				mConversation.setNotificationMuted(this.opened);
			}
			catch (RemoteException e)
			{
				
			}
		}

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
	public void setUnreadMessages(int count)
	{
		this.unreadCount = count;
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
 
	   //dest.writeBinderArray( new IBinder[] { mContext.asBinder() } );

	   dest.writeInt(unreadCount);
	   dest.writeInt(opened ? 0 : 1);
	   dest.writeInt(listID);
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
		//mContext = ServiceChatContext.Stub.asInterface(binders[0]);
		unreadCount = in.readInt();
		opened = in.readInt() == 0;
		initiateConnections();
		
		listID = in.readInt();
		
		mContext = null;
		mConversation = null;
	}
	//Parcelable Zeugs ENDE

}
