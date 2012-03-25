package com.ingloriouscoders.sheep;

import java.util.List;
import java.util.Vector;

import com.ingloriouscoders.chatbackend.Message;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;

import android.view.Window;

import android.widget.ImageView;

import android.view.Menu;
import android.view.MenuInflater;

public class SingleChat extends FragmentActivity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.singlechat);
        
        GridView gridview = (GridView) findViewById(R.id.messagesGridView);

               MessageAdapter msga = new MessageAdapter(this);
               gridview.setAdapter(msga);
               
               
               String[] testwords = {"hallo","ich","bin","ja","nein","doch","aber","idiot","hey!","lieber","ist","luca","clemens","dome","jan","schule","bier","bitte","ein","eine","arsch","tschüss",".","scheiße","nicht"};
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
               }

   }
    
    @Override
    public void onPause() {
        super.onPause();
        
        overridePendingTransition(R.anim.enterfromleft, R.anim.leavetoright);
   }
}