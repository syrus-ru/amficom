/*-
* $Id: ProcessingDialog.java,v 1.1 2005/09/18 13:13:41 bob Exp $
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

import com.syrus.AMFICOM.client.model.Environment;


/**
 * 
 * Using as blocking (modal) dialog processing task 
 * 
 * @version $Revision: 1.1 $, $Date: 2005/09/18 13:13:41 $
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
		synchronized (LOCK) {
			runnableTaskNames.put(runnable, title);
			runnableTasks.add(runnable);
		}			
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
		modalDialog.setSize(screenSize.width / 3, modalDialog.getHeight());
		modalDialog.setLocation((screenSize.width - modalDialog.getWidth())/2,
				(screenSize.height - modalDialog.getHeight())/2);
		modalDialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				new Thread() {
					@Override
					public void run() {
						for(int i = 0; i < runnableTasks.size(); i++) {
							final Runnable runnable = runnableTasks.get(i);
							final String title = runnableTaskNames.get(runnable);
							modalDialog.setTitle(title);
							progressBar.setString(title);
							runnable.run();
						}
						synchronized (LOCK) {
							runnableTasks.clear();
							runnableTaskNames.clear();
							modalDialog.dispose();
						}
					}
				}.start();								
			}
		});		
		modalDialog.setVisible(true);
	}
	
}

