package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.tools.MapTool;
import com.mapinfo.beans.vmapj.VisualMapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class MapInfoLogicalNetLayer extends LogicalNetLayer 
{
	protected VisualMapJ visualMapJ = null;
	
	MapTool logicalLayerMapTool = null;
	
	public static final double ZOOM_FACTOR = 2D;

	public MapInfoLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"SpatialLogicalNetLayer(" + viewer + ")");

		logicalLayerMapTool = new LogicalLayerMapTool(this);
		setMapViewer(viewer);
	}

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
		try
		{
			com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(point.x, point.y);
			com.mapinfo.util.DoublePoint screendp = visualMapJ.getMapJ().transformNumericToScreen(mapdp);
			return new Point((int )screendp.x, (int )screendp.y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		try
		{
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.DoublePoint(point.x, point.y);
			com.mapinfo.util.DoublePoint mapdp = visualMapJ.getMapJ().transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		return d;
	}
	
	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
	}

	public DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist)
	{
		DoublePoint point = new DoublePoint(startPoint.x, startPoint.y);
		double len = distance(
				startPoint, 
				endPoint);
		point.x += (endPoint.x - startPoint.x) / len * dist;
		point.y += (endPoint.y - startPoint.y) / len * dist;
		return point;
	}

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		try
		{
			return visualMapJ.getMapJ().sphericalDistance(
				new com.mapinfo.util.DoublePoint(from.x, from.y),
				new com.mapinfo.util.DoublePoint(to.x, to.y));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0.0D;
	}

	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(DoublePoint center)
	{
		System.out.println("Set center (" + center.x + ", " +center.y + ")");
		try
		{
			visualMapJ.setOffsets(0, 0);
			visualMapJ.getMapJ().setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			repaint(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
		DoublePoint center;
		try 
		{
			center = new DoublePoint(
				visualMapJ.getMapJ().getCenter().x,
				visualMapJ.getMapJ().getCenter().y);
		} 
		catch (Exception ex) 
		{
			center = null;
			ex.printStackTrace();
		} 
		
		return center;
	}

	public Rectangle2D.Double getVisibleBounds()
	{
		try 
		{
			DoubleRect rect = visualMapJ.getMapJ().getBounds();
			return new Rectangle2D.Double(rect.xmin, rect.ymin, rect.width(), rect.height());
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		return null;
	}

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты
	 */
	public void release()
	{
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
		visualMapJ.repaint(fullRepaint);
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
		System.out.println("Set cursor " + cursor.getName());
		visualMapJ.setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return visualMapJ.getCursor();
	}


	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		try
		{
			return visualMapJ.getMapJ().getZoom();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0.0D;
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void setScale(double scale)
	{
		System.out.println("Set scale " + scale);
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setScale(" + scale + ")");
		try
		{
			if(scale != 0.0D)
				visualMapJ.getMapJ().setZoom(scale);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateZoom();
		repaint(true);
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
		
		try
		{
			visualMapJ.getMapJ().setZoom(getScale() * scaleСoef);
			System.out.println("Set scale " + getScale() * scaleСoef);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateZoom();
		repaint(true);
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
		
		scaleTo(1.0D / ZOOM_FACTOR);
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
				"zoomIn()");
		
		scaleTo(ZOOM_FACTOR);
	}
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		System.out.println("Zoom to box (" + from.x + ", " + from.y + ") - (" + to.x + ", " + to.y + ")");
		try
		{
			visualMapJ.getMapJ().setBounds(new DoubleRect(
					from.x, 
					from.y,
					to.x, 
					to.y));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		updateZoom();
		repaint(true);
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
		visualMapJ.interruptRenderer();
		visualMapJ.setOffsets(
				me.getX() - startPoint.x, 
				me.getY() - startPoint.y);
		repaint(false);
//		spatialViewer.getMapCanvas().repaint();
//		repaint();
	}

	public List findSpatialObjects(String searchText)
	{
		throw new UnsupportedOperationException();
	}

	public void centerSpatialObject(SpatialObject so)
	{
		throw new UnsupportedOperationException();
	}

	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setMapViewer(" + mapViewer + ")");
		
		super.setMapViewer(mapViewer);
		
		MapInfoNetMapViewer snmv = (MapInfoNetMapViewer)mapViewer;
		this.visualMapJ = snmv.visualMapJ;
		System.out.println("Units " + this.visualMapJ.getMapJ().getDistanceUnits().toString());
		this.visualMapJ.getMapJ().setDistanceUnits(LinearUnit.meter);
	}
}
