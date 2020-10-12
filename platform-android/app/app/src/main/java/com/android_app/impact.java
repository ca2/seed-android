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


class impact extends View
{


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


	private class impact_editable extends SpannableStringBuilder 
	{

		impact_editable() 
		{

			super();

		}

			
		@Override
		public SpannableStringBuilder replace(final int start, final int end, CharSequence tb, int tbstart, int tbend) 
		{

			SpannableStringBuilder builder = super.replace(start, end, tb, tbstart, tbend);

			return builder;

		}

	}


	class impact_input_connection extends BaseInputConnection 
   {

		public impact_editable m_editable;

		public impact m_impact;
		
		public impact_input_connection(impact impact, boolean bFullEditor) 
		{

			super(impact, bFullEditor);

			m_impact = impact;

			m_editable = impact.m_editable;

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

			boolean b = super.setComposingText(text, newCursorPosition);

			String str = text.toString();

			m_impact.aura_on_text_composition(str, newCursorPosition);

			return b;

		}

		// just adding this to show that text is being committed.
		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) 
		{
      
			boolean returnValue = super.commitText(text, newCursorPosition);
			
			Log.i("TAG", "text: " + m_editable);

			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			//manager.updateSelection();
			
			return returnValue;

		}


	}


   public app m_app;

   private Bitmap m_bitmap;

   private long m_lStartTime;

	private int m_iPaintStep;

	private int m_iWidth;

	private int m_iHeight;

	impact_input_connection m_inputconnection;

   public os m_os;

	public impact_editable m_editable;

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

	private static native boolean aura_on_text_composition(String str, int newCursorPosition);

	public Timer m_timer;

	

   public impact(app app) 
   {

		super(app);

		m_editable = new impact_editable();
		
//		setEnabled(false);

//		setFocusable(false);

		setEnabled(true);

		setFocusable(true);

		setFocusableInTouchMode(true);

		m_app = app;

		m_os = m_app.m_os;

		m_lStartTime = System.currentTimeMillis();

	}


	public void on_editable_replace(final int start, final int end, CharSequence s, int tbstart, int tbend)
	{

		String str = s.toString();

		Log.d("on_editable_replace", "Text : \"" + str + "\"");

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

			Log.d("com.android_app.impact", "m_os.m_bShowKeyboard");

			onReceivedShowKeyboard();

			//setEnabled(true);

			//setFocusable(true);

			//setFocusableInTouchMode(true);

			requestFocus();

			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);

		}

		if (m_os.m_bHideKeyboard) 
		{
	  
			m_os.m_bHideKeyboard = false;

			Log.d("com.android_app.impact", "onDraw Start Hiding Soft Keyboard");

			onReceivedHideKeyboard();

			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			manager.hideSoftInputFromWindow (getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

			clearFocus();

			setEnabled(false);

			setFocusable(false);

			//setFocusableInTouchMode(false);

			Log.d("com.android_app.impact", "m_os.m_bHideKeyboard");

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

		if(m_os.m_bEditFocusSet)
		{

			m_os.m_bEditFocusSet = false;

			m_editable.replace(0, m_editable.length(), m_os.m_strEditFocusText);

			if(m_os.m_iEditFocusSelBeg >= 0)
			{

				int iSelBeg = m_os.m_iEditFocusSelBeg;

				int iSelEnd = m_os.m_iEditFocusSelEnd;

				if(iSelEnd < 0)
				{

					iSelEnd = m_editable.length() + iSelEnd + 1;

				}

				m_inputconnection.setSelection(iSelBeg, iSelEnd);

			}

		}

		if(m_os.m_bEditFocusKill)
		{

			m_os.m_bEditFocusKill = false;

		}

   }


   @Override
   public InputConnection onCreateInputConnection(EditorInfo outAttrs) 
   {

		m_inputconnection = new impact_input_connection(this, true);

		outAttrs.inputType = InputType.TYPE_CLASS_TEXT;

		return m_inputconnection;

   }


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



