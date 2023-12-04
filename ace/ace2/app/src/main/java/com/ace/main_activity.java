// From platform-android/app to ace/ace by camilo on 2022-05-03 <3ThomasBorregaardSorensen!!
// getStringResourceFromName https://stackoverflow.com/questions/7493287/android-how-do-i-get-string-from-resources-using-its-name
package com.ace;


import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.Display;
import android.util.DisplayMetrics;
import android.widget.Toast;
import java.lang.UnsatisfiedLinkError;
import android.util.Log;


public class main_activity extends Activity 
{


	impact m_impact;

	public bind m_bind;

	public AssetManager m_assetmanager;

	public static native void aura_init(bind bind, AssetManager assetManager);

	public static native void aura_start();

	public static native void on_aura_message_box_response(long lMessageBoxSequence, long lMessageBoxResponse);

	public static native boolean aura_is_started();

	private static native void sync_mem_free_available();


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);

		Display display = getWindowManager().getDefaultDisplay();

		DisplayMetrics metrics = new DisplayMetrics();

		display.getRealMetrics(metrics);

		m_bind = new bind();

		m_bind.m_iWidth = -1;

		m_bind.m_iHeight = -1;

		m_bind.m_fDpiX = metrics.xdpi;

		m_bind.m_fDpiY = metrics.ydpi;

		m_bind.m_fDensity = metrics.density;

		String strApplicationName = "";

		String strApplicationIdentifier = "";

		String strCommandLineParameters = "";

		try 
		{

			strApplicationName = getString("app_name");

			strApplicationIdentifier = getString("application_identifier");

			strCommandLineParameters = getString("command_line_parameters");

		} 
		catch (NullPointerException e)
		{

		}

		m_bind.m_strApplicationIdentifier = strApplicationIdentifier;

		m_bind.m_strCommandLineParameters = strCommandLineParameters;

		boolean bLoaded = false;

		String strApplicationLibraryName = strApplicationIdentifier;

		strApplicationLibraryName = strApplicationLibraryName.replace("/", "_");

		strApplicationLibraryName = strApplicationLibraryName.replace("-", "_");

		try
		{

			System.loadLibrary("impl_" + strApplicationLibraryName);

			bLoaded = true;

		}
		catch(UnsatisfiedLinkError unsatisfiedLinkError)
		{

			String strUnsatisfiedLinkError = unsatisfiedLinkError.getMessage(); 
		 
			Log.i("com.ace.ace", strUnsatisfiedLinkError);

		}

		m_bind.m_strCacheDirectory = getApplicationContext().getCacheDir().getAbsolutePath();

		Intent intent = getIntent();

		if(intent != null)
		{

			Uri uri = intent.getData();

			if(uri != null)
			{

				m_bind.m_strUri = uri.toString();

			}

		}

		m_assetmanager = getAssets();

		m_impact = new impact(this);

		setContentView(m_impact);

	}


	//this is called when the screen rotates.
// (onCreate is no longer called when screen rotates due to manifest, see: android:configChanges)
@Override
public void onConfigurationChanged(Configuration newConfig)
{
    super.onConfigurationChanged(newConfig);
    //setContentView(R.layout.main);

    //InitializeUI();
}


	public void update_mem_free_available() 
	{

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		MemoryInfo memoryinfo = new ActivityManager.MemoryInfo();

		activityManager.getMemoryInfo(memoryinfo);

		m_bind.m_lMemFreeAvailableKb = memoryinfo.availMem / 1024;

		sync_mem_free_available();

	}


	public String getStringResourceByName(String aString) 
	{

		String packageName = getPackageName();

		int resId = getResources().getIdentifier(aString, "string", packageName);

		try 
		{

			return getString(resId);

		}
		catch (Resources.NotFoundException notFoundException)
		{


		}

		return new String("");

	}


	public String getString(String aString) 
	{
		
		return getStringResourceByName(aString);
	
	}

	
	public void on_message_box_response(long lMessageBoxSequence, long lMessageBoxResponse)
	{

		m_impact.m_messagebox = null;

		on_aura_message_box_response(lMessageBoxSequence, lMessageBoxResponse);

	}

	 public void requestReadExternalStoragePermission() {

        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Read External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
        }
    }

}
