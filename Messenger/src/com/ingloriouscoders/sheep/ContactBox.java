package com.ingloriouscoders.sheep;


import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import android.graphics.PorterDuff.Mode;




public class ContactBox extends View {
	public ContactBox(Context context){

		super(context);

	}

	

	public ContactBox(Context context, AttributeSet attr){

		super(context,attr);

	}

	
	public ContactBox(Context context, AttributeSet attr, int defaultStyles){

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
		Paint borderPaint = new Paint();
		
		borderPaint.setColor(Color.WHITE);
		borderPaint.setAntiAlias(true);
		
		Bitmap contact_image = BitmapFactory.decodeResource(getResources(),R.drawable.nocontact);				
		
		int border_radius = 50;
			
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		
		int horizontal_line_length = width - 2*border_radius;
		int vertical_line_length = height - 2*border_radius;

		int color = 0xff424242;
		
		canvas.drawARGB(0, 0, 0, 0);
		borderPaint.setColor(color);
		canvas.drawARGB(0, 0, 0, 0);
		final Rect rect = new Rect(0, 0, contact_image.getWidth(), contact_image.getHeight());

		
		
		borderPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		if (contact_image != null)
		{
			canvas.drawBitmap(contact_image, rect, rect, borderPaint);
		}
		canvas.drawRoundRect(new RectF(0,0,width,height)
		, border_radius,border_radius, borderPaint);
		
		
		
	}
	
	
}
