/**
 * $Id: OfxLogicalNetLayer.java,v 1.4 2004/11/10 16:00:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.component.MapViewer;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxRectangle;
import com.ofx.mapViewer.SxMapLayer;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.repository.SxSpatialObject;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * ���������� ������ ����������� ����������� ���� �� ����� ����������
 * ������ SpatialFX. ���� ��������������� ����� ������������ � �������
 * ������� ���� SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/11/10 16:00:54 $
 * @module ���_�2
 * @author $Author: krupenn $
 * @see
 */
public class OfxLogicalNetLayer extends LogicalNetLayer 
{
	/**
	 * ������-����, ���������� ������� ��������������� �����
	 */
	SxMapLayer spatialLayer = null;

	/**
	 * ������ �� ���������, ������������ �����
	 */
	SxMapViewer spatialViewer = null;
	
	public OfxLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"SpatialLogicalNetLayer(" + viewer + ")");

		spatialLayer = new AMFICOMSxMapLayer(this);
		setMapViewer(viewer);
	}

	/**
	 * �������� �������� ���������� �� �������������� �����������
	 */
	public Point convertMapToScreen(Point2D.Double point)
	{
		SxDoublePoint sdp = new SxDoublePoint(point.x, point.y);
		return spatialLayer.convertLongLatToScreen(sdp);
	}
	
	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public Point2D.Double convertScreenToMap(Point point)
	{
		SxDoublePoint sdp = spatialLayer.convertScreenToLongLat(point);
		return new Point2D.Double(sdp.x, sdp.y);
	}

	public double convertScreenToMap(double screenDistance)
	{
		Point2D.Double p1 = convertScreenToMap(new Point(0, 0));
		Point2D.Double p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		double d2 = screenDistance * spatialViewer.getScale();

		return d2;
	}
	
	public double convertMapToScreen(double topologicalDistance)
	{
		double d = topologicalDistance / spatialViewer.getScale();

		return d;

//		Point p1 = convertMapToScreen(new Point2D.Double(0, 0));
//		Point p2 = convertMapToScreen(new Point2D.Double(topologicalDistance, 0));
//		double length = Math.sqrt(
//			(p2.x - p1.x) * (p2.x - p1.x) +
//			(p2.y - p1.y) * (p2.y - p1.y) );
//		
//		return length;
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������������� �����������
	 * �������� ���������� ��������� �������� ��������, ��� ��� ��
	 * ��� ��������������� ��� �������������
	 */
	public double distance(Point2D.Double from, Point2D.Double to)
	{
		return spatialViewer.distance(from.x, from.y, to.x, to.y);
/*
		double a1 = from.x * 3.14 / 180;
		double a2 = from.y * 3.14 / 180;
		double b1 = to.x * 3.14 / 180;
		double b2 = to.y * 3.14 / 180;

		double r = 6400000;

		double d = r * Math.sqrt(
			( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2)) *
			( Math.cos(a1) * Math.cos(a2) - Math.cos(b1) * Math.cos(b2)) +

			( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2)) *
			( Math.sin(a1) * Math.cos(a2) - Math.sin(b1) * Math.cos(b2)) +

			( Math.sin(a2) - Math.sin(b2)) * ( Math.sin(a2) - Math.sin(b2)) );

		return d;
*/
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(Point2D.Double center)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setCenter(" + center.x + ", " + center.y + ")");

		spatialViewer.setCenter(center.x, center.y);
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public Point2D.Double getCenter()
	{
		Point2D.Double center = new Point2D.Double(
			spatialViewer.getCenter()[0],
			spatialViewer.getCenter()[1]);
		return center;
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		SxRectangle sxRect = spatialViewer.getMapCanvas().getGroundRect();
		sxRect = spatialViewer.convertDBToLatLong(sxRect);
		Rectangle2D.Double rect = new Rectangle2D.Double(
			sxRect.getBottomLeft().x,
			sxRect.getBottomLeft().y,
			sxRect.getWidth(),
			sxRect.getHeight());
		return rect;
	}

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����
	 */
	public void release()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"release()");
		
		spatialLayer.release();
	}

	/**
	 * ������������ ���������� ���������� � ������
	 */
	public void repaint()
	{
		spatialLayer.postDirtyEvent();
		spatialLayer.postPaintEvent();
	}
	
	/**
	 * ���������� ������ ���� �� ���������� ����������� �����
	 */
	public void setCursor(Cursor cursor)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setCursor(" + cursor.toString() + ")");
		
		spatialViewer.getMapCanvas().setCursor(cursor);
	}

	/**
	 * �������� ������� ������� ���� �����
	 */
	public double getScale()
	{
		return spatialViewer.getScale();
	}

	/**
	 * ���������� �������� ������� ���� �����
	 */
	public void setScale(double scale)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setScale(" + scale + ")");
		
		spatialViewer.setScale(scale);
		updateZoom();
	}

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"scaleTo(" + scale�oef + ")");
		
		spatialViewer.setScale(spatialViewer.getScale() * scale�oef);
		updateZoom();
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"zoomIn()");
		
		spatialViewer.zoomIn();
		updateZoom();
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"zoomOut()");
		
		spatialViewer.zoomOut();
		updateZoom();
	}
	
	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����
	 */
	public void zoomToBox(Point2D.Double from, Point2D.Double to)
	{
		spatialViewer.zoomToRect(from.x, from.y, to.x, to.y);
	}

	public void handDragged(MouseEvent me)
	{
		java.awt.Point point = new Point(
				me.getX() - startPoint.x, 
				me.getY() - startPoint.y);
		spatialViewer.getMapCanvas().setBufferOffset(point);
		spatialViewer.getMapCanvas().repaint();
	}
	
	/**
	 * ���������� ������ �� ������, ����������� ������������ �����.
	 * �������������� ����� �������� ������ � ���, ����� �������� ������
	 * �� ������ ������ SxMapViewer
	 */
	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setMapViewer(" + mapViewer + ")");
		
		super.setMapViewer(mapViewer);
		
		OfxNetMapViewer snmv = (OfxNetMapViewer)mapViewer;
		this.spatialViewer = snmv.getJMapViewer().getSxMapViewer();
		System.out.println("Units " + this.spatialViewer.getUnits());
		this.spatialViewer.setUnits(MapViewer.METERS);
	}

	public SxMapViewer getSpatialMapViewer()
	{
		return spatialViewer;
	}

	public List findSpatialObjects(String searchText)
	{
		List found = new LinkedList();

		Vector vector;
		SxQueryResultInterface objects;
		String spatialClassName;
		
		String searchTextLowCase = searchText.toLowerCase();
		String sampleLowCase;
		
		try
		{
			vector = spatialViewer.getForegroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();

				objects = spatialViewer.getQuery().getObjects(spatialClassName);
				for(Enumeration en = objects.elements(); en.hasMoreElements();)
				{
					SxSpatialObject obj = (SxSpatialObject )en.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						found.add(new OfxSpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}

			vector = spatialViewer.getBackgroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();
				objects = spatialViewer.getQuery().getObjects(spatialClassName);
				for(Enumeration en = objects.elements(); en.hasMoreElements();)
				{
					SxSpatialObject obj = (SxSpatialObject)en.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						found.add(new OfxSpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}
		}
		catch(Exception ex)
		{
		}

		return found;
	}
	
	public void centerSpatialObject(SpatialObject so)
	{
		try 
		{
			OfxSpatialObject oso = (OfxSpatialObject)so;
			SxDoublePoint center = oso.getSxSpatialObject().geometry.getCenter();
			System.out.print("Center " + center.x + ", " + center.y);
			center = spatialViewer.convertLatLongToMap(center);
			System.out.println(" --> " + center.x + ", " + center.y);
			spatialViewer.setCenter(center.x, center.y);
		} 
		catch (Exception ex) 
		{
		} 
	}

	class AMFICOMSxMapLayer extends SxMapLayer
	{
		LogicalNetLayer lnl = null;
		
		public AMFICOMSxMapLayer(LogicalNetLayer lnl)
		{
			super();
			this.lnl = lnl;
		}
		
		public void paint(Graphics g)
		{
			lnl.paint(g);
		}
	}
}

