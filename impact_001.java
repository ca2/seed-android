package com.android_app;


import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.os.Build;
import android.text.Selection;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.graphics.Point;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;


class impact extends EditText
{


	class impact_input_connection extends BaseInputConnection 
   {


		public impact_editable m_editable;

		public impact m_impact;
		
		public impact_input_connection(impact impact, boolean bFullEditor) 
		{

			super(impact, bFullEditor);

			m_impact = impact;

			m_editable = new impact_editable(this);

		}

		
		private class impact_editable extends SpannableStringBuilder 
		{

			impact_input_connection	m_connection;
			
			impact_editable(impact_input_connection connection) 
			{

				super();

				m_connection = connection;

			}

			
			@Override
			public SpannableStringBuilder replace(final int start, final int end, CharSequence tb, int tbstart, int tbend) 
			{

				SpannableStringBuilder builder = super.replace(start, end, tb, tbstart, tbend);

				m_connection.m_impact.on_editable_replace(start, end, tb, tbstart, tbend);

				return builder;

			/*

				if (tbend > tbstart) 
				{

					super.replace(0, length(), "", 0, 0);

					return super.replace(0, 0, tb, tbstart, tbend);

				}
				else if (end > start) 
				{

					super.replace(0, length(), "", 0, 0);

					return super.replace(0, 0, DUMMY, 0, DUMMY.length());

				}

				return super.replace(start, end, tb, tbstart, tbend);

				*/

			}

		}

		
		@Override
		public Editable getEditable() 
		{

			return m_editable;

		}

		
		@Override
		public boolean deleteSurroundingText(int beforeLength, int afterLength) 
		{

			// Not called in latest Android version...

			return super.deleteSurroundingText(beforeLength, afterLength);

		}
		@Override
		public boolean setComposingText(CharSequence text, int newCursorPosition)
		{

			return super.setComposingText(text, newCursorPosition);

		}


	}

   public app m_app;

   private Bitmap m_bitmap;

   private long m_lStartTime;

	private int m_iPaintStep;

	private int m_iWidth;

	private int m_iHeight;

   public os m_os;

   private static native void render_impact(Bitmap bitmap, long time_ms);

   private static native void lButtonDown(float x, float y);

   private static native void mouseMove(float x, float y);

   private static native void lButtonUp(float x, float y);

   private static native void keyDown(int keycode);

   private static native void keyUp(int keycode);

   private static native void keyPreImeDown(int keycode, int iUni);

   private static native void keyPreImeUp(int keycode, int iUni);

   private static native void onReceivedShowKeyboard();

   private static native void onReceivedHideKeyboard();

   private static native void onText(String str);

	private static native void native_on_timer();

	private static native void aura_size_changed();

	public Timer m_timer;

	
	class impact_timer extends TimerTask 
	{
   
		impact m_impact;
	
		public impact_timer(impact impact)
		{

			m_impact = impact;
	
		}

		public void run() 
		{
      
			m_impact.on_timer();
   
		}

	}


