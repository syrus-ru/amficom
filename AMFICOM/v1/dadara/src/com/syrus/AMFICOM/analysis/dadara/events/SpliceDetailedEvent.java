/*-
 * $Id: SpliceDetailedEvent.java,v 1.7 2005/10/18 08:06:07 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * �������� ��� ������. �� ������, �� ������ ��� �� ���������.
 * y0 - ������� ������ �������, �� (������������� ��������)
 * y1 - ������� ����� �������, �� (������������� ��������)
 * loss - ������� ������ �� ������� (��� �� ������ �� ��, ��� � ������� ������ �� ����� � � ������)
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/10/18 08:06:07 $
 * @module
 */
public class SpliceDetailedEvent extends DetailedEvent
implements HavingY0, HavingLoss {
	double y0;
	double y1;
	double loss;

	public SpliceDetailedEvent(SimpleReflectogramEvent sre, double y0,
			double y1, double loss) {
		super(sre);
		this.y0 = y0;
		this.y1 = y1;
		this.loss = loss;
	}
	public SpliceDetailedEvent() {
		// empty, for dis reading
	}

	public double getLoss() {
		return this.loss;
	}
	public double getY0() {
		return this.y0;
	}
	public double getY1() {
		return this.y1;
	}
}
