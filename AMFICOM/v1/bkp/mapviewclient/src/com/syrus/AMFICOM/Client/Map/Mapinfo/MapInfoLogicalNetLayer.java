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
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(Point2D.Double point)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Получить географические координаты по экранным
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
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(Point2D.Double from, Point2D.Double to)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(Point2D.Double center)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить центральную точку вида карты
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
	 * Освободить ресурсы компонента с картой и завершить отображение карты
	 */
	public void release()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void setScale(double scale)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
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
