/**
 * $Id: MapCoordinatesConverter.java,v 1.6 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Конвертер географических и экранных координат для отображения элементов карты 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @todo в панелях свойств используется приведение конвертера к типу 
 * LogicalNetLayer, поскольку в данной реализации модуля именно он 
 * имплементит MapCoordinatesConverter - необходимо найти обходной путь
 */
public interface MapCoordinatesConverter 
{
	/**
	 * Перевести географические координаты в экранные
	 * @param point
	 * @return 
	 */
	Point convertMapToScreen(DoublePoint point);
	
	/**
	 * перевести экранные координаты в географические
	 * @param point
	 * @return 
	 */
	DoublePoint convertScreenToMap(Point point);

	/**
	 * перевести экранную дистанцию в географическую
	 * @param screenDistance
	 * @return 
	 */
	double convertScreenToMap(double screenDistance);
	
	/**
	 * перевести географическую дистанцию в экранную
	 * @param topologicalDistance
	 * @return 
	 */
	double convertMapToScreen(double topologicalDistance);

	/**
	 * получить точку на линии (startPoint, endPoint), отлежащий от
	 * точки startPoint на дистанцию dist
	 * @param startPoint
	 * @param endPoint
	 * @param dist
	 * @return 
	 */
	DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist);

	/**
	 * найти расстояние между двумя точками в географической системе координат
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(DoublePoint from, DoublePoint to);
}
