/**
 * $Id: MapElement.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
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
//	String[][] getExportColumns();

	/**
	 * ���������� �������� �������������� ����� �� ����������� ��������.
	 * ������������ ��� �������
	 */	
//	void setColumn(String field, String value);
	
}

