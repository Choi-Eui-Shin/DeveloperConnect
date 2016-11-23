/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
package com.rs;

import javax.swing.JProgressBar;

public class Progress extends JProgressBar
{
	private static final int MAX = 5;
	
	class SelfThread extends Thread
	{
		private boolean running;
		
		public void run()
		{
			running = true;
			
			while(running && isInterrupted() == false)
			{
				int v = getValue();
				v = (v+1) % MAX;
				setValue(v);
				try
				{
					sleep(1000);
				}catch(Exception x) {}
			}
		}
		
		public void shutdown()
		{
			running = false;
		}
	}
	
	private SelfThread worker;
	
	public Progress() {
		setValue(0);
		setMinimum(0);
		setMaximum(MAX);
	}

	/**
	 * 
	 */
	public void start()
	{
		setValue(0);
		worker = new SelfThread();
		worker.start();
	}
	
	/**
	 * 
	 */
	public void stop()
	{
		worker.shutdown();
		worker.interrupt();
		worker = null;
		setValue(0);
	}
}
