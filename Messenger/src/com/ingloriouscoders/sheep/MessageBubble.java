package com.ingloriouscoders.sheep;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.view.*;
import android.widget.GridView;
import android.widget.TableLayout.LayoutParams;
import android.graphics.*;
import android.graphics.Paint.FontMetrics;
import android.content.*;
import android.content.res.TypedArray;
import android.util.*;

public class MessageBubble extends View {
	
	private String msgtext, sender;
	public int paddingleft = 10, paddingright = 10, paddingup = 10, paddingdown = 10;
	public int bubblewidth = 500, bubbleheight, bubbleoffset = 10, bubblepadding = 50;
	private List<String> lines;
	public boolean incoming; public int bubblecolor;
	public boolean clicked = false;
	
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
		int measuredHeight = this.calculateHeight() + this.bubbleoffset;
		setMeasuredDimension( measuredWidth, measuredHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{		
		this.bubblewidth = this.getWidth() - bubblepadding;

		
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		
		FontMetrics fm = textPaint.getFontMetrics();
		int ascent = Math.abs((int) fm.ascent);
		int top = Math.abs((int) fm.top);
		
		textPaint.setTextSize(20);
		textPaint.setColor(Color.BLACK);
		
		//Algorithmus fuer die Zeilenumbrueche. Die einzelnen Textzeilen werden in der List "lines" gespeichert.
		lines  = new ArrayList<String>();
		int destinationWidth = bubblewidth - paddingleft - (paddingright * 5);
		msgtext = msgtext.trim();
		boolean linesFinished = false;
		String currentText = msgtext, processingText = "", processedText;
		
		while (!linesFinished) {
			int chars = textPaint.breakText(msgtext, true, destinationWidth, null);
			
			if (currentText.length() > chars) {
				processingText = currentText.substring(0, chars);
			} else {
				lines.add(currentText);
				linesFinished = true;
				break;
			}
			
			if (processingText.lastIndexOf(" ") == -1) {
				processedText = processingText;
			} else {
				processedText = processingText.substring(0, processingText.lastIndexOf(" ")+1);
			}
			
			lines.add(processedText);
			currentText = currentText.substring(processedText.length(), currentText.length());
		}
		
		bubbleheight = paddingup + ascent*3 + (ascent+top)*lines.size() - paddingdown;

		this.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,bubbleheight + bubbleoffset));
		this.invalidate();
		
		Paint borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setColor(bubblecolor);
		
		float darkening1 = 1.5f;
		float darkening2 = 1.2f;
		
		if (!this.clicked) {
			darkening1 = 1.0f;
			darkening2 = 0.7f;
		}
		
		
		float[] hsv = new float[3];
		Color.colorToHSV(bubblecolor, hsv);
		hsv[2] *= darkening1; // value component
		int bubblecolor2 = Color.HSVToColor(hsv);
		
		float[] hsv2 = new float[3];
		Color.colorToHSV(bubblecolor, hsv2);
		hsv2[2] *= darkening2; // value component
		int bubblecolor3 = Color.HSVToColor(hsv2);
		
		
	    LinearGradient gradient = new LinearGradient(0, 0, 0, bubbleheight, bubblecolor2, bubblecolor3, Shader.TileMode.MIRROR);
	    borderPaint.setDither(true);
	    borderPaint.setShader(gradient);

		
		if (!this.incoming) {
			int round_radius = 10;
			canvas.drawRoundRect(new RectF(bubblepadding,0,this.getWidth()-10,bubbleheight), round_radius, round_radius, borderPaint);
			
			Path path = new Path();
			path.moveTo(this.getWidth()-10, 10);
			path.lineTo(this.getWidth(), 20);
			path.lineTo(this.getWidth()-10, 30);
			path.close();
			
			canvas.drawPath(path, borderPaint);
			
			canvas.drawText(sender + ":", paddingleft+bubblepadding, paddingup + ascent, textPaint);
			textPaint.setColor(Color.DKGRAY);
			canvas.drawLine(paddingleft-5+bubblepadding, paddingup + ascent + 3, this.getWidth()-10-(paddingright-5), paddingup + ascent + 3, textPaint);
			
			textPaint.setColor(Color.BLACK);
			for (int i=0; i<lines.size(); i++) {
				canvas.drawText(lines.get(i), paddingleft+bubblepadding, (int) (paddingup*1.5) + ascent*3 + (ascent+top)*i, textPaint);
			}
		}
		
