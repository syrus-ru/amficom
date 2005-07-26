/*-
 * $Id: MapElement.java,v 1.15 2005/07/26 09:23:33 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.Map;

import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 *
 *
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/07/26 09:23:33 $
 * @module map_v1
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

	/**
	 * ���������� ����������� ������� ����� ����������,
	 * ������� ������������ ��� ��������.
	 * @return ���-������� ���������� ��������
	 * @deprecated use <pre>XMLBeansTransferable.fillXMLTransferable(XMLObject)</pre> instead
	 */
	@Deprecated
	Map getExportMap();
}

