/**
 * $Id: OfxLogicalNetLayer.java,v 1.4 2004/12/30 12:56:24 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.base.SxDistance;
import com.ofx.component.MapViewer;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxGeometry;
import com.ofx.geometry.SxRectangle;
import com.ofx.mapViewer.SxMapLayer;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.repository.SxSpatialObject;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import com.syrus.AMFICOM.map.DoublePoint;
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
 * Реализация уровня логического отображения сети на карте средствами
 * пакета SpatialFX. Слой топографической схемы отображается с помощью
 * объекта типа SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/30 12:56:24 $
 * @module Ьфз_м2
 * @author $Author: krupenn $
 * @see
 */
public class OfxLogicalNetLayer extends LogicalNetLayer 
{
	/**
	 * Объект-слой, содержищий объекты топографической схемы
	 */
	SxMapLayer spatialLayer = null;

	/**
	 * Ссылка на компонент, отображающий карту
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
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
		SxDoublePoint sdp = new SxDoublePoint(point.getX(), point.getY());
		return spatialLayer.convertLongLatToScreen(sdp);
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		SxDoublePoint sdp = spatialLayer.convertScreenToLongLat(point);
		return new DoublePoint(sdp.getX(), sdp.getY());
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		return d;
//		double d2 = screenDistance * spatialViewer.getScale();
	}

	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
//		double d = topologicalDistance / spatialViewer.getScale();
//		return d;
	}

	public DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist)
	{
		double x = startPoint.getX();
		double y = startPoint.getY();
		double len = distance(
				startPoint, 
				endPoint);
		x += (endPoint.getX() - startPoint.getX()) / len * dist;
		y += (endPoint.getY() - startPoint.getY()) / len * dist;
		return new DoublePoint(x, y);
	}
	
	/**
	 * Получить дистанцию между двумя точками в географических координатах
	 * Алгорита мычисления дастанции остается загадкой, так как не
	 * был закомментирован его разработчиком
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		return spatialViewer.distance(from.getX(), from.getY(), to.getX(), to.getY());
	}

	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(DoublePoint center)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setCenter(" + center.getX() + ", " + center.getY() + ")");

		spatialViewer.setCenter(center.getX(), center.getY());
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
		DoublePoint center = new DoublePoint(
			spatialViewer.getCenter()[0],
			spatialViewer.getCenter()[1]);
		return center;
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		SxRectangle sxRect = spatialViewer.getMapCanvas().getGroundRect();
		sxRect = spatialViewer.convertDBToLatLong(sxRect);
		Rectangle2D.Double rect = new Rectangle2D.Double(
			sxRect.getBottomLeft().getX(),
			sxRect.getBottomLeft().getY(),
			sxRect.getWidth(),
			sxRect.getHeight());
		return rect;
	}

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты
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
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
		spatialLayer.postDirtyEvent();
		spatialLayer.postPaintEvent();
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
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

	public Cursor getCursor()
	{
		return spatialViewer.getMapCanvas().getCursor();
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		return spatialViewer.getScale();
	}

	/**
	 * Установить заданный масштаб вида карты
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
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"scaleTo(" + scaleСoef + ")");
		
		spatialViewer.setScale(spatialViewer.getScale() * scaleСoef);
		updateZoom();
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
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
	 * Отдалить вид карты со стандартным коэффициентом
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
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		spatialViewer.zoomToRect(from.getX(), from.getY(), to.getX(), to.getY());
		updateZoom();
	}

	public void updateZoom()
	{
		super.updateZoom();

		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		Double p = new Double(getScale());
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
	}

	public void handDragged(MouseEvent me)
	{
		java.awt.Point point = new Point(
				me.getX() - (int )startPoint.getX(), 
				me.getY() - (int )startPoint.getY());
		spatialViewer.getMapCanvas().setBufferOffset(point);
		spatialViewer.getMapCanvas().repaint();
	}
	
	/**
	 * Установить ссылку на объект, управляющий отображением карты.
	 * Переопределяет метод базового класса с тем, чтобы получить ссылку
	 * на объект класса SxMapViewer
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
			System.out.print("Center " + center.getX() + ", " + center.getY());
			center = spatialViewer.convertLatLongToMap(center);
			System.out.println(" --> " + center.getX() + ", " + center.getY());
			spatialViewer.setCenter(center.getX(), center.getY());
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

