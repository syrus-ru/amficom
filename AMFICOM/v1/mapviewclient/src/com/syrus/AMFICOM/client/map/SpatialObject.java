/**
 * $Id: SpatialObject.java,v 1.8 2005/08/29 12:17:46 peskovsky Exp $
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
 * @author $Author: peskovsky $
 * @version $Revision: 1.8 $, $Date: 2005/08/29 12:17:46 $
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.operations.SpatialSearchPanel
 */
public abstract class SpatialObject implements Comparable 
{
	protected String label = null;
	
	/**
	 * Возвращает имя географического объекта.
	 * @return имя объекта
	 */
	public String getLabel() {
		return this.label;
	}
	
	public int compareTo(Object o) {
		if (!(o instanceof SpatialObject))
			throw new ClassCastException();
		
		SpatialObject spatialObject = (SpatialObject)o;
		return (this.label.compareTo(spatialObject.label));
	}
}
