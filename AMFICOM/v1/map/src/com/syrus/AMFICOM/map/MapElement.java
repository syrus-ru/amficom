/*-
 * $Id: MapElement.java,v 1.26 2005/09/29 10:46:56 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.26 $, $Date: 2005/09/29 10:46:56 $
 * @module map
 */
public interface MapElement extends Identifiable, Namable {
	/**
	 * �������� ���� ��������� ��������.
	 * @return ���� ��������� ��������
	 */
	boolean isSelected();
	
	/**
	 * ���������� �������� ����� ��������� ��������.
	 * ��� ��������������� ��������� �������� � ���������������� ����������
	 * ������� ������������ ����� {@link Map#setSelected(MapElement, boolean)},
	 * ������� � ������������� ���� ��� ������� ��������
	 * @param selected ����� �������� ����� ��������� ��������
	 * @see Map#setSelected(MapElement, boolean)
	 */
	void setSelected(final boolean selected);

	/**
	 * ���������� �������� ����� ������� �������.
	 * @param alarmState ����� �������� ����� ������� �������
	 */
	void setAlarmState(final boolean alarmState);
	
	/**
	 * ������� �������� ����� ������� �������.
	 * @return �������� ����� ������� �������
	 */
	boolean getAlarmState();

	/**
	 * �������� ����� (���) ��������.
	 * @return �������������� ���������� ������ ��������
	 */
	DoublePoint getLocation();

	/**
	 * �������� ������� ��������� ��������.
	 * @return ������� ��������� ��������
	 */
	MapElementState getState();
	
	/**
	 * ������������ ��������� ��������.
	 * @param state ��������� ��������
	 */
	void revert(final MapElementState state);
	
	/**
	 * ���� ����, ��� ������� ������.
	 * @return ���� �������� ��������
	 */
	boolean isRemoved();
	
	/**
	 * ���������� ���� �������� ��������.
	 * @param removed ���� �������� ��������
	 */
	void setRemoved(final boolean removed);

	Characterizable getCharacterizable();
}
