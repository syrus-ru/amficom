/**
 * $Id: MapElement.java,v 1.13 2005/05/30 14:50:23 krupenn Exp $
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

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 *
 *
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/05/30 14:50:23 $
 * @module map_v1
 */
public interface MapElement extends Characterizable, Namable
{
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
	java.util.Map getExportMap();

}

