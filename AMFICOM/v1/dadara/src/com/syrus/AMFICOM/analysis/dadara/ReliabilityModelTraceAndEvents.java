/*-
 * $Id: ReliabilityModelTraceAndEvents.java,v 1.2 2005/07/18 12:41:10 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/07/18 12:41:10 $
 * @module
 */
public interface ReliabilityModelTraceAndEvents extends ModelTraceAndEvents {
    // ReliabilitySimpleReflectogramEvent[] getSimpleEvents();
    // ReliabilitySimpleReflectogramEvent getSimpleEvent(int nEvent);
}
