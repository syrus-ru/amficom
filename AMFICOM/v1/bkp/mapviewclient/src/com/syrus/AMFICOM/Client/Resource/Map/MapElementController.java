package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public interface MapElementController 
{
	void setLogicalNetLayer(LogicalNetLayer lnl);
	
	LogicalNetLayer getLogicalNetLayer();

	/**
	 * ��������� ��������
	 */
	void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds);

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� mouseTolerancy
	 */
	boolean isMouseOnElement(MapElement me, Point currentMousePoint);

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������)
	 */
	boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds);

	/**
	 * ����� ����������� ���������
	 */
	String getToolTipText(MapElement me);
}