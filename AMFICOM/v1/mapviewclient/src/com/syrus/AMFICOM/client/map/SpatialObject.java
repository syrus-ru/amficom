/**
 * $Id: SpatialObject.java,v 1.7 2005/08/11 12:43:29 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
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
