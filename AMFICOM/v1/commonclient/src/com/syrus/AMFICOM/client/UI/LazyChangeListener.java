/*-
* $Id: LazyChangeListener.java,v 1.3 2006/04/28 07:36:45 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2006/04/28 07:36:45 $
 * @author $Author: saa $
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
				if (changeEvent != null && (System.currentTimeMillis() - previousEventTime) > timeout) {
					changeListener.stateChanged(changeEvent);
					changeEvent = null;
				}
			}}).start();
	}

	public void stateChanged(final ChangeEvent e) {
		this.changeEvent = e;
		this.previousEventTime = System.currentTimeMillis();
	}
}

