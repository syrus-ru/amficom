/**
 * $Id: SpatialObject.java,v 1.10 2005/08/29 16:09:38 arseniy Exp $
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
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/08/29 16:09:38 $
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.operations.SpatialSearchPanel
 */
public abstract class SpatialObject implements Comparable<SpatialObject> {
	protected String label;

	public SpatialObject(String label) {
		this.label = label;
	}

	/**
	 * Возвращает имя географического объекта.
	 * 
	 * @return имя объекта
	 */
	public String getLabel() {
		return this.label;
	}

	public int compareTo(SpatialObject spatialObject) {
		return (this.label.compareTo(spatialObject.label));
	}
}
