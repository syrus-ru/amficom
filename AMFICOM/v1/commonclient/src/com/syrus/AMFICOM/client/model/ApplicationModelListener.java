/*-
 * $Id: ApplicationModelListener.java,v 1.2 2006/04/03 11:57:15 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @author Andrei Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/04/03 11:57:15 $
 * @module commonclient
 */
public interface ApplicationModelListener {
	// ��� ���������� � ������ ���������� ������� �������� �����������
	// � ��������� ������ ���������� ���������
	void modelChanged(String elementNames[]);
	
	void modelChanged(String elementName);
}
