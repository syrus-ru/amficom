/**
 * $Id: SpatialObject.java,v 1.2 2005/01/21 16:19:57 krupenn Exp $
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
 * графических объектов 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.Client.Map.Setup.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * возвращает имя географического объекта
	 */
	String getLabel();
}
