/*-
 * $Id: ModelTraceAndEvents.java,v 1.6 2005/06/24 12:50:57 saa Exp $
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
 * @author saa
 * @version $Revision: 1.6 $, $Date: 2005/06/24 12:50:57 $
 * @module
 */
public interface ModelTraceAndEvents
{
	double getDeltaX();
	ModelTrace getModelTrace();
	SimpleReflectogramEvent[] getSimpleEvents();
	SimpleReflectogramEvent getSimpleEvent(int nEvent);
	DetailedEvent[] getDetailedEvents(); // @todo: add getDetailedEvent(int), that would be more efficient
	int getEventByCoord(int x);
	int getNEvents();
}
