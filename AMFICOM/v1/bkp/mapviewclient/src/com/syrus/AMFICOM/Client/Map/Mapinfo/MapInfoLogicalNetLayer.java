package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class MapInfoLogicalNetLayer extends LogicalNetLayer 
{
	public MapInfoLogicalNetLayer()
	{
		super();
	}

	/**
	 * �������� �������� ���������� �� �������������� �����������
	 */
	public Point convertMapToScreen(Point2D.Double point)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public Point2D.Double convertScreenToMap(Point point)
	{
		throw new UnsupportedOperationException();
	}

	public double convertScreenToMap(double screenDistance)
	{
		throw new UnsupportedOperationException();
	}
	
	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
	}

	public Point2D.Double pointAtDistance(
			Point2D.Double startPoint, 
			Point2D.Double endPoint, 
			double dist)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������� �����������
	 */
	public double distance(Point2D.Double from, Point2D.Double to)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(Point2D.Double center)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public Point2D.Double getCenter()
	{
		throw new UnsupportedOperationException();
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����
	 */
	public void release()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ������������ ���������� ���������� � ������
	 */
	public void repaint()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * ���������� ������ ���� �� ���������� ����������� �����
	 */
	public void setCursor(Cursor cursor)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ������� ������� ���� �����
	 */
	public double getScale()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ���������� �������� ������� ���� �����
	 */
	public void setScale(double scale)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����
	 */
	public void zoomToBox(Point2D.Double from, Point2D.Double to)
	{
		throw new UnsupportedOperationException();
	}

	public void handDragged(MouseEvent me)
	{
		throw new UnsupportedOperationException();
	}

	public List findSpatialObjects(String searchText)
	{
		throw new UnsupportedOperationException();
	}

	public void centerSpatialObject(SpatialObject so)
	{
		throw new UnsupportedOperationException();
	}

}
