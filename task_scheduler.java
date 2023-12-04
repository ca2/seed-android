package com.ace;

import android.os.Handler;
import android.util.ArrayMap;


/**
*  Created by Ofek on 19/08/2015.
*/
//
//
// tweaked by Camilo on 2020-12-28 <3TBS, Mummi and bilbo!!
// with ca2 refactoring 
//
//
// see commit messages at https://github.com/ca2/seed-android/...
//
//
public class task_scheduler extends Handler 
{


	private ArrayMap<Runnable,Runnable> m_runnablea = new ArrayMap<>();
   

	public void scheduleAtFixedRate(final Runnable runnable,long delay,final long period)
	{
      
		Runnable runnableScheduled = new Runnable()
      {
      
			@Override
         public void run()
			{
         
    			try
            {
            
   				runnable.run();

            }
            finally
            {

            }
            
				postDelayed(this, period);

         }
      
      };
      
      m_runnablea.put(runnable, runnableScheduled);
      
      postDelayed(runnableScheduled, delay);

   }


   public void scheduleAtFixedRate(final Runnable runnable, final long period)
   {

      scheduleAtFixedRate(runnable, period, period);
    
   }

   
   public void stop(Runnable runnable)
   {
    
      Runnable runnableScheduled = m_runnablea.remove(runnable);

      if (runnableScheduled != null) 
      {
    	  
    	   removeCallbacks(runnableScheduled);

      }

   }

}



