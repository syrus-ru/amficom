/*-
 * $Id: MapElement.java,v 1.19 2005/08/12 14:24:16 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 *
 *
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.19 $, $Date: 2005/08/12 14:24:16 $
 * @module map
 */
public interface MapElement extends Characterizable, Namable {
	/**
	 * ������� ������������� ��������.
	 * @return ������������� �������� �����.
	 */
	Identifier getId();

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
}

