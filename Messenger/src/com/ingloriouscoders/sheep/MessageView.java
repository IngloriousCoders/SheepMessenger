package com.ingloriouscoders.sheep;

import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Conversation;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnContactDataChanged;
import com.ingloriouscoders.chatbackend.OnNewMessageListener;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MessageView extends ListView {

	private boolean adapter_set = false;
	public MessageAdapter adp;
	public MessageView(Context ctx)
	{
		super(ctx);
		initAdapter();
		
	}
	public MessageView(Context ctx,AttributeSet attrs)
	{
		super(ctx,attrs);
		initAdapter();
	}
	public MessageView(Context ctx,AttributeSet attrs,int defStyle)
	{
		super(ctx,attrs,defStyle);
		initAdapter();
	}
	private void initAdapter()
	{
		adp = new MessageAdapter(getContext());
		this.setAdapter(adp);
		((MessageAdapter)adp).msgListener = mListener;
	}

	private final MessageView thisobj = this;
	public OnNewMessageListener mListener = new OnNewMessageListener() {
		
		@Override
		public void onNewMessage(Conversation _conversation,Message _newmessage) {						

		}
	};
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		if (!adapter_set)
		{
			super.setAdapter(adapter);
			adapter_set = true;
		}
	}
	
	public MessageAdapter getMessageAdapter() {
		return adp;
	}
	
	public void updateGraphics(Message _message) {

		//this.setAdapter(adp);
		//Log.v("Custom","invalidating: " + _message);
		
		/*if (this.getAdapter() != null)
		{
			MessageAdapter adp = (MessageAdapter)this.getAdapter();
			int index = adp.mMessageBubble.indexOf(_message);
			if (index != -1)
			{
				MessageBubble cb = (MessageBubble)this.getChildAt(index);
				this.invalidateChild(cb, null);
			}
		}*/
		this.invalidate();
	}
}
