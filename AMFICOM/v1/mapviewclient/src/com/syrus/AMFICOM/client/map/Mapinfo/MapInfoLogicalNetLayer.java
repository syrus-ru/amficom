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
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(Point2D.Double point)
	{
		return null;
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public Point2D.Double convertScreenToMap(Point point)
	{
		return null;
	}

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(Point2D.Double from, Point2D.Double to)
	{
		return 0D;
	}

	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(Point2D.Double center)
	{
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public Point2D.Double getCenter()
	{
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
	public void repaint()
	{
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		return 1D;
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void setScale(double scale)
	{
	}

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
	}
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
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
