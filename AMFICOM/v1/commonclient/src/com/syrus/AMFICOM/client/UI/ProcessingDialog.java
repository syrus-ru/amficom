/*-
* $Id: ProcessingDialog.java,v 1.3 2005/09/19 07:56:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Log;


/**
 * 
 * Using as blocking (modal) dialog processing task 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/19 07:56:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class ProcessingDialog {

	final static Object LOCK = new Object(); 
	
	static List<Runnable> runnableTasks = 
		new ArrayList<Runnable>();
	static Map<Runnable, String> runnableTaskNames = 
		new HashMap<Runnable, String>();
	
	public ProcessingDialog(final Runnable runnable, final String title) {
		final String threadName = Thread.currentThread().getName();
		assert Log.debugMessage("ProcessingDialog.ProcessingDialog | before LOCK " + new Date() + " " + title + '[' + threadName + ']',
			Log.DEBUGLEVEL10);
		synchronized (LOCK) {
			assert Log.debugMessage("ProcessingDialog.ProcessingDialog | LOCK " + new Date() + " " + title + '[' + threadName + ']',
				Log.DEBUGLEVEL10);			
			runnableTasks.add(runnable);
			runnableTaskNames.put(runnable, title);
		}
		assert Log.debugMessage("ProcessingDialog.ProcessingDialog | after LOCK " + new Date() + " " + title + '[' + threadName + ']',
			Log.DEBUGLEVEL10);
		this.startIfItNeeded();
	}
	
	private void  startIfItNeeded() {
		if (runnableTasks.size() > 1) {
			return;
		}
		
		final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = defaultToolkit.getScreenSize();
		
		final JDialog modalDialog = 
			new JDialog(Environment.getActiveWindow(), true);
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);
		modalDialog.getContentPane().add(progressBar);
		modalDialog.pack();
		modalDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		modalDialog.setSize(screenSize.width / 3, modalDialog.getHeight());
		modalDialog.setLocation((screenSize.width - modalDialog.getWidth())/2,
				(screenSize.height - modalDialog.getHeight())/2);
		
		modalDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				new Thread() {
					@Override
					public void run() {
						
						final String threadName = currentThread().getName();
						
						while(!runnableTasks.isEmpty()) {
							final Runnable runnable;
							final String title;							
							assert Log.debugMessage(".run | before LOCK " + new Date()  + '[' + threadName + ']' , Log.DEBUGLEVEL10);
							synchronized (LOCK) {
								assert Log.debugMessage(".run | LOCK " + new Date() + '[' + threadName + ']', Log.DEBUGLEVEL10);
								runnable = runnableTasks.remove(0);
								title = runnableTaskNames.remove(runnable);
							}
							assert Log.debugMessage(".run | after LOCK " + new Date() + '[' + threadName + ']', Log.DEBUGLEVEL10);
							modalDialog.setTitle(title);
							progressBar.setString(title);
							try {
								runnable.run();
							} catch(final Throwable throwable) {
								// too unlikely
								new Launcher.DefaultThrowableHandler().handle(throwable);
							}
						}
						modalDialog.dispose();
						
					}
				}.start();								
			}
		});		
		modalDialog.setVisible(true);
	}
	
}

