package com.ingloriouscoders.sheep;


import java.io.FileNotFoundException;
import java.io.IOException;

import android.view.*;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.content.*;
import android.util.*;
import android.graphics.PorterDuff.Mode;
import android.view.InputEvent.*;
import android.util.Log;
import android.content.res.Resources;
import android.net.Uri;	
import android.content.res.AssetFileDescriptor;
import java.io.InputStream;


public class ContactBox extends View {
	
	private Bitmap rounded_contact_bitmap;
	
	private int width = 300;
	private int height = 300;
	
	private int border_radius = 20;
	private int shadow_radius = 10;
	
	private Bitmap standard_image = BitmapFactory.decodeResource(getResources(),R.drawable.nocontact);
	
	private boolean state_pressed = false;
	
	private int label_max_len = 15;
	private String contact_label = "Luca";
	
	private Context mContext;	
	
	private Contact mContact;
	
	public ContactBox(Context context){

		super(context);
		mContext = context;
		mContact = Contact.getPlaceholder();
		initializeBitmap();
		

	}
	public ContactBox(Context context,Contact _contact){

		super(context);

		mContext = context;
		mContact = _contact;
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
		int box_height = width;
		
		Bitmap bitmap = standard_image;
		
		if (mContact != null && mContact.getPhotoURI() != "")
		{
			try
			{
				Uri myUri = Uri.parse(mContact.getPhotoURI());
				if (myUri == null)
				{
					Log.v("ContactBox","uri is null");
				}
				ContentResolver cr = mContext.getContentResolver();
				if (cr == null)
				{
					Log.v("ContactBox","cr is null");
				}
				
				InputStream is = cr.openInputStream(myUri);
			
				bitmap = BitmapFactory.decodeStream(is);
			}
			catch (FileNotFoundException e)
			{
				Log.v("ContactBox","Datei nicht gefunden");
			}
				
			
						
		}
		
		int img_width = bitmap.getWidth();
		int img_height = bitmap.getHeight();
		
		rounded_contact_bitmap = Bitmap.createBitmap(width-shadow_radius,box_height-shadow_radius,Bitmap.Config.ARGB_8888);
		
		Canvas init_canvas = new Canvas(rounded_contact_bitmap);
		
		Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		maskPaint.setColor(Color.RED);
		
		init_canvas.drawRoundRect(new RectF(shadow_radius+1,shadow_radius+1,width-1-shadow_radius,box_height-1-shadow_radius), border_radius, border_radius , maskPaint);
		
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		init_canvas.drawBitmap(bitmap,new Rect(0,0,img_width,img_height),new Rect(shadow_radius+1,shadow_radius+1,width-1-shadow_radius,box_height-1-shadow_radius),maskPaint);		
		
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
		if (height < width+50)
		{
			return;
		}
		int box_height = width;
	
		Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		shadowPaint.setColor(Color.BLACK);
		
		shadowPaint.setShadowLayer(shadow_radius,0,0,Color.BLACK);
		canvas.drawRoundRect(new RectF(shadow_radius,shadow_radius,width-shadow_radius,box_height-shadow_radius), border_radius, border_radius, shadowPaint);
		
		Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.GRAY);
		if (mContact.getUnreadMessages() > 0)
		{
			borderPaint.setColor(Color.GREEN);
		}
		
		
		if (state_pressed)
		{
			borderPaint.setColor(Color.BLACK);
			
		}
		
		canvas.drawRoundRect(new RectF(shadow_radius,shadow_radius,width-shadow_radius,box_height-shadow_radius), border_radius, border_radius, borderPaint);
		
		Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		if (rounded_contact_bitmap != null)
		{
			canvas.drawBitmap(rounded_contact_bitmap,0,0,bitmapPaint);
		}
		if (mContact.getUnreadMessages() > 0)
		{
			int start_height = box_height - (int)(box_height*0.4);
			int end_height = box_height -  border_radius - shadow_radius -1;
			int sum_height = end_height - start_height;
			Path path = new Path();
			path.moveTo(shadow_radius+1, start_height);
			path.lineTo(width-shadow_radius-1, start_height);
			path.lineTo(width-shadow_radius-1, end_height);
			path.arcTo(new RectF(width-border_radius-shadow_radius*2-1,/*left*/
							     end_height-border_radius-1,/*top*/
							     width-shadow_radius-1,/*right*/
							     end_height+border_radius-2),/*bottom*/
							     0, 90, false);
			path.lineTo(shadow_radius+border_radius, end_height+border_radius);
			path.arcTo(new RectF(
						shadow_radius+1,
						end_height-border_radius,
						2*border_radius+1,
						end_height+border_radius-2
						), 90, 90, false);
			path.close();
			Paint darkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			darkerPaint.setColor(Color.BLACK);
			darkerPaint.setAlpha(100);
			canvas.drawPath(path, darkerPaint);
			
			darkerPaint.setAlpha(255);
			darkerPaint.setColor(Color.GREEN);
			darkerPaint.setTextSize(38);
			darkerPaint.setTextAlign(Align.LEFT);
			
			float num_height = darkerPaint.ascent()*-1;
			float num_width = darkerPaint.measureText(""+mContact.getUnreadMessages());
			float num_padding_bottom = (sum_height-num_height)/2;
			
			canvas.drawText(""+mContact.getUnreadMessages(),shadow_radius+10 , end_height-num_padding_bottom, darkerPaint);
			
			int fontSize = 15;
			darkerPaint.setTextSize(fontSize);
						
			Resources res = getResources();
			String new_messages_label_pt1 = res.getString(R.string.new_messages_pt1);
			String new_messages_label;
			if (mContact.getUnreadMessages() > 1)
			{
				new_messages_label = res.getString(R.string.new_messages_pt2_plur);
			}
			else
			{
				new_messages_label = res.getString(R.string.new_messages_pt2_sing);
			}
			float label_height = darkerPaint.ascent();
			
			canvas.drawText( new_messages_label ,num_width+shadow_radius+10 ,end_height-num_padding_bottom, darkerPaint);
			canvas.drawText(new_messages_label_pt1 ,num_width+shadow_radius+10 ,end_height-num_padding_bottom+label_height, darkerPaint);
			
		}
		
		
		if (state_pressed)
		{
			Paint darkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			darkerPaint.setColor(Color.BLACK);
			darkerPaint.setAlpha(100);
			
			canvas.drawRoundRect(new RectF(shadow_radius,shadow_radius,width-shadow_radius,box_height-shadow_radius), border_radius, border_radius, darkerPaint);
			
		}
		
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(20);
		textPaint.setTextAlign(Align.CENTER);
		
		String showLabel = this.mContact.getShowname();
		if (showLabel.length() > label_max_len)
		{
			showLabel = showLabel.substring(0, label_max_len) + "...";
		}
		
		canvas.drawText(showLabel,width/2,box_height+30,textPaint);
		
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
			return false;
		default:
			state_pressed = false;
			invalidate();
			return false;
		
		}
	}
	public void setContact(Contact _contact)
	{
		this.mContact = _contact;
		initializeBitmap();
	}

	
}
	

