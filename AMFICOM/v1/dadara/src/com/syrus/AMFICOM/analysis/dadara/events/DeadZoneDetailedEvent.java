/*-
 * $Id: DeadZoneDetailedEvent.java,v 1.7 2006/01/12 14:06:29 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * ��������� ������� ����
 * po - ������������������ (�� �������� ���. �������) ������� ��� ������ ������� (������������� ��������)
 * adz - ������ ADZ, � ������
 * edz - ������ EDZ, � ������
 * FIXME: ��� ���������� �����-�����? ��� DZ? ����� ���� ����� P0,Y1? ��� EOT? ����� ������ Y0?
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2006/01/12 14:06:29 $
 * @module
 */
public class DeadZoneDetailedEvent extends DetailedEvent {
	private double po;
	private double y1;
	private int edz;
	private int adz;

	public DeadZoneDetailedEvent(SimpleReflectogramEvent sre,
			double po, double y1, int edz, int adz) {
		super(sre);
		this.po = po;
		this.y1 = y1;
		this.edz = edz;
		this.adz = adz;
	}
	protected DeadZoneDetailedEvent() {
		// for DIS reading
	}

	public int getAdz() {
		return this.adz;
	}
	public int getEdz() {
		return this.edz;
	}
	public double getPo() {
		return this.po;
	}
	public double getY1() {
		return this.y1;
	}
}
