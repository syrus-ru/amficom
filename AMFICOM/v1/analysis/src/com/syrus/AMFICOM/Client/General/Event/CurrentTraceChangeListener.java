/*-
 * $Id: CurrentTraceChangeListener.java,v 1.1 2005/03/31 18:41:17 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/31 18:41:17 $
 * @module
 */
public interface CurrentTraceChangeListener
{
	// ����������, ��� � ������ ������� ����� �/�
	void currentTraceChanged(String id);
	
	// �� ���������� � �������� ������� �/� - � ������ � ���,
	// ��� ������� ����� ������
}
