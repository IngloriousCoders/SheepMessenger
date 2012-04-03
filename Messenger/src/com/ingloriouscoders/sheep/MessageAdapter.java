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
import com.ingloriouscoders.chatbackend.OnContactDataChanged;
import com.ingloriouscoders.chatbackend.OnNewMessageListener;

import android.widget.GridView;
import android.os.Parcelable;

import android.content.Intent;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.util.Log;

public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    public List<Message> mMessageBubble = new ArrayList<Message>();
    protected OnNewMessageListener msgListener;
    int debug_Reads = 0;
    public MessageView lview; public Message msg;

    public MessageAdapter(Context c) {
        mContext = c;
    }

    public void addMessage(Message _msg)
    {
    	mMessageBubble.add(_msg);
    	this.notifyDataSetChanged();
    }
    
    public void addScrolledMessage(Message _msg, MessageView _lview)
    {
    	mMessageBubble.add(_msg);
		this.lview = _lview;
		this.msg = _msg;
		

		    	lview.smoothScrollBy(lview.getCount() * 500, lview.getCount() * 1000);
		    	lview.getMessageAdapter().notifyDataSetChanged();
		    	lview.updateGraphics(msg);
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
    	//thismessage.setOnNewMessageListener(msgListener);

    	if (convertView == null)
    	{
    		   	
        	mb = new MessageBubble(mContext);
        	mb.setMessage(thismessage.getSender(), thismessage.getMessageText());
        	mb.setGraphics(thismessage.getIncoming(), thismessage.getColor());
        	//mb.giveParameters(thismessage.getParameters());
        	//System.out.println(thismessage.getParameters()[0]);
        	mb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,mb.calculateHeight()+mb.bubbleoffset));
    	}
    	else
    	{
    		mb = (MessageBubble)convertView;
    		mb.setMessage(thismessage.getSender(), thismessage.getMessageText());
    		mb.setGraphics(thismessage.getIncoming(), thismessage.getColor());
    		//mb.giveParameters(thismessage.getParameters());
    		mb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,mb.calculateHeight()+mb.bubbleoffset));	
    	}
    	
    	//DEBUG:
    	mb.setReads(7, 0);
    	
    	final MessageAdapter ma = this;
    	
    	/*mb.setOnLongClickListener(new View.OnLongClickListener() {
        	public boolean onLongClick(View view) {

                return false;
         	}
    	});*/
    	
    	mb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		//DEBUG:
        		if(debug_Reads <= 7) {
        			debug_Reads++;
        			mb.setReads(7, debug_Reads);
        		}
         	}
    	});
    	
    	/*mb.setOnTouchListener(new View.OnTouchListener() {
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