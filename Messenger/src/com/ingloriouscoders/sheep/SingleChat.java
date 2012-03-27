package com.ingloriouscoders.sheep;

import java.util.List;
import java.util.Vector;

import com.ingloriouscoders.chatbackend.ChatContext;
import com.ingloriouscoders.chatbackend.Contact;
import com.ingloriouscoders.chatbackend.Conversation;
import com.ingloriouscoders.chatbackend.Message;
import com.ingloriouscoders.chatbackend.OnNewMessageListener;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
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

public class SingleChat extends FragmentActivity {
    /** Called when the activity is first created. */
	Conversation mConversation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.singlechat);
        
        Intent startIntent = this.getIntent();
        Bundle extras = startIntent.getExtras();
        String username = extras.getString("username");
        String showname = extras.getString("showname");
        String address = extras.getString("address");
        String photoURI = extras.getString("photoURI");
        
        
         Debug dbg = Debug.getInstance(); 
        
        if (username == null ||
        	showname == null ||
        	address == null ||
        	photoURI == null)
        {
        	dbg.out("Some arguments were strange");
        	finish();
        }
        
        Contact opposite = new Contact(username,showname,photoURI);
        opposite.setAddress(address);
               
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ChatContext ctx = ChatContext.getChatContext(prefs.getString("account_username", ""), prefs.getString("account_password", ""));
        
        if (!ctx.isInitiated())
    	{
    		dbg.out("Fehler beim initiieren:" + ctx.getLastErrorMessage());
    		return;
    	}
    	if (!ctx.isConnected() && !ctx.connect())
    	{
    		dbg.out("Fehler beim verbinden:" + ctx.getLastErrorMessage());
    		return;
    	}
        
        final GridView gridview = (GridView) findViewById(R.id.messagesGridView);
        final MessageAdapter msga = new MessageAdapter(this);
        gridview.setAdapter(msga);
 
        mConversation = Conversation.spawnConversation(opposite, ctx);
          
        List<Message> history = mConversation.getHistory(0);
        for (int i=0;i<history.size();i++)
        {
        	msga.addMessage(history.get(i));
        }
                
        mConversation.setOnNewMessageListener(new OnNewMessageListener() {
			
			@Override
			public void onNewMessage(Conversation _conversation, Message _newmessage) {
				_newmessage.setColor(Color.rgb(109,217,110));
				msga.addMessage(_newmessage);	
				Log.v("SingleChat","Message recieved");
			}
		});
        final Activity ownact = this;

        
        
        Button sendbutton = (Button)findViewById(R.id.send_button);
        sendbutton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SingleChat myact = ((SingleChat)ownact);
				Conversation myconv = myact.mConversation;
				if (myconv == null)
				{
					return;
				}
				EditText sendfield = (EditText)myact.findViewById(R.id.msg_field);
				Editable content = sendfield.getText();
				sendfield.setText("");
				if (content.toString() == "")
				{
					return;
				}				
				Message msg = myconv.prepareMessage();
				msg.setColor(Color.rgb(29, 106, 115));
				msg.setMessageText(content.toString());
				msg.send();	
				msga.addMessage(msg);
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
        
               /*String[] testwords = {"hallo","ich","bin","ja","nein","doch","aber","idiot","hey!","lieber","ist","luca","clemens","dome","jan","schule","bier","bitte","ein","eine","arsch","tschüss",".","scheiße","nicht"};
               String[] testSenders = {"Luca","Clemens","Dome","Jan"};
               int[] colors = {Color.rgb(29, 106, 115),Color.rgb(50,166,166),Color.rgb(81,191,143),Color.rgb(109,217,110)};
               
               for(int i=0;i<100;i++) {
            	   String testmessage = "";
            	   for(int a=0;a<(int) (Math.random()*30+4);a++) {
            		   testmessage = testmessage + testwords[(int) (Math.random()*testwords.length)] + " ";
            	   }
            	   int randomint = (int) (Math.random()*4);
            	   String testsender = testSenders[randomint];
            	   boolean incoming;
            	   
            	   if (randomint==3)
            		   incoming = false;
            	   else 
            		   incoming = true;
            	   
	                 //Syntax: Message foo = new Message(String nachricht, String absender, boolean ankommend, int farbe);
	                Message msg = new Message(testmessage,testsender, incoming, colors[randomint]);
	                
	                msga.addMessage(msg);
               }*/

   }
    
    @Override
    public void onPause() {
        super.onPause();
        
        overridePendingTransition(R.anim.enterfromleft, R.anim.leavetoright);
   }
}