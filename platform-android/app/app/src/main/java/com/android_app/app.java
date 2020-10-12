package com.android_app;


import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.util.DisplayMetrics;
import android.graphics.Point;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;


public class app extends Activity 
{

	
   impact					m_impact;
   public os				m_os;


	public static native void aura_init(os os);
   public static native void aura_start();
	public static native boolean aura_is_started();
	private static native void sync_mem_free_available();


   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
	
		super.onCreate(savedInstanceState);



		Display display = getWindowManager().getDefaultDisplay();

		DisplayMetrics metrics = new DisplayMetrics();

		display.getRealMetrics(metrics);

		m_os = new os();

		m_os.m_iWidth = -1;

		m_os.m_iHeight = -1;

		m_os.m_fDpiX = metrics.xdpi;

		m_os.m_fDpiY = metrics.ydpi;

		m_os.m_fDensity = metrics.density;

		String prjname = "";

		String cmdline = "";

		String libname = "";

		try 
		{

			ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

			Bundle bundle = info.metaData;

			prjname = bundle.getString("project_name");

			cmdline = bundle.getString("command_line");

			libname = bundle.getString("library_name");

		}
		catch (NameNotFoundException e) 
		{

		}
		catch (NullPointerException e)
		{

		}

		m_os.m_strCommandLine = cmdline;

		System.loadLibrary("aura");

		if (libname.length() > 0) 
		{

			String[] parts = libname.split(",");

			for (int i = 0; i < parts.length; i++) 
			{

				String lib = parts[i];

				if (lib.length() > 0) 
				{

					System.loadLibrary(lib);

				}

			}

		}

		boolean bLoaded = false;
		
		if(prjname.substring(0, 8).equals("android_"))
		{

			prjname = prjname.substring(8);

		}

		if (!bLoaded) 
		{

			try 
			{

				System.loadLibrary(prjname);

				bLoaded = true;

			}
			catch (UnsatisfiedLinkError e) 
			{

			}

		}

		if (!bLoaded) 
		{

			System.loadLibrary(prjname);

			bLoaded = true;

		}

		m_os.m_strCacheDir = getApplicationContext().getCacheDir().getAbsolutePath();

		m_impact = new impact(this);

		setContentView(m_impact);

	}


	public void update_mem_free_available()
	{

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		MemoryInfo memoryinfo = new ActivityManager.MemoryInfo();
		
		activityManager.getMemoryInfo(memoryinfo);
		
		m_os.m_lMemFreeAvailableKb = memoryinfo.availMem / 1024;

		sync_mem_free_available();

	}


}



