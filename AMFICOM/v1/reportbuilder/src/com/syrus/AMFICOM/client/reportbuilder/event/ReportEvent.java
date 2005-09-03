/*
 * $Id: ReportEvent.java,v 1.5 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import java.beans.PropertyChangeEvent;

/**
 * ������������ � ��������� �������� ������� ��� �������� ������� ����
 * ���������, ��� ���� �� ����� ������ ������ �������.
 * @author $Author: peskovsky $
 * @version $Revision: 1.5 $, $Date: 2005/09/03 12:42:20 $
 * @module reportbuilder_v1
 */
public abstract class ReportEvent extends PropertyChangeEvent{
	public static final String TYPE = ReportEvent.class.getName();
	
	public ReportEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source, propertyName, oldValue, newValue);
		// Empty
	}
}
