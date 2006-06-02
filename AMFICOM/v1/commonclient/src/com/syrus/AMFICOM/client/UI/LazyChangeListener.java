/*-
* $Id: LazyChangeListener.java,v 1.4 2006/06/02 10:47:04 arseniy Exp $
*
* Copyright ї 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * extendable lazy change listener
 * 
 * change state only on last event within timeout
 * 
 * @deprecated Нет описания.
 *   Первичная реализация (rev. 1.2) содержала грубую ошибку синхронизации.
 * 
 * @version $Revision: 1.4 $, $Date: 2006/06/02 10:47:04 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
@Deprecated
public class LazyChangeListener implements ChangeListener {

	long previousEventTime;		
	final int timeout;
	ChangeEvent changeEvent;
	
	final ChangeListener changeListener;
	
	public LazyChangeListener(final ChangeListener changeListener) {
		this(changeListener, 500);
	}

	public LazyChangeListener(final ChangeListener changeListener,
			final int timeout) {
		this.changeListener = changeListener;
		this.timeout = timeout;

		this.createThread();
	}
	
	private void createThread() {
		new Timer(this.timeout, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (LazyChangeListener.this.changeEvent != null && (System.currentTimeMillis() - LazyChangeListener.this.previousEventTime) > LazyChangeListener.this.timeout) {
					LazyChangeListener.this.changeListener.stateChanged(LazyChangeListener.this.changeEvent);
					LazyChangeListener.this.changeEvent = null;
				}
			}}).start();
	}

	public void stateChanged(final ChangeEvent e) {
		this.changeEvent = e;
		this.previousEventTime = System.currentTimeMillis();
	}
}

