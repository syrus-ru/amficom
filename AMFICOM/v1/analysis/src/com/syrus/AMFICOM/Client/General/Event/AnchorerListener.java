/*-
 * $Id: AnchorerListener.java,v 1.1 2005/11/18 10:28:38 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * ��������� �� ��������� � ���������� Heap.anchorer.
 * � ������ ������ ������� ����� ���������� Heap.anchorer �����
 * ���������� � ���������, ������������� � ��������.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/11/18 10:28:38 $
 * @module analysis
 */
public interface AnchorerListener {
	/**
	 * ���������� ��� ��������, ��������� ��� �������� ��������.
	 * � ������, ���� �������� ��������� ��� ����������,
	 * �������������, ��� �� ������ ������ ����� ������
	 * ������ ����������, � ���������
	 * ��������� � ���������, ������������� � ��������.
	 */
	void anchorerChanged();
}
