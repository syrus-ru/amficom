/**
 * $Id: MapElement.java,v 1.5 2005/01/18 15:42:25 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/01/18 15:42:25 $
 * @module map_v1
 */
public interface MapElement 
{
	/**
	 * ������� ������������� ��������
	 */
	Identifier getId();

	String getName();

	void setName(String name);

	/**
	 * ���������� �����
	 */
	Map getMap();
	
	/**
	 * ���������� ������ �����
	 */
	void setMap(Map map);

	/**
	 * ���� ��������� ��������
	 */
	boolean isSelected();
	
	/**
	 * ���������� ��������� ��������
	 */
	void setSelected(boolean selected);

	void setAlarmState(boolean alarmState);
	
	boolean getAlarmState();

	/**
	 * ����� (���) ��������
	 */
	DoublePoint getLocation();

	/**
	 * �������� ������� ��������� ��������
	 */
	MapElementState getState();
	
	/**
	 * ������������ ��������� ��������
	 */
	void revert(MapElementState state);
	
	/**
	 * ���� ����, ��� ������� ������
	 */
	boolean isRemoved();
	
	/**
	 * ���������� ���� �������� ��������
	 */
	void setRemoved(boolean removed);

	List getCharacteristics();
	
	void addCharacteristic(Characteristic characteristic);

	void removeCharacteristic(Characteristic characteristic);

	/**
	 * @deprecated use {@link #getExportMap()}
	 * ���������� ������ ����������, ����������� �������������� �����,
	 * ������� ������������ ��� ��������
	 */
	Object[][] exportColumns();
	
	java.util.Map getExportMap();

	/**
	 * ���������� �������� �������������� ����� �� ����������� ��������.
	 * ������������ ��� �������
	 */	
//	void setColumn(String field, String value);
	
}

