/*-
 * $$Id: MapCoordinatesConverter.java,v 1.13 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Point;

import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Конвертер географических и экранных координат для отображения элементов карты
 * 
 * @version $Revision: 1.13 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public interface MapCoordinatesConverter {
	public abstract MapConnection getMapConnection()
			throws MapConnectionException;

	/**
	 * Перевести географические координаты в экранные.
	 * 
	 * @param point географические координаты
	 * @return экранные координаты
	 */
	Point convertMapToScreen(DoublePoint point)
			throws MapConnectionException, MapDataException;

	/**
	 * Перевести экранные координаты в географические.
	 * 
	 * @param point экранные координаты
	 * @return географические координаты
	 */
	DoublePoint convertScreenToMap(Point point)
			throws MapConnectionException, MapDataException;

	/**
	 * Получить точку на линии (startPoint, endPoint), отлежащий от точки
	 * startPoint на дистанцию dist.
	 * 
	 * @param startPoint географические координаты начальной точки
	 * @param endPoint географические координаты конечной точки
	 * @param dist географическая дистанция
	 * @return географические координаты полученной точки
	 */
	DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist) throws MapConnectionException, MapDataException;

	/**
	 * Найти расстояние между двумя точками в географической системе координат.
	 * 
	 * @param from географические координаты начальной точки
	 * @param to географические координаты конечной точки
	 * @return расстояние между точками
	 */
	double distance(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException;

}
