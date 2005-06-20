/**
 * $Id: MapContext.java,v 1.5 2005/06/20 10:09:13 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import com.syrus.AMFICOM.map.DoublePoint;

public interface MapContext {
	public static final double ZOOM_FACTOR = 2D;

	public abstract MapConnection getMapConnection()
		throws MapConnectionException;

	/**
	 * Установить центральную точку вида карты.
	 * @param center географическая координата центра
	 */
	public abstract void setCenter(DoublePoint center)
			throws MapConnectionException, MapDataException;

	/**
	 * Получить центральную точку вида карты.
	 * @return географическая координата центра
	 */
	public abstract DoublePoint getCenter()
			throws MapConnectionException, MapDataException;

	/**
	 * Получить текущий масштаб вида карты.
	 * @return масштаб
	 */
	public abstract double getScale()
			throws MapConnectionException, MapDataException;

	/**
	 * Установить заданный масштаб вида карты.
	 * @param scale масштаб
	 */
	public abstract void setScale(double scale)
			throws MapConnectionException, MapDataException;

	/**
	 * Изменить масштаб вида карты в заданное число раз.
	 * @param scaleCoef коэффициент масштабирования
	 */
	public abstract void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException;

	/**
	 * Приблизить вид карты в стандартное число раз.
	 */
	public abstract void zoomIn()
			throws MapConnectionException, MapDataException;

	/**
	 * Отдалить вид карты в стандартное число раз.
	 */
	public abstract void zoomOut()
			throws MapConnectionException, MapDataException;

	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек.
	 * @param from географическая координата
	 * @param to географическая координата
	 */
	public abstract void zoomToBox(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException;

}
