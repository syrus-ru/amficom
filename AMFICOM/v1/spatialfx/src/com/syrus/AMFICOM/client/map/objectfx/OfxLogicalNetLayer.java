/**
 * $Id: OfxLogicalNetLayer.java,v 1.5 2005/02/10 11:37:49 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.ObjectFX;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.ofx.component.MapViewer;
import com.ofx.geometry.SxDoublePoint;
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

/**
 * Реализация уровня логического отображения сети на карте средствами
 * пакета SpatialFX. Слой топографической схемы отображается с помощью
 * объекта типа SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/02/10 11:37:49 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
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

		this.spatialLayer = new AMFICOMSxMapLayer(this);
		setMapViewer(viewer);
	}

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
		SxDoublePoint sdp = new SxDoublePoint(point.getX(), point.getY());
		return this.spatialLayer.convertLongLatToScreen(sdp);
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		SxDoublePoint sdp = this.spatialLayer.convertScreenToLongLat(point);
		return new DoublePoint(sdp.getX(), sdp.getY());
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		return d;
//		double d2 = screenDistance * this.spatialViewer.getScale();
	}

	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
//		double d = topologicalDistance / this.spatialViewer.getScale();
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
		return this.spatialViewer.distance(from.getX(), from.getY(), to.getX(), to.getY());
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

		this.spatialViewer.setCenter(center.getX(), center.getY());
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
		DoublePoint center = new DoublePoint(
			this.spatialViewer.getCenter()[0],
			this.spatialViewer.getCenter()[1]);
		return center;
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		SxRectangle sxRect = this.spatialViewer.getMapCanvas().getGroundRect();
		sxRect = this.spatialViewer.convertDBToLatLong(sxRect);
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
		
		this.spatialLayer.release();
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
		this.spatialLayer.postDirtyEvent();
		this.spatialLayer.postPaintEvent();
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
		
		this.spatialViewer.getMapCanvas().setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return this.spatialViewer.getMapCanvas().getCursor();
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		return this.spatialViewer.getScale();
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
		
		this.spatialViewer.setScale(scale);
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
		
		this.spatialViewer.setScale(this.spatialViewer.getScale() * scaleСoef);
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
		
		this.spatialViewer.zoomIn();
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
		
		this.spatialViewer.zoomOut();
		updateZoom();
	}
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		this.spatialViewer.zoomToRect(from.getX(), from.getY(), to.getX(), to.getY());
		updateZoom();
	}

	public void updateZoom()
	{
		super.updateZoom();

		if(this.aContext == null)
			return;
		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;
		Double p = new Double(getScale());
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
	}

	public void handDragged(MouseEvent me)
	{
		java.awt.Point point = new Point(
				me.getX() - (int )this.startPoint.getX(), 
				me.getY() - (int )this.startPoint.getY());
		this.spatialViewer.getMapCanvas().setBufferOffset(point);
		this.spatialViewer.getMapCanvas().repaint();
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
		return this.spatialViewer;
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
			vector = this.spatialViewer.getForegroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();

				objects = this.spatialViewer.getQuery().getObjects(spatialClassName);
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

			vector = this.spatialViewer.getBackgroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();
				objects = this.spatialViewer.getQuery().getObjects(spatialClassName);
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
				ex.printStackTrace();
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
			center = this.spatialViewer.convertLatLongToMap(center);
			System.out.println(" --> " + center.getX() + ", " + center.getY());
			this.spatialViewer.setCenter(center.getX(), center.getY());
		} 
		catch (Exception ex) 
		{
			System.out.println("Cannot center object: ");
			ex.printStackTrace();
			
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
			this.lnl.paint(g);
		}
	}
}

