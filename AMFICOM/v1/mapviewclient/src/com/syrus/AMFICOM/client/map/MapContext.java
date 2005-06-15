/**
 * $Id: MapContext.java,v 1.1 2005/06/15 07:42:28 krupenn Exp $
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
	 * @deprecated use netMapViewer
	 */
	public abstract void setCenter(DoublePoint center)
			throws MapConnectionException, MapDataException;

	/**
	 * Получить центральную точку вида карты.
	 * @return географическая координата центра
	 * @deprecated use netMapViewer
	 */
	public abstract DoublePoint getCenter()
			throws MapConnectionException, MapDataException;

	/**
	 * Получить текущий масштаб вида карты.
	 * @return масштаб
	 * @deprecated use netMapViewer
	 */
	public abstract double getScale()
			throws MapConnectionException, MapDataException;

	/**
	 * Установить заданный масштаб вида карты.
	 * @param scale масштаб
	 * @deprecated use netMapViewer
	 */
	public abstract void setScale(double scale)
			throws MapConnectionException, MapDataException;

	/**
	 * Установить масштаб вида карты с заданным коэффициентом.
	 * @param scaleСoef коэффициент масштабирования
	 * @deprecated use netMapViewer
	 */
	public abstract void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException;

	/**
	 * Приблизить вид карты со стандартным коэффициентом.
	 * @deprecated use netMapViewer
	 */
	public abstract void zoomIn()
			throws MapConnectionException, MapDataException;

	/**
	 * Отдалить вид карты со стандартным коэффициентом.
	 * @deprecated use netMapViewer
	 */
	public abstract void zoomOut()
			throws MapConnectionException, MapDataException;

	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек.
	 * @param from географическая координата
	 * @param to географическая координата
	 * @deprecated use netMapViewer
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
