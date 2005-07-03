/**
 * $Id: SpatialObject.java,v 1.6 2005/06/06 12:57:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map;

/**
 * Географический объект. Реализация принадлежит используемой системе ГИС.
 * текущее использование класса - в списке найденных по строковому шаблону
 * графических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:57:01 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.client.map.operations.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * Возвращает имя географического объекта.
	 * @return имя объекта
	 */
	String getLabel();
}
