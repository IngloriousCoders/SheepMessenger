package com.ingloriouscoders.sheep;

import android.app.Activity;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;

public class contactBox extends View {
	public contactBox(Context context){

		super(context);

	}

	

	public contactBox(Context context, AttributeSet attr){

		super(context,attr);

	}

	
	public contactBox(Context context, AttributeSet attr, int defaultStyles){

		super(context, attr, defaultStyles);

	}
	
	@Override
	protected void onMeasure(int width,int height)
	{
		int measuredWidth = MeasureSpec.getSize(width);

		int measuredHeight = MeasureSpec.getSize(height);
		
		setMeasuredDimension( measuredWidth, measuredHeight);
		
		setMeasuredDimension( measuredWidth, measuredHeight);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint borderPaint = new Paint();
		
		borderPaint.setColor(Color.WHITE);
		
		int border_radius = 100;
		
		canvas.drawArc(new RectF(0,0,border_radius,border_radius)
						, 180, 90, true, borderPaint);
	}
	
	
}
