package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.util.Log;

public class SheepMessenger extends Activity {
	
	private ImageView image;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        GridView gridview = (GridView) findViewById(R.id.contactGridView);
        ContactAdapter adp = new ContactAdapter(this);
        gridview.setAdapter(adp);
        for(int i=0;i<100;i++)
        {
        	// new Contact ( username,anzeigename )
        	Contact contact = new Contact("username" + i,"Person "+i);
        	adp.addContact(contact);
        }
        
       // Onclick listener Code wurde in ContactAdapter.java ausgelagert
   }
    
}