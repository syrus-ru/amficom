/**
 * $Id: MapElement.java,v 1.3 2005/01/17 10:54:59 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/01/17 10:54:59 $
 * @module
 * @author $Author: bob $
 * @see
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
	 * ���������� ������ ����������, ����������� �������������� �����,
	 * ������� ������������ ��� ��������
	 */
	Object[][] exportColumns();

	/**
	 * ���������� �������� �������������� ����� �� ����������� ��������.
	 * ������������ ��� �������
	 */	
//	void setColumn(String field, String value);
	
}

