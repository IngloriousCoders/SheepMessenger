package com.ingloriouscoders.sheep;


import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import android.graphics.PorterDuff.Mode;
import android.view.InputEvent.*;
import android.util.Log;


public class ContactBox extends View {
	
	private Bitmap rounded_contact_bitmap;
	
	private int width = 300;
	private int height = 300;
	
	private int border_radius = 20;
	
	private boolean state_pressed = false;
	
	public ContactBox(Context context){

		super(context);
		initializeBitmap();

	}

	

	public ContactBox(Context context, AttributeSet attr){

		super(context,attr);
		initializeBitmap();

	}

	
	public ContactBox(Context context, AttributeSet attr, int defaultStyles){

		super(context, attr, defaultStyles);
		initializeBitmap();

	}
	
	private void initializeBitmap()
	{
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nocontact);
		
		int img_width = bitmap.getWidth();
		int img_height = bitmap.getHeight();
		
		rounded_contact_bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		
		Canvas init_canvas = new Canvas(rounded_contact_bitmap);
		
		Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		maskPaint.setColor(Color.RED);
		
		init_canvas.drawRoundRect(new RectF(1,1,width-1,height-1), border_radius, border_radius , maskPaint);
		
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		init_canvas.drawBitmap(bitmap,new Rect(0,0,img_width,img_height),new Rect(1,1,width-1,height-1),maskPaint);		
		
	}
	@Override
	protected void onMeasure(int width,int height)
	{
		int measuredWidth = MeasureSpec.getSize(width);

		int measuredHeight = MeasureSpec.getSize(height);
		
		
		
		setMeasuredDimension( measuredWidth, measuredHeight);
		
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
		initializeBitmap();
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.WHITE);
		
		if (state_pressed)
		{
			borderPaint.setColor(Color.BLACK);
			
		}
		
		canvas.drawRoundRect(new RectF(0,0,width,height), 20, 20, borderPaint);
		
		Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		if (rounded_contact_bitmap != null)
		{
			canvas.drawBitmap(rounded_contact_bitmap,0,0,bitmapPaint);
		}
		if (state_pressed)
		{
			Paint darkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			darkerPaint.setColor(Color.BLACK);
			darkerPaint.setAlpha(100);
			
			canvas.drawRoundRect(new RectF(0,0,width,height), 20, 20, darkerPaint);
			
		}
		
	}	
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		width = w;
		height = h;
		initializeBitmap();
	}
	@Override
	public boolean onTouchEvent(MotionEvent touch_event)
	{
		switch (touch_event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			state_pressed = true;
			// Invalidate tut alles redrawen
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			state_pressed = false;
			invalidate();
			callOnClick();
			return true;
		
		}
		
		
		return false;
	}
	
}
	

