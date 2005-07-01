/*-
 * $Id: RefMismatchListener.java,v 1.1 2005/07/01 17:53:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Подписчик информации об изменении Heap.refMismatch.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/07/01 17:53:02 $
 * @module
 */
public interface RefMismatchListener {
	void refMismatchCUpdated(); // added or changed
	void refMismatchRemoved(); // removed
}
