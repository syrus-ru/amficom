/*-
 * $Id: ProcessingDialogDummy.java,v 1.2 2006/03/06 14:55:41 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.client.launcher.Launcher;

/**
 * Used to execute Runnable immediately.
 * This class have been introduced as a workaround with problems
 * caused by poor understanding of multithreading programming
 * for Swing, see bug 476.
 * 
 * @todo This version does not display any "process..." window.
 * It is worth displaying some kind of "prosess" window
 * before getting unresponsible...
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/03/06 14:55:41 $
 * @module
 */
public class ProcessingDialogDummy {
	public ProcessingDialogDummy(Runnable runnable, String string) {
		try {
			runnable.run();
		} catch (final Throwable throwable) {
			// too unlikely
			new Launcher.DefaultThrowableHandler().handle(throwable);
		}
	}
}