   public impact(app app) 
   {

		super(app);
		
		setInputType(InputType.TYPE_NULL);

		setEnabled(false);

		setFocusable(false);

		m_app = app;

		m_os = m_app.m_os;

		setFocusableInTouchMode(true);

		m_lStartTime = System.currentTimeMillis();

		this.setOnKeyListener(this);
		
		this.addTextChangedListener(this);


	}


   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	{

      String data = s.toString();

		Log.d("com.android_app.impact", "beforeTextChanged \"" + data + "\"");

   }

   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) 
	{

      String data = s.toString();

		Log.d("com.android_app.impact", "onTextChanged \"" + data + "\"");

   }

   @Override
   public void afterTextChanged(Editable s) 
	{

      String data = s.toString();

		Log.d("com.android_app.impact", "afterTextChanged \"" + data + "\"");

   }

	public void on_editable_replace(final int start, final int end, CharSequence tb, int tbstart, int tbend)
	{


	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
  
		super.onSizeChanged(w, h, oldw, oldh);

  		m_iWidth = w;

		m_iHeight = h;

		m_bitmap = Bitmap.createBitmap(m_iWidth, m_iHeight, Bitmap.Config.ARGB_8888);

		m_app.m_os.m_iWidth = m_iWidth;

		m_app.m_os.m_iHeight = m_iHeight;

		m_app.aura_init(m_app.m_os);

		if(!m_app.aura_is_started())
		{

			m_app.aura_start();

		}
		else
		{

			aura_size_changed();

		}

		m_app.update_mem_free_available();

		if(m_timer == null)
		{

			m_timer = new Timer();

			TimerTask updateBall = new impact_timer(this);

			m_timer.scheduleAtFixedRate(updateBall, 0, 100);

		}

	}


   public void on_timer()
	{

		native_on_timer();

	}

	
   @Override
	protected void onDraw(Canvas canvas) 
	{

		render_impact(m_bitmap, System.currentTimeMillis() - m_lStartTime);

		canvas.drawBitmap(m_bitmap, 0, 0, null);

		invalidate();

		if(m_iPaintStep % 8 == 0)
		{
		
			m_app.update_mem_free_available();

		}

		m_iPaintStep++;

		if (m_os.m_bShowKeyboard) 
		{

			m_os.m_bShowKeyboard = false;

			onReceivedShowKeyboard();

			/*
			
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);

			*/

			setInputType(InputType.TYPE_CLASS_TEXT);

			setEnabled(true);

			setFocusable(true);

		}

		if (m_os.m_bHideKeyboard) 
		{
	  
			Log.d("com.android_app.impact", "onDraw Start Hiding Soft Keyboard");

			m_os.m_bHideKeyboard = false;

			onReceivedHideKeyboard();

			/*
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			// imm.hideSoftInputFromWindow (getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

			imm.hideSoftInputFromWindow(getWindowToken(), 0);

			*/

			setInputType(InputType.TYPE_NULL);

			setEnabled(false);

			setFocusable(false);

			Log.d("com.android_app.impact", "onDraw Soft Keyboard (Should be) Hidden (2)");

		}

		if (m_os.m_strOpenUrl != null && m_os.m_strOpenUrl.length() > 0) 
		{

			openUrl(m_os.m_strOpenUrl);
		
		}

		if (m_os.m_iShowMessageBox > 0) 
		{

			m_os.m_iShowMessageBox = 0;

			message_box(this, m_os.m_strMessageBox, m_os.m_strMessageBoxCaption, m_os.m_iMessageBoxButton);

		}

   }


   @Override
   public InputConnection onCreateInputConnection(EditorInfo outAttrs) 
   {

	return super.onCreateInputConnection(outAttrs);

	  /*impact_input_connection connection = new impact_input_connection(this, false);

	  outAttrs.inputType = InputType.TYPE_NULL;

	  return connection;*/

   }


	@Override
	public boolean onKey(View impact, int keyCode, KeyEvent keyEvent) 
	{

		int action = keyEvent.getAction();

		if (action == KeyEvent.ACTION_MULTIPLE) 
		{

			String s = keyEvent.getCharacters();
		 
		 /*
			if (!s.equals(DUMMY) && !s.equals("\n")) 
			{

				onText(s);

			}
			*/
	  }
	  else if (action == KeyEvent.ACTION_DOWN) 
	  {
	  
			keyPreImeDown(keyCode, keyEvent.getUnicodeChar());

	  }
	  else if (action == KeyEvent.ACTION_UP) 
	  {

			keyPreImeUp(keyCode, keyEvent.getUnicodeChar());

	  }

	  return false;

   }


	public boolean onTouchEvent(final MotionEvent ev)
	{
	
		if (ev.getAction() == MotionEvent.ACTION_DOWN) 
		{

			lButtonDown(ev.getX(), ev.getY());

		}
		else if (ev.getAction() == MotionEvent.ACTION_MOVE) 
		{

			mouseMove(ev.getX(), ev.getY());

		}
		else if (ev.getAction() == MotionEvent.ACTION_UP) 
		{

			lButtonUp(ev.getX(), ev.getY());

		}

		return true;

   }

   
   public boolean onKeyPreIme(int keyCode, KeyEvent ev) 
   {

		if (ev.getAction() == KeyEvent.ACTION_DOWN) 
		{

			keyPreImeDown(keyCode, ev.getUnicodeChar());

		}
		else if (ev.getAction() == KeyEvent.ACTION_UP) 
		{

			keyPreImeUp(keyCode, ev.getUnicodeChar());

		}

		return true;

	}


   private void openUrl(String url) 
   {

		Uri uriUrl = Uri.parse(url);

		Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getContext().startActivity(intent);

   }


   public void message_box(View impact, String strMessage, String strCaption, int iButton) 
   {

		AlertDialog.Builder builder = new AlertDialog.Builder(this.m_app);

		builder.setTitle(strCaption);

		builder.setMessage(strMessage);

		m_os.m_iMessageBoxResult = 0;

		if ((iButton & 1) != 0 || iButton == 0) 
		{

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
		 
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
			   
					m_os.m_iMessageBoxResult = 1;

				}

			});

		}

		if ((iButton & 2) != 0) 
		{

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

					m_os.m_iMessageBoxResult = 2;

				}

			});

		}
	
		if ((iButton & 4) != 0) 
		{

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

					m_os.m_iMessageBoxResult = 4;

				}

			});

		}

		if ((iButton & 8) != 0) 
		{

			builder.setNegativeButton("No", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

					m_os.m_iMessageBoxResult = 8;

				}

			});

		}

		AlertDialog dialog = builder.create();

		dialog.show();

	}


}



