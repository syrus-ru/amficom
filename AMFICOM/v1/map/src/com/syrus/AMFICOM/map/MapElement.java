/**
 * $Id: MapElement.java,v 1.9 2005/03/09 14:49:53 bass Exp $
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

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 * 
 * 
 * 
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/03/09 14:49:53 $
 * @module map_v1
 */
public interface MapElement extends Characterizable
{
	/**
	 * ������� ������������� ��������.
	 * @return ������������� �������� �����.
	 */
	Identifier getId();

	/**
	 * �������� �������� ��������.
	 * @return �������� �������� �����.
	 */
	String getName();

	/**
	 * ���������� �������� ��������.
	 * @param name ����� ��������.
	 */
	void setName(final String name);

	/**
	 * �������� ������ �� �������������� �����, �� ������� ������� ������ �������.
	 * @return ������ �� �������������� �����
	 */
	Map getMap();
	
	/**
	 * ���������� ������ �����. ������������ ��� �������� ������ ��������, ���
	 * ��������� �������� �� ���� ������ � ��� 
	 * @param map ������ �� ������ �����
	 */
	void setMap(final Map map);

	/**
	 * �������� ���� ��������� ��������.
	 * @return ���� ��������� ��������
	 */
	boolean isSelected();
	
	/**
	 * ���������� �������� ����� ��������� ��������.
	 * @param selected ����� �������� ����� ��������� ��������
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
	 */
	java.util.Map getExportMap();

}

