/**
 * $Id: MapCoordinatesConverter.java,v 1.2 2004/10/15 14:09:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Конвертер географических и экранных координат для отображения элементов карты 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/15 14:09:00 $
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
	Point convertMapToScreen(Point2D.Double point);
	
	/**
	 * перевести экранные координаты в географические
	 * @param point
	 * @return 
	 */
	Point2D.Double convertScreenToMap(Point point);
	
	/**
	 * найти расстояние между двумя точками в географической системе координат
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(Point2D.Double from, Point2D.Double to);
}
