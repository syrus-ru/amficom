/*-
* $Id: ProcessingDialog.java,v 1.14 2005/10/26 07:27:43 bob Exp $
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
 * @version $Revision: 1.14 $, $Date: 2005/10/26 07:27:43 $
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
	
	private static class RunnableQueue extends WorkQueue {
		@Override
		protected void processingItem(final Runnable workItem) 
		throws InterruptedException {
			assert Log.debugMessage("RunnableQueue.processingItem | in " + workItem,
				LOGLEVEL);
			workItem.run();
			assert Log.debugMessage("RunnableQueue.processingItem | out " + workItem,
				LOGLEVEL);
		}
	}
	
	private static class DisplayQueue extends WorkQueue {

		final JDialog	modalDialog;
		private final JProgressBar	progressBar;
		final RunnableQueue runnableQueue;

		public DisplayQueue() {
			
			this.runnableQueue =  new RunnableQueue();
			
			final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
			final Dimension screenSize = defaultToolkit.getScreenSize();
			
			this.modalDialog = 
				new JDialog(Environment.getActiveWindow(), true);
			this.progressBar = new JProgressBar();
			this.progressBar.setStringPainted(true);
			this.progressBar.setIndeterminate(true);
			this.modalDialog.getContentPane().add(this.progressBar);
			this.modalDialog.pack();
			this.modalDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.modalDialog.setSize(screenSize.width / 3, this.modalDialog.getHeight());
			this.modalDialog.setLocation((screenSize.width - this.modalDialog.getWidth())/2,
					(screenSize.height - this.modalDialog.getHeight())/2);
		}
		
		@Override
		protected void processingItem(final Runnable workItem) 
		throws InterruptedException {
			final String title = runnableTaskNames.remove(workItem);
			assert Log.debugMessage("DisplayQueue.processingItem | thread " 
					+ title 
					+ " in " 
					+ workItem,
				LOGLEVEL);
			this.modalDialog.setTitle(title);
			this.progressBar.setString(title);
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
							DisplayQueue.this.modalDialog.setVisible(false);							
						}
					});
					
					DisplayQueue.this.modalDialog.removeComponentListener(this);
					assert Log.debugMessage("DisplayQueue.processingItem | thread " 
							+ title 
							+ " out " 
							+ workItem,
						LOGLEVEL);
				}
			};
			this.modalDialog.addComponentListener(componentListener);
			this.modalDialog.setVisible(true);
			
		}
	}
	

}

