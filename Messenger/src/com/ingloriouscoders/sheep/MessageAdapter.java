package com.ingloriouscoders.sheep;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout.LayoutParams;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;

import com.ingloriouscoders.chatbackend.Message;

import android.widget.GridView;
import android.os.Parcelable;

import android.content.Intent;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.util.Log;

public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Message> mMessageBubble = new ArrayList<Message>();

    public MessageAdapter(Context c) {
        mContext = c;
    }

    public void addMessage(Message _msg)
    {
    	mMessageBubble.add(_msg);
    	this.notifyDataSetChanged();
    }
    
    public void addScrolledMessage(Message _msg, ListView lview)
    {
    	mMessageBubble.add(_msg);
		lview.smoothScrollBy(1000, MessageBubble.calculateHeight(_msg.getMessageText()));
    	this.notifyDataSetChanged();
    }
    
    public void removeMessage(Message _msg)
    {
    	if (mMessageBubble.contains(_msg))
    	{
    		mMessageBubble.remove(_msg);
    		this.notifyDataSetChanged();
    	}
    	
    }
    
    public int getCount() {
        return mMessageBubble.size();
    }

    public Object getItem(int position) {
        Message obj = mMessageBubble.get(position);
        return obj;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	final MessageBubble mb;
    	final Message thismessage = mMessageBubble.get(position);

    	if (convertView == null)
    	{
    		   	
        	mb = new MessageBubble(mContext);
        	mb.setMessage(thismessage.getSender(), thismessage.getMessageText());
        	mb.setGraphics(thismessage.getIncoming(), thismessage.getColor());
        	mb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,mb.calculateHeight()+mb.bubbleoffset));
        	
    	}
    	else
    	{
    		mb = (MessageBubble)convertView;
    		mb.setMessage(thismessage.getSender(), thismessage.getMessageText());
    		mb.setGraphics(thismessage.getIncoming(), thismessage.getColor());
    		mb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,mb.calculateHeight()+mb.bubbleoffset));
    		
    	}
    	
    	final MessageAdapter ma = this;
    	
    	/*mb.setOnLongClickListener(new View.OnLongClickListener() {
        	public boolean onLongClick(View view) {

                return false;
         	}
    	});
    	
    	mb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		mb.clicked = !mb.clicked;
         	}
    	});
    	
    	mb.setOnTouchListener(new View.OnTouchListener() {
    	    public boolean onTouch(View v, MotionEvent event) {
    	        if(event.getAction() == MotionEvent.ACTION_DOWN) {
    	            mb.clicked = true;
    	        } else if (event.getAction() == MotionEvent.ACTION_UP) {
    	            mb.clicked = false;
    	        }
    	        return true;
    	    }
    	});*/

    	
    	
    	return mb;
    }

}