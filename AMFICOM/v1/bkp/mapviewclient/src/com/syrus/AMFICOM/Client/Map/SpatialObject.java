/**
 * $Id: SpatialObject.java,v 1.4 2005/03/02 12:37:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

/**
 * Географический объект. Реализация принадлежит используемой системе ГИС.
 * текущее использование класса - в списке найденных по строковому шаблону
 * графических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/03/02 12:37:45 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Operations.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * Возвращает имя географического объекта.
	 * @return имя объекта
	 */
	String getLabel();
}
