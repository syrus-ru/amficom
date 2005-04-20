/*-
 * $Id: CurrentEventChangeListener.java,v 1.1 2005/04/20 12:24:14 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/20 12:24:14 $
 * @module
 */
public interface CurrentEventChangeListener
{
	// Уведомляет, что выбрано новое событие или выбор отменен.
	void currentEventChanged();
}
