/**
 * $Id: MapElementController.java,v 1.3 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * ��������� ����������� �������� �����. ��������� ��������� ������������
 * ��������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/01/31 12:19:18 $
 * @module mapviewclient_v1
 */
public interface MapElementController 
{
	/**
	 * ���������� ���������� ���� .
	 * @param lnl ���������� ����
	 */
	void setLogicalNetLayer(LogicalNetLayer lnl);
	
	/**
	 * �������� ���������� ����.
	 * @return ���������� ����
	 */
	LogicalNetLayer getLogicalNetLayer();

	/**
	 * ���������� �������. ��� ��������� ���������� ����������� �������� 
	 * ��������� �������� ����� � ������� �������, � ������������ ������
	 * � ���� ������.
	 * @param me ������� �����, ������� ���������� ����������
	 * @param g ����������� ��������
	 * @param visibleBounds ������� ������
	 */
	void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds);

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� {@link com.syrus.AMFICOM.Client.Map.MapPropertiesManager#getMouseTolerancy()}.
	 * @param me ������� �����
	 * @param currentMousePoint ����� � �������� �����������
	 * @return <code>true</code>, ���� ����� �� �������� �����, ����� 
	 * <code>false</code>
	 */
	boolean isMouseOnElement(MapElement me, Point currentMousePoint);

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������).
	 * @param me ������� �����
	 * @param visibleBounds ������� ������
	 * @return <code>true</code>, ���� ������� �������� � �������, ����� 
	 * <code>false</code>
	 */
	boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds);

	/**
	 * �������� ����� ����������� ��������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ������ ��� ����������� ���������
	 */
	String getToolTipText(MapElement me);
}
