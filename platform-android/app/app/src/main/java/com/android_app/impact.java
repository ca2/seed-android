package com.android_app;


import java.util.Timer;
import java.util.TimerTask;
import java.lang.Runnable;
import android.app.AlertDialog;
import android.os.Build;
import android.text.Selection;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.text.InputType;
import android.graphics.Point;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;



class impact extends View
{



	class impact_timer implements Runnable
	{
   
		impact m_impact;
	
		public impact_timer(impact impact)
		{

			m_impact = impact;
	
		}

		  @Override
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

		public SpannableStringBuilder set(String str) 
		{

			int iLocalStart = 0;

			int iLocalLengthy = length();

			int iStringsStart = 0;

			int iStringLen = str.length();

			return replace(iLocalStart, iLocalLengthy, str, iStringsStart, iStringLen);

		}

	}


	class impact_input_connection extends BaseInputConnection 
   {

		public impact_editable m_editable;

		public impact m_impact;

		int m_iBatch;

		public os m_os;
		

		public impact_input_connection(impact impact, boolean bFullEditor) 
		{

			super(impact, bFullEditor);

			m_iBatch = 0;

			m_impact = impact;

			m_editable = impact.m_editable;

			m_os = impact.m_os;

		}


		public boolean isBatchEdit()
		{
		
			return m_iBatch > 0;

		}


		@Override
		public boolean beginBatchEdit() 
		{

			boolean b = super.beginBatchEdit();

			m_iBatch++;

			m_impact.InputConnectionBeginBatchEdit();

			return b;

		}


		@Override
		public boolean endBatchEdit() 
		{

			boolean b = super.endBatchEdit();

			m_impact.InputConnectionEndBatchEdit();

			m_iBatch--;

			return b;

		}


		@Override
		public Editable getEditable() 
		{

			return m_editable;

		}


		@Override
		public CharSequence getTextBeforeCursor(int n, int flags) 
		{

			if(m_os.m_strEditorText == null)
			{

				return "";

			}

			int iStart = Math.min(m_os.m_iEditorSelectionStart, m_os.m_iEditorSelectionEnd);

			String string = m_os.m_strEditorText.substring(Math.max(0, iStart - n), iStart);

			return string;

		}


		@Override
		public CharSequence getTextAfterCursor(int n, int flags) 
		{

			if(m_os.m_strEditorText == null)
			{

				return "";

			}

			int iEnd = Math.max(m_os.m_iEditorSelectionStart, m_os.m_iEditorSelectionEnd);

			String string = m_os.m_strEditorText.substring(iEnd, Math.min(iEnd + n, m_os.m_strEditorText.length()));

			return string;

		}


		@Override
		public CharSequence getSelectedText(int flags) 
		{

			if(m_os.m_strEditorText == null)
			{

				return "";

			}

			int iStart = Math.min(m_os.m_iEditorSelectionStart, m_os.m_iEditorSelectionEnd);

			int iEnd = Math.max(m_os.m_iEditorSelectionStart, m_os.m_iEditorSelectionEnd);

			iStart = Math.max(iStart, 0);

			iEnd = Math.min(iEnd, m_os.m_strEditorText.length());

			String string = m_os.m_strEditorText.substring(iStart, iEnd);

			return string;

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

			m_impact.InputConnectionSetComposingText(str, newCursorPosition);

			return b;

		}


		@Override
		public boolean commitText(CharSequence text, int newCursorPosition)
		{

			boolean b = super.commitText(text, newCursorPosition);

			String str = text.toString();

			m_impact.InputConnectionCommitText(str, newCursorPosition);

			return b;

		}


		@Override
		public boolean setComposingRegion(int start, int end)
		{

			boolean b = super.setComposingRegion(start, end);

			m_impact.InputConnectionSetComposingRegion(start, end);

			return b;

		}

		
		@Override
		public boolean setSelection(int start, int end)
		{

			boolean b = super.setSelection(start, end);

			m_impact.InputConnectionSetSelection(start, end);

			return b;

		}


		// just adding this to show that text is being committed.
		@Override
		public boolean finishComposingText() 
		{
      
			boolean returnValue = super.finishComposingText();
			
			m_impact.InputConnectionFinishComposingText();

			return returnValue;

		}


	}


   public app m_app;

	private boolean m_bAskedToShowSoftInput;

   private Bitmap m_bitmap;

   private long m_lStartTime;

	private int m_iStep;

	private int m_iWidth;

	private int m_iHeight;

	private boolean m_bShowKeyboardAfterwards2;

	private boolean m_bFocus;

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

	private static native boolean InputConnectionBeginBatchEdit();

