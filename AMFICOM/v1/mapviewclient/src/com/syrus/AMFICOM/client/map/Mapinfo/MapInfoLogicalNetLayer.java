package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

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
		return null;
	}
	
	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public Point2D.Double convertScreenToMap(Point point)
	{
		return null;
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������� �����������
	 */
	public double distance(Point2D.Double from, Point2D.Double to)
	{
		return 0D;
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(Point2D.Double center)
	{
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public Point2D.Double getCenter()
	{
		return null;
	}

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����
	 */
	public void release()
	{
	}

	/**
	 * ������������ ���������� ���������� � ������
	 */
	public void repaint()
	{
	}
	
	/**
	 * ���������� ������ ���� �� ���������� ����������� �����
	 */
	public void setCursor(Cursor cursor)
	{
	}

	/**
	 * �������� ������� ������� ���� �����
	 */
	public double getScale()
	{
		return 1D;
	}

	/**
	 * ���������� �������� ������� ���� �����
	 */
	public void setScale(double scale)
	{
	}

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
	}
	
	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����
	 */
	public void zoomToBox(Point2D.Double from, Point2D.Double to)
	{
	}

	public void handDragged(MouseEvent me)
	{
	}

	public List findSpatialObjects(String searchText)
	{
		return new LinkedList();
	}

	public void centerSpatialObject(SpatialObject so)
	{
	}

}
