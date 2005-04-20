/*-
 * $Id: CurrentEventChangeListener.java,v 1.2 2005/04/20 13:14:08 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/04/20 13:14:08 $
 * @module
 */
public interface CurrentEventChangeListener
{
	// Уведомляет, что выбрано новое событие или выбор отменен.
	// Рассылается при любом измении пары currentEvent, currentEtalonEvent,
	// в т.ч. если изменилось только currentEtalonEvent
	void currentEventChanged();
}
