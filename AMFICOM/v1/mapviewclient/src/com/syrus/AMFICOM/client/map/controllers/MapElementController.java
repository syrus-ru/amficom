/**
 * $Id: MapElementController.java,v 1.6 2005/06/06 12:20:32 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * ��������� ����������� �������� �����. ��������� ��������� ������������
 * ��������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:20:32 $
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
	void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException;

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
	boolean isMouseOnElement(MapElement me, Point currentMousePoint)
		throws MapConnectionException, MapDataException;

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������).
	 * @param me ������� �����
	 * @param visibleBounds ������� ������
	 * @return <code>true</code>, ���� ������� �������� � �������, ����� 
	 * <code>false</code>
	 */
	boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException;

	/**
	 * �������� ����� ����������� ��������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ������ ��� ����������� ���������
	 */
	String getToolTipText(MapElement me);
}
