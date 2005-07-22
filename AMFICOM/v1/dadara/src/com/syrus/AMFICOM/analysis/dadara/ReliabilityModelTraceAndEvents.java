/*-
 * $Id: ReliabilityModelTraceAndEvents.java,v 1.3 2005/07/22 06:39:51 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ��������� {@link ModelTraceAndEvents} ����������� � ���������� �������.
 * �������� ����� ���������� ����������� � ���, ���
 * ����� {@link #getSimpleEvents()} ����� ����������
 * ������ {@link ReliabilitySimpleReflectogramEvent},
 * � �� ������ ������ {@link SimpleReflectogramEvent},
 * ��� ��� ���������� ����������������.
 * 
 * ����������, ����� {@link #getSimpleEvent(int)} ����� ����������
 * {@link ReliabilitySimpleReflectogramEvent},
 * � �� ������ {@link SimpleReflectogramEvent}.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.3 $, $Date: 2005/07/22 06:39:51 $
 * @module
 */
public interface ReliabilityModelTraceAndEvents extends ModelTraceAndEvents {
	// ReliabilitySimpleReflectogramEvent[] getSimpleEvents();
	// ReliabilitySimpleReflectogramEvent getSimpleEvent(int nEvent);
}
