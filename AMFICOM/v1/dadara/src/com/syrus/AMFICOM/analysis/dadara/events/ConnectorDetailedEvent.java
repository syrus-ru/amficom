/*-
 * $Id: ConnectorDetailedEvent.java,v 1.7 2005/10/18 08:06:07 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * Параметры коннектора (любого отражения, кроме начала и конца волокна)
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y1 - уровень конца события, дБ (отрицательное значение)
 * y2 - уровень в максимуме события, дБ (отрицательное значение или ноль)
 * loss - уровень потерь на событии (это не совсем то же, что и разница уровня на конце и в начале)
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/10/18 08:06:07 $
 * @module
 */
public class ConnectorDetailedEvent extends DetailedEvent
implements HavingY0, HavingLoss {
	private double y0;
	private double y1;
	private double y2;
//    private double refl;
	private double loss;

	protected ConnectorDetailedEvent() {
		// for DIS reading
	}
	public ConnectorDetailedEvent(SimpleReflectogramEvent ev, double y0,
			double y1, double y2, double loss) {
		super(ev);
		this.y0 = y0;
		this.y1 = y1;
		this.y2 = y2;
//        this.refl = refl;
		this.loss = loss;
	}

	public double getLoss() {
		return this.loss;
	}
//    public double getRefl() {
//        return refl;
//    }
	public double getY0() {
		return this.y0;
	}
	public double getY1() {
		return this.y1;
	}
	public double getY2() {
		return this.y2;
	}
	public double getAmpl() {
		return this.y2 - this.y0;
	}
}
