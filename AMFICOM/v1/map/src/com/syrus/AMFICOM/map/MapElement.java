/**
 * $Id: MapElement.java,v 1.8 2005/02/09 15:12:53 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;

import java.util.List;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/09 15:12:53 $
 * @module map_v1
 */
public interface MapElement 
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
	void setName(String name);

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
	void setMap(Map map);

	/**
	 * �������� ���� ��������� ��������.
	 * @return ���� ��������� ��������
	 */
	boolean isSelected();
	
	/**
	 * ���������� �������� ����� ��������� ��������.
	 * @param selected ����� �������� ����� ��������� ��������
	 */
	void setSelected(boolean selected);

	/**
	 * ���������� �������� ����� ������� �������.
	 * @param alarmState ����� �������� ����� ������� �������
	 */
	void setAlarmState(boolean alarmState);
	
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
	void revert(MapElementState state);
	
	/**
	 * ���� ����, ��� ������� ������.
	 * @return ���� �������� ��������
	 */
	boolean isRemoved();
	
	/**
	 * ���������� ���� �������� ��������.
	 * @param removed ���� �������� ��������
	 */
	void setRemoved(boolean removed);

	/**
	 * �������� ������ ��������� �����������.
	 * @return ������ ��������� �����������
	 */
	List getCharacteristics();
	
	/**
	 * �������� �������� ����� ������� �����������.
	 * @param characteristic ����� ������� �����������
	 */
	void addCharacteristic(Characteristic characteristic);

	/**
	 * ������ �� �������� ������� �����������.
	 * @param characteristic ������� �����������
	 */
	void removeCharacteristic(Characteristic characteristic);

	/**
	 * ���������� ����������� ������� ����� ����������,
	 * ������� ������������ ��� ��������.
	 * @return ���-������� ���������� ��������
	 */
	java.util.Map getExportMap();

}

