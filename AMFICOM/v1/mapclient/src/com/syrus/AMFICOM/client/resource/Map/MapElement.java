/**
 * $Id: MapElement.java,v 1.4 2004/09/13 12:02:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface MapElement 
{
	/**
	 * ��������� ��������
	 */
	void paint (Graphics g);

	/**
	 * ������� ������������� ��������
	 */
	String getId();

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

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� mouseTolerancy
	 */
	boolean isMouseOnThisObject(Point currentMousePoint);

	/**
	 * ��������� ��������� �������� � ���������� �������� ������������
	 */
//	MapStrategy getMapStrategy();

	/**
	 * ����������� ���� ��������
	 */
//	MapPopupMenu getContextMenu();
	
	/**
	 * ������ ������� ��������
	 */
//	String getPropertyPaneClassName();

	/**
	 * ���� ����������� ������������ ��������
	 */
	boolean isMovable();
	
	/**
	 * �������� ������� � �������� ���������
	 */
//	void move (double deltaX, double deltaY);

	/**
	 * ����� ����������� ���������
	 */
	String getToolTipText();

	/**
	 * ����� (���) ��������
	 */
	Point2D.Double getAnchor();

	/**
	 * ������������ ��������
	 */	
	Object clone(DataSourceInterface dataSource);
	
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
}

