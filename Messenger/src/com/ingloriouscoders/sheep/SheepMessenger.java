package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SheepMessenger extends Activity {
	
	private ImageView image;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        ContactBox conbox = (ContactBox) findViewById(R.id.contactbox);
        
        conbox.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View view) {
	                Intent myIntent = new Intent(view.getContext(), SingleChat.class);
	                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	                startActivityForResult(myIntent, 0);
	                
	                overridePendingTransition(R.anim.enterfromright, R.anim.leavetoleft);
	                

            	}
        });
   }
    
}