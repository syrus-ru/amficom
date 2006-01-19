/*-
* $Id: LazyChangeListener.java,v 1.2 2006/01/19 09:59:18 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * extendable lazy change listener
 * 
 * change state only on last event within timeout
 * 
 * @version $Revision: 1.2 $, $Date: 2006/01/19 09:59:18 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class LazyChangeListener implements ChangeListener {
	
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

