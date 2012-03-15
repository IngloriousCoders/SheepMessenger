package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.content.res.TypedArray;
import android.util.*;

public class MessageBubble extends View {
	
	public MessageBubble(Context context){
		super(context);
	}

	

	public MessageBubble(Context context, AttributeSet attr){
		super(context,attr);
	}

	
	public MessageBubble(Context context, AttributeSet attr, int defaultStyles){
		super(context, attr, defaultStyles);
	}
	
	@Override
	protected void onMeasure(int width,int height)
	{
		int measuredWidth = MeasureSpec.getSize(width);
		int measuredHeight = MeasureSpec.getSize(height);
		setMeasuredDimension( measuredWidth, measuredHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		//TODO: Attribute an Custom Widget uebergeben?!
		//TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MessageBubble, 0, 0);
		
		Paint borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		
		
		
		//TODO: Uebergabe der Farbe ans Widget
		borderPaint.setColor(Color.rgb(100, 130, 255));
		
		int round_radius = 10;
		canvas.drawRoundRect(new RectF(0,0,380,100), round_radius, round_radius, borderPaint);
		
		Path path = new Path();
		path.moveTo(380, 20);
		path.lineTo(390, 30);
		path.lineTo(380, 40);
		path.close();
		
		canvas.drawPath(path, borderPaint);
	}
	
	
}
