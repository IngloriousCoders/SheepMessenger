package com.ingloriouscoders.sheep;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.view.*;
import android.widget.GridView;
import android.widget.TableLayout.LayoutParams;
import android.graphics.*;
import android.graphics.Paint.FontMetrics;
import android.content.*;
import android.util.*;

public class MessageBubble extends View {
	
	private String msgtext, sender;
	public String params;
	
	public int paddingleft = 10, paddingright = 10, paddingup = 10, paddingdown = 10;
	public int bubblewidth = 500, bubbleheight, bubbleoffset = 10, bubblepadding = 50;
	private List<String> lines;
	public boolean incoming; public int bubblecolor;
	public boolean clicked = false; public boolean placeholder = false;
	public int totalreaders = 0, reads = 0;
	
	private Paint textPaint;
	/*private Paint borderPaint, checkPaint; int ascent, top;
	private FontMetrics fm;*/
	private Path path, path2;
	private int runs = 0;
	
	public MessageBubble(Context context){
		super(context);
		initVariables();
	}

	public MessageBubble(Context context, AttributeSet attr){
		super(context,attr);
		initVariables();
	}

	
	public MessageBubble(Context context, AttributeSet attr, int defaultStyles){
		super(context, attr, defaultStyles);
		initVariables();
	}
	
	@Override
	protected void onMeasure(int width,int height)
	{
		int measuredWidth = MeasureSpec.getSize(width);
		int measuredHeight = this.calculateHeight() + this.bubbleoffset;
		setMeasuredDimension( measuredWidth, measuredHeight);
	}
	
	public void initVariables() {
		/*textPaint = new Paint();
		checkPaint = new Paint();
		fm = textPaint.getFontMetrics();
		borderPaint = new Paint();*/
		lines  = new ArrayList<String>();
		path = new Path();
		path2 = new Path();
		//timetext = "";
	}
	
	public void initParameters() {
		// 1.) Timestamp
			/*if (params != null) {
				if (!getParamFromId(params,"time").equals("")) {
					String timestamp = getParamFromId(params,"time");
					strtimes = timestamp.split(";");
					for(int i=0;i<strtimes.length;i++) {
						if (strtimes[i].length() == 1) {
							strtimes[i] = "0" + strtimes[i];
						}
					}
					String timetext = strtimes[3] + ":" + strtimes[4];
				}
			}*/
	}
	
	public void setPlaceholder()
	{
		this.placeholder = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{		
		if (this.msgtext.equals("") && this.sender.equals("")) {
			placeholder = true;
		} else {
			placeholder = false;
		}

		if(!placeholder) {
			
					this.bubblewidth = this.getWidth() - bubblepadding;
			
					
					textPaint = new Paint(); //<-
					textPaint.setAntiAlias(true);
					
					FontMetrics fm = textPaint.getFontMetrics(); //<-
					int ascent = Math.abs((int) fm.ascent);
					int top = Math.abs((int) fm.top);
					
					textPaint.setTextSize(20);
					textPaint.setColor(Color.BLACK);
					
					//Algorithmus fuer die Zeilenumbrueche. Die einzelnen Textzeilen werden in der List "lines" gespeichert.
					lines  = new ArrayList<String>(); //<-
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
					
					if(runs < 8) {
						this.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT,bubbleheight + bubbleoffset));
						this.invalidate();
						//initParameters();
						runs++;
					}
	
					
					Paint borderPaint = new Paint(); //<-
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
						
						//Path path = new Path(); //<-
						path.moveTo(this.getWidth()-10, 10);
						path.lineTo(this.getWidth(), 20);
						path.lineTo(this.getWidth()-10, 30);
						path.close();

						canvas.drawPath(path, borderPaint);

						canvas.drawText(sender + ":", paddingleft+bubblepadding, paddingup + ascent, textPaint);
						
						
						String timetext = null;
						String timeParameter = getParamFromId(params,"time");
						
						if (timeParameter != null)
							timetext = getHourAndMinuteFromStamp(Long.parseLong(timeParameter));
						
						if (timetext != null)
							canvas.drawText(timetext, this.getWidth()-paddingleft-10-textPaint.measureText(timetext), paddingup + ascent, textPaint);
						
						
						textPaint.setColor(Color.DKGRAY);
						canvas.drawLine(paddingleft-5+bubblepadding, paddingup + ascent + 3, this.getWidth()-10-(paddingright-5), paddingup + ascent + 3, textPaint);
				
						textPaint.setColor(Color.BLACK);
						for (int i=0; i<lines.size(); i++) {
							canvas.drawText(lines.get(i), paddingleft+bubblepadding, (int) (paddingup*1.5) + ascent*3 + (ascent+top)*i, textPaint);
						}
						
						if (totalreaders > 0) {
							Paint checkPaint = new Paint(); //<-
							checkPaint.setAntiAlias(true);
							checkPaint.setColor(Color.DKGRAY);
							checkPaint.setTextSize(20);
							
							int textRightPadding = 35;
							int textDownPadding = 9;
							
							if (totalreaders > reads) {
								canvas.drawText(reads + "/" + totalreaders, this.getWidth()-10-textRightPadding, bubbleheight-textDownPadding+3, checkPaint);
							}
							
							if (totalreaders <= reads) {
								int checkRightPadding = 10;
								int checkDownPadding = 9;
								
								checkPaint.setStrokeWidth(3);
								checkPaint.setStrokeCap(Paint.Cap.ROUND);
								
								canvas.drawLine(this.getWidth()-25 -checkRightPadding, bubbleheight-5-checkDownPadding, this.getWidth()-20-checkRightPadding, bubbleheight-checkDownPadding, checkPaint);
								canvas.drawLine(this.getWidth()-20-checkRightPadding, bubbleheight-checkDownPadding, this.getWidth()-10-checkRightPadding, bubbleheight-10-checkDownPadding, checkPaint);
							}
						}
					}
					