	private static native boolean InputConnectionEndBatchEdit();

	private static native boolean InputConnectionCommitText(String text, int newCursorPosition);

	private static native boolean InputConnectionSetComposingText(String text, int newCursorPosition);

	private static native boolean InputConnectionSetComposingRegion(int start, int end);

	private static native boolean InputConnectionSetSelection(int start, int end);

	private static native boolean InputConnectionFinishComposingText();

	//private static native boolean aura_on_text_commit(String str, int newCursorPosition);

	public task_scheduler m_scheduler;

	
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

		if(m_scheduler == null)
		{

			m_scheduler = new task_scheduler();

			Runnable exchange_timer = new impact_timer(this);

			m_scheduler.scheduleAtFixedRate(exchange_timer, 100);

		}

	}


   public void on_timer()
	{

		native_on_timer();

		step();

		if(m_os.m_bRedraw)
		{

			postInvalidate();

		}

	}


	void step()
	{

		m_iStep++;

		if (m_os.m_bHideKeyboard) 
		{
	  
			m_os.m_bHideKeyboard = false;

			Log.d("com.android_app.impact", "onDraw Start Hiding Soft Keyboard");

			onReceivedHideKeyboard();

			if(m_bAskedToShowSoftInput)
			{

				m_bAskedToShowSoftInput = false;
			
				InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				///manager.hideSoftInputFromWindow (getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

				manager.hideSoftInputFromWindow (getWindowToken(), 0);

			}

			clearFocus();

			//setEnabled(false);

			//setFocusable(false);

			Log.d("com.android_app.impact", "m_os.m_bHideKeyboard");

		}

		if(m_os.m_bEditFocusSet)
		{

			m_os.m_bEditFocusSet = false;

			if(m_os.m_bShowKeyboard)
			{

				m_os.m_bShowKeyboard = false;

				m_bShowKeyboardAfterwards2 = true;

			}

			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			manager.restartInput(this);

			requestFocus();

		}


		if (m_os.m_bShowKeyboard) 
		{

			m_os.m_bShowKeyboard = false;

			Log.d("com.android_app.impact", "m_os.m_bShowKeyboard");

			onReceivedShowKeyboard();

			//setEnabled(true);

			//setFocusable(true);

			//setFocusableInTouchMode(true);

			requestFocus();

				if(!m_bAskedToShowSoftInput)
				{

						  	 m_bAskedToShowSoftInput = true;

			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			//manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
				manager.showSoftInput(this, InputMethodManager.SHOW_FORCED);

				

				}

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

		if(m_os.m_bEditorTextUpdated)
		{

			m_os.m_bEditorTextUpdated = false;

			m_editable.set(m_os.m_strEditorText);

		}

		if(m_inputconnection != null)
		{

			if(!m_inputconnection.isBatchEdit())
			{

				if(m_os.m_bInputMethodManagerUpdateSelection)
				{

					m_os.m_bInputMethodManagerUpdateSelection = false;

					InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

					manager.updateSelection(
							this,
							m_os.m_iInputMethodManagerSelectionStart,
							m_os.m_iInputMethodManagerSelectionEnd,
							m_os.m_iInputMethodManagerCandidateStart,
							m_os.m_iInputMethodManagerCandidateEnd);

				}

			}

		}

		if(m_os.m_bEditFocusKill)
		{

			m_os.m_bEditFocusKill = false;

		}

		if(m_iStep % 8 == 0)
		{
		
			m_app.update_mem_free_available();

		}

	}

	
   @Override
	protected void onDraw(Canvas canvas) 
	{

		render_impact(m_bitmap, System.currentTimeMillis() - m_lStartTime);

		canvas.drawBitmap(m_bitmap, 0, 0, null);

		if(m_os.m_bRedraw)
		{

			m_os.m_bRedraw = false;

		}

   }


   @Override
   public InputConnection onCreateInputConnection(EditorInfo outAttrs) 
   {

		m_inputconnection = new impact_input_connection(this, true);

		outAttrs.inputType = InputType.TYPE_CLASS_TEXT;

		outAttrs.initialSelStart = m_os.m_iEditorSelectionStart;

		outAttrs.initialSelEnd = m_os.m_iEditorSelectionEnd;

		String strEditor;

		if(m_os.m_strEditorText != null)
		{

			strEditor = m_os.m_strEditorText;

		}
		else
		{

			strEditor = "";

		}

		m_editable.set(strEditor);

		  if(m_bShowKeyboardAfterwards2)
		  {

				  m_bShowKeyboardAfterwards2 = false;

			m_os.m_bShowKeyboard = true;

		  }

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



