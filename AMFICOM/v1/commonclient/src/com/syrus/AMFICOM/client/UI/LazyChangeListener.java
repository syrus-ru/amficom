/*-
* $Id: LazyChangeListener.java,v 1.1 2006/01/17 12:19:07 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Lazy ChangeListener
 * @version $Revision: 1.1 $, $Date: 2006/01/17 12:19:07 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class LazyChangeListener implements ChangeListener {
	
	private Thread		thread;
	
	long previousEventTime;		
	final long timeout;
	ChangeEvent changeEvent;
	
	final ChangeListener changeListener;
	
	public LazyChangeListener(final ChangeListener changeListener) {
		this(changeListener, 500);
	}
	
	public LazyChangeListener(final ChangeListener changeListener,
			final long timeout) {
		this.changeListener = changeListener;
		this.timeout = timeout;
		
		this.createThread();
		this.thread.start();
	}
	
	private void createThread() {
		this.thread = new Thread() {

			@SuppressWarnings("unqualified-field-access")
			@Override
			public void run() {
				while (true) {
					if (changeEvent != null && (System.currentTimeMillis() - previousEventTime) > timeout) {
						changeListener.stateChanged(changeEvent);
						changeEvent = null;			
					}
					try {
						Thread.sleep(timeout);
					} catch (final InterruptedException e) {
						return;
					}
				}
			}
		};
	}
	
	public void stateChanged(final ChangeEvent e) {			
		this.changeEvent = e;
		this.previousEventTime = System.currentTimeMillis();
		
	}
}

