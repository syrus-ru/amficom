/*-
* $Id: LazyAdjustmentListener.java,v 1.1 2006/01/17 12:19:07 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;


/**
 * Lazy AdjustmentListener
 * 
 * @version $Revision: 1.1 $, $Date: 2006/01/17 12:19:07 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class LazyAdjustmentListener implements AdjustmentListener {
	
	private Thread		thread;
	
	long previousEventTime;		
	final long timeout;
	AdjustmentEvent adjustmentEvent;
	
	final AdjustmentListener adjustmentListener;
	
	public LazyAdjustmentListener(final AdjustmentListener changeListener) {
		this(changeListener, 500);
	}
	
	public LazyAdjustmentListener(final AdjustmentListener changeListener,
			final long timeout) {
		this.adjustmentListener = changeListener;
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
					if (adjustmentEvent != null && (System.currentTimeMillis() - previousEventTime) > timeout) {
						adjustmentListener.adjustmentValueChanged(adjustmentEvent);
						adjustmentEvent = null;			
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
	
	public void adjustmentValueChanged(AdjustmentEvent e) {
		this.adjustmentEvent = e;
		this.previousEventTime = System.currentTimeMillis();
		
	}
}

