/**
 * $Id: MapCoordinatesConverter.java,v 1.9 2005/06/16 14:39:05 krupenn Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.map.DoublePoint;

import java.awt.Point;

/**
 * Конвертер географических и экранных координат для отображения элементов карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/06/16 14:39:05 $
 * @module maviewclient_v1
 */
public interface MapCoordinatesConverter
{
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
			double dist) 
		throws MapConnectionException, MapDataException;

	/**
	 * Найти расстояние между двумя точками в географической системе координат.
	 * 
	 * @param from географические координаты начальной точки
	 * @param to географические координаты конечной точки
	 * @return расстояние между точками
	 */
	double distance(DoublePoint from, DoublePoint to)
		throws MapConnectionException, MapDataException;

	/**
	 * Перевести экранную дистанцию в географическую.
	 * 
	 * @param screenDistance экранная дистанция
	 * @return географическая дистанция
	 */
//	double convertScreenToMap(double screenDistance)
//		throws MapConnectionException, MapDataException;

	/**
	 * Перевести географическую дистанцию в экранную.
	 * 
	 * @param topologicalDistance географическая дистанция
	 * @return экранная дистанция
	 */
//	double convertMapToScreen(double topologicalDistance)
//		throws MapConnectionException, MapDataException;

}
