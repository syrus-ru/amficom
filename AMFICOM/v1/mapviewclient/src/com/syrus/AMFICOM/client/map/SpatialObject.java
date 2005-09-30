/*-
 * $$Id: SpatialObject.java,v 1.12 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

/**
 * Географический объект. Реализация принадлежит используемой системе ГИС.
 * текущее использование класса - в списке найденных по строковому шаблону
 * графических объектов.
 * 
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
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
