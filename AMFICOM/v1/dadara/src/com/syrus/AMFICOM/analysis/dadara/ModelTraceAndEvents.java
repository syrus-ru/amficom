/*-
 * $Id: ModelTraceAndEvents.java,v 1.4 2005/05/05 11:45:28 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;

/**
 * ������������� ���������� � �.�. � ������ ������� ������������ �/�.
 * ��� ��� ����� ������� �������� �����:
 * ��� ������������ ���������� (� �������), ������������ ������������
 * ��� ����������� ������������� ������ (�.�. �.�.) � ���������� �������,
 * ������������ ����� � �������, �.�. �� ��� ����� ����������� ������
 * ��� �.�.
 * 
 * @author $Author: saa $
 * @author: saa
 * @version $Revision: 1.4 $, $Date: 2005/05/05 11:45:28 $
 * @module
 */
public interface ModelTraceAndEvents
{
	double getDeltaX();
	ModelTrace getModelTrace();
	SimpleReflectogramEvent[] getSimpleEvents();
	SimpleReflectogramEvent getSimpleEvent(int nEvent);
//  ComplexReflectogramEvent[] getComplexEvents();
	DetailedEvent[] getDetailedEvents(); // @todo: add getDetailedEvent(int),it's more efficient
	int getEventByCoord(int x);
	int getNEvents();
}
