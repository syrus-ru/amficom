/**
 * $Id: MapElement.java,v 1.2 2005/01/13 15:14:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import java.util.List;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/13 15:14:00 $
 * @module
 * @author $Author: krupenn $
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
	
	void addCharacteristic(Characteristic ch);

	void removeCharacteristic(Characteristic ch);

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

