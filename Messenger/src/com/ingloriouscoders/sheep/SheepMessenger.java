package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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

import java.util.Vector;
import java.util.List;
import android.app.Fragment;


public class SheepMessenger extends FragmentActivity {
	
	private ImageView image;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        
        
        StartAdapter viewpager_adp = new StartAdapter(getSupportFragmentManager(),this);
        viewpager_adp.addPage(ContactFragment.class.getName());
        viewpager_adp.addPage(GroupFragment.class.getName());
        
        ViewPager vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(viewpager_adp);
        
        
       // Onclick listener Code wurde in ContactAdapter.java ausgelagert
   }
    
}