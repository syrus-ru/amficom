/**
 * $Id: MapElement.java,v 1.9 2004/09/28 07:58:36 krupenn Exp $
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

//import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *  ��������� ��� ���� ��������� �����. ����� ��� ����������� ���������
 *  ��������, ��� ���������, ������, ������������, ����������� � �.�.
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/09/28 07:58:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface MapElement 
{
	/**
	 * ��������� ��������
	 */
	void paint (Graphics g, Rectangle2D.Double visibleBounds);

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

	boolean isVisible(Rectangle2D.Double visibleBounds);

	/**
	 * ������ ������� ��������
	 */
//	String getPropertyPaneClassName();

	/**
	 * ���� ����������� ������������ ��������
	 */
	boolean isMovable();
	
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
	Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException;
	
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

	String[][] getExportColumns();

	void setColumn(String field, String value);
	
//	ObjectResourceController getPropertiesController();
}

