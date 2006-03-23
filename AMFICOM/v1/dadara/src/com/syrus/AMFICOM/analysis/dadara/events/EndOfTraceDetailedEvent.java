/*-
 * $Id: EndOfTraceDetailedEvent.java,v 1.8 2006/03/23 08:55:04 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * Конец волокна
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y2 - уровень максимума, дБ (отрицательное значение или ноль)
 * @author $Author: saa $
 * @version $Revision: 1.8 $, $Date: 2006/03/23 08:55:04 $
 * @module
 */
public class EndOfTraceDetailedEvent extends DetailedEvent
implements HavingY0, HavingAmpl {
	private double y0;
	private double y2;
	//private double refl;

	protected EndOfTraceDetailedEvent() {
		// for DIS reading
	}
	public EndOfTraceDetailedEvent(SimpleReflectogramEvent sre,
			double y0, double y2) {
		super(sre);
		this.y0 = y0;
		this.y2 = y2;
//        this.refl = refl;
	}

//    public double getRefl() {
//        return refl;
//    }
//    public void setRefl(double refl) {
//        this.refl = refl;
//    }
	public double getAmpl() {
		return this.y2 - this.y0;
	}
	public double getY0() {
		return this.y0;
	}
	public double getY2() {
		return this.y2;
	}
}
