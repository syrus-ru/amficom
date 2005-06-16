/**
 * $Id: MapContext.java,v 1.2 2005/06/16 10:57:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import com.syrus.AMFICOM.map.DoublePoint;

public interface MapContext {

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
	 * Установить масштаб вида карты с заданным коэффициентом.
	 * @param scaleCoef коэффициент масштабирования
	 */
	public abstract void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException;

	/**
	 * Приблизить вид карты со стандартным коэффициентом.
	 */
	public abstract void zoomIn()
			throws MapConnectionException, MapDataException;

	/**
	 * Отдалить вид карты со стандартным коэффициентом.
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


	/**
	 * Получить список географических слоев.
	 * @return список слоев &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List getLayers()
		throws MapDataException;
	
}
