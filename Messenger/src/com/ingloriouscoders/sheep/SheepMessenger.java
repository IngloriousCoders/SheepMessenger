package com.ingloriouscoders.sheep;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class SheepMessenger extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        ContactBox conbox = (ContactBox) findViewById(R.id.contactbox);
        
        conbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Log.v("Custom","pressed");
                Intent myIntent = new Intent(view.getContext(), SingleChat.class);
                startActivityForResult(myIntent, 0);
            }
        });
   }
}