/**
 * $Id: MapCoordinatesConverter.java,v 1.4 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.map.DoublePoint;

import java.awt.Point;

/**
 * Конвертер географических и экранных координат для отображения элементов карты 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/10 11:48:39 $
 * @module maviewclient_v1
 * @todo в панелях свойств используется приведение конвертера к типу 
 * LogicalNetLayer, поскольку в данной реализации модуля именно он 
 * имплементит MapCoordinatesConverter - необходимо найти обходной путь
 */
public interface MapCoordinatesConverter 
{
	/**
	 * Перевести географические координаты в экранные.
	 * @param point географические координаты
	 * @return экранные координаты
	 */
	Point convertMapToScreen(DoublePoint point);
	
	/**
	 * Перевести экранные координаты в географические.
	 * @param point экранные координаты
	 * @return географические координаты
	 */
	DoublePoint convertScreenToMap(Point point);

	/**
	 * Перевести экранную дистанцию в географическую.
	 * @param screenDistance экранная дистанция 
	 * @return географическая дистанция
	 */
	double convertScreenToMap(double screenDistance);
	
	/**
	 * Перевести географическую дистанцию в экранную.
	 * @param topologicalDistance географическая дистанция
	 * @return экранная дистанция
	 */
	double convertMapToScreen(double topologicalDistance);

	/**
	 * Получить точку на линии (startPoint, endPoint), отлежащий от
	 * точки startPoint на дистанцию dist.
	 * @param startPoint географические координаты начальной точки
	 * @param endPoint географические координаты конечной точки
	 * @param dist географическая дистанция
	 * @return географические координаты полученной точки
	 */
	DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist);

	/**
	 * Найти расстояние между двумя точками в географической системе координат.
	 * @param from географические координаты начальной точки
	 * @param to географические координаты конечной точки
	 * @return расстояние между точками
	 */
	double distance(DoublePoint from, DoublePoint to);
}