					if (this.incoming) {
						int round_radius = 10;
						canvas.drawRoundRect(new RectF(10,0,this.getWidth()-bubblepadding,bubbleheight), round_radius, round_radius, borderPaint);
						
						//Path path2 = new Path(); //<-
						path2.moveTo(10, 10);
						path2.lineTo(0, 20);
						path2.lineTo(10, 30);
						path2.close();
						
						canvas.drawPath(path2, borderPaint);
						
						canvas.drawText(sender + ":", paddingleft+10, paddingup + ascent, textPaint);
						
						
						String timeParameter = getParamFromId(params,"time");
						String timetext = null;
						if (timeParameter != null)
							timetext = getHourAndMinuteFromStamp(Long.parseLong(timeParameter));
							
						if (timetext != null)
							canvas.drawText(timetext, this.getWidth()-bubblepadding-paddingleft-textPaint.measureText(timetext), paddingup + ascent, textPaint);
				
							
						textPaint.setColor(Color.DKGRAY);
						canvas.drawLine(paddingleft+5, paddingup + ascent + 3, bubblewidth-(paddingright-5), paddingup + ascent + 3, textPaint);
						
						textPaint.setColor(Color.BLACK);
						for (int i=0; i<lines.size(); i++) {
							canvas.drawText(lines.get(i), paddingleft+10, (int) (paddingup*1.5) + ascent*3 + (ascent+top)*i, textPaint);
						}
					}
		}
	}
	
	public String getHourAndMinuteFromStamp(long _timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date resultingDate = new Date(_timestamp);
		
		return sdf.format(resultingDate);
	}
	
	public void setMessage(String _sender, String _msgtext) {
		this.msgtext = _msgtext;
		this.sender  = _sender;
	}
	
	public void giveParameters(String _params) {
		this.params = _params;
	}
	
	public void setGraphics(boolean _incoming, int _color) {
		this.bubblecolor = _color;
		this.incoming = _incoming;
	}
	
	public void setReads(int _totalreaders, int _reads) {
		this.totalreaders = _totalreaders;
		this.reads = _reads;
	}
	
	public String getParamFromId(String _params, String id) {
		String param = null;
		
		if (_params.contains("&")) {
			String[] paramsSplit = _params.split("&");
			for(int i=1;i<paramsSplit.length;i++) {
					if (paramsSplit[i].startsWith(id)) {
						param = paramsSplit[i].substring(id.length()+1, paramsSplit[i].length());
						return param;
					}
			}
		}
		
		return param;
	}
	
	public static String getStaticParamFromId(String _params, String id) {
		String param = null;
		
		if (_params.contains("&")) {
			String[] paramsSplit = _params.split("&");
			for(int i=1;i<paramsSplit.length;i++) {
					if (paramsSplit[i].startsWith(id)) {
						param = paramsSplit[i].substring(id.length()+1, paramsSplit[i].length());
						return param;
					}
			}
		}
		
		return param;
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
		final int bubblewidth = 500;
		
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