		if (this.incoming) {
			int round_radius = 10;
			canvas.drawRoundRect(new RectF(10,0,this.getWidth()-bubblepadding,bubbleheight), round_radius, round_radius, borderPaint);
			
			Path path = new Path();
			path.moveTo(10, 10);
			path.lineTo(0, 20);
			path.lineTo(10, 30);
			path.close();
			
			canvas.drawPath(path, borderPaint);
			
			canvas.drawText(sender + ":", paddingleft+10, paddingup + ascent, textPaint);
			textPaint.setColor(Color.DKGRAY);
			canvas.drawLine(paddingleft+5, paddingup + ascent + 3, bubblewidth-(paddingright-5), paddingup + ascent + 3, textPaint);
			
			textPaint.setColor(Color.BLACK);
			for (int i=0; i<lines.size(); i++) {
				canvas.drawText(lines.get(i), paddingleft+10, (int) (paddingup*1.5) + ascent*3 + (ascent+top)*i, textPaint);
			}
		}
	}
	
	public void setMessage(String _sender, String _msgtext) {
		this.msgtext = _msgtext;
		this.sender  = _sender;
	}
	
	/*public void setWidth(int _width) {
		this.bubblewidth = _width;

		bubbleheight = calculateHeight();
		this.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,bubbleheight + bubbleoffset));
		this.invalidate();
	}*/
	
	public void setGraphics(boolean _incoming, int _color) {
		this.bubblecolor = _color;
		this.incoming = _incoming;
	}

	
	public int calculateHeight() {
		int _bubbleheight = 0;
		
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		FontMetrics fm = textPaint.getFontMetrics();
		int ascent = Math.abs((int) fm.ascent);
		int top = Math.abs((int) fm.top);	
		textPaint.setTextSize(20);
		textPaint.setColor(Color.BLACK);
		int linecount = 0;
		int destinationWidth = bubblewidth - paddingleft - (paddingright * 4);
		msgtext = msgtext.trim();
		boolean linesFinished = false;
		String currentText = msgtext, processingText = "", processedText;
		
		while (!linesFinished) {
			int chars = textPaint.breakText(msgtext, true, destinationWidth, null);
			
			
			if (currentText.length() > chars) {
				processingText = currentText.substring(0, chars);
			} else {
				linecount++;
				linesFinished = true;
				break;
			}
			
			if (processingText.lastIndexOf(" ") == -1) {
				processedText = processingText;
			} else {
				processedText = processingText.substring(0, processingText.lastIndexOf(" ")+1);
			}
			
			linecount++;
			
			currentText = currentText.substring(processedText.length(), currentText.length());
		}
		
		_bubbleheight = paddingup + ascent*3 + (ascent+top)*linecount - paddingdown;
		
		return _bubbleheight;
	}
	
	static public int calculateHeight(String msgtext) {
		int _bubbleheight = 0;
		final int paddingleft = 10, paddingright = 10, paddingup = 10, paddingdown = 10;
		final int bubblewidth = 500, bubbleheight, bubbleoffset = 10, bubblepadding = 50;
		
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		FontMetrics fm = textPaint.getFontMetrics();
		int ascent = Math.abs((int) fm.ascent);
		int top = Math.abs((int) fm.top);	
		textPaint.setTextSize(20);
		textPaint.setColor(Color.BLACK);
		int linecount = 0;
		int destinationWidth = bubblewidth - paddingleft - (paddingright * 4);
		msgtext = msgtext.trim();
		boolean linesFinished = false;
		String currentText = msgtext, processingText = "", processedText;
		
		while (!linesFinished) {
			int chars = textPaint.breakText(msgtext, true, destinationWidth, null);
			
			
			if (currentText.length() > chars) {
				processingText = currentText.substring(0, chars);
			} else {
				linecount++;
				linesFinished = true;
				break;
			}
			
			if (processingText.lastIndexOf(" ") == -1) {
				processedText = processingText;
			} else {
				processedText = processingText.substring(0, processingText.lastIndexOf(" ")+1);
			}
			
			linecount++;
			
			currentText = currentText.substring(processedText.length(), currentText.length());
		}
		
		_bubbleheight = paddingup + ascent*3 + (ascent+top)*linecount - paddingdown;
		
		return _bubbleheight;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent touch_event)
	{
		switch (touch_event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			clicked = true;
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			clicked = false;
			invalidate();
			callOnClick();
			return false;
		default:
			clicked = false;
			invalidate();
			return false;
		
		}
	}
}
