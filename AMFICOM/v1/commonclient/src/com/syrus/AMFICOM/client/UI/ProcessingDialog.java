/*-
* $Id: ProcessingDialog.java,v 1.18 2005/11/08 08:11:12 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Log;
import com.syrus.util.WorkQueue;


/**
 * 
 * Using as blocking (modal) dialog processing task 
 * 
 * @version $Revision: 1.18 $, $Date: 2005/11/08 08:11:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class ProcessingDialog {

	final static Level LOGLEVEL = Log.DEBUGLEVEL08;
	
	volatile static Map<Runnable, String> runnableTaskNames = 
		new HashMap<Runnable, String>();	
	
	private static final DisplayQueue DISPLAY_QUEUE = new DisplayQueue();
	
	public ProcessingDialog(final Runnable runnable, final String title) {
		DISPLAY_QUEUE.enqueue(runnable);
		runnableTaskNames.put(runnable, title);
	}
	
	private static class RunnableQueue extends WorkQueue<Runnable> {
		@Override
		protected void processingItem(final Runnable workItem) 
		throws InterruptedException {
			Log.debugMessage("in " + workItem,
				LOGLEVEL);
			workItem.run();
			Log.debugMessage("out " + workItem,
				LOGLEVEL);
		}
	}
	
	private static class DisplayQueue extends WorkQueue<Runnable> {

		final RunnableQueue runnableQueue;

		public DisplayQueue() {			
			this.runnableQueue =  new RunnableQueue();
		}
		
		@Override
		protected void processingItem(final Runnable workItem) 
		throws InterruptedException {
			final String title = runnableTaskNames.remove(workItem);
			Log.debugMessage("thread " 
					+ title 
					+ " in " 
					+ workItem,
				LOGLEVEL);
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
			modalDialog.setTitle(title);
			progressBar.setString(title);
			final ComponentListener componentListener = new ComponentAdapter() {
				@Override
				public void componentShown(ComponentEvent e) {
					DisplayQueue.this.runnableQueue.enqueue(new Runnable() {

						public void run() {
							try {
								workItem.run();
							} catch (final Throwable throwable) {
								// too unlikely
								new Launcher.DefaultThrowableHandler().handle(throwable);
							}
							modalDialog.dispose();
						}
					});
					
					modalDialog.removeComponentListener(this);
					Log.debugMessage("thread " 
							+ title 
							+ " out " 
							+ workItem,
						LOGLEVEL);
				}
			};
			modalDialog.addComponentListener(componentListener);
			modalDialog.setVisible(true);
			
		}
	}
	

}

