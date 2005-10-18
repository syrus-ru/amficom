/*-
 * $Id: DetailedEvent.java,v 1.8 2005/10/18 08:06:07 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * Представляет максимальный набор информации по событию, кроме информации
 * о достоверности. Это начало, конец, тип и специфическая информация.
 * 
 * Все абс. уровни производных классов отсчитываются от максимума,
 * и имеют отрицательные значения.
 * Все отн. величины имеют знаки, соответствующие их названию. 
 * {@link #begin}, {@link #end} {@link #eventType} - см.
 * описание {@link SimpleReflectogramEvent}
 * @author $Author: saa $
 * @version $Revision: 1.8 $, $Date: 2005/10/18 08:06:07 $
 * @module
 */
public abstract class DetailedEvent
implements SimpleReflectogramEvent {

	protected int begin;
	protected int end;
	protected int eventType;

	protected DetailedEvent(SimpleReflectogramEvent sre) {
		this.begin = sre.getBegin();
		this.end = sre.getEnd();
		this.eventType = sre.getEventType();
	}
	protected DetailedEvent() {
		// empty, for dis reading
	}

	public int getBegin() {
		return this.begin;
	}
	public int getEnd() {
		return this.end;
	}
	public int getEventType() {
		return this.eventType;
	}
}
