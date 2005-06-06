/**
 * $Id: MapNavigateEvent.java,v 1.1 2005/06/06 12:45:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.event;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Событие выделения/снятия выделения элемента(-ов) карты
 *
 *
 *
 * @version $Revision: 1.1 $, $Date: 2005/06/06 12:45:30 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public class MapNavigateEvent extends MapEvent
{
	/**
	 * Пользователь создал маркер.
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int MAP_MARKER_CREATED_EVENT = 1;
	/**
	 * Пользователь удалил маркер.
	 * Используются поля {@link #markerId}
	 */
	public static final int MAP_MARKER_DELETED_EVENT = 2;
	/**
	 * Пользователь выделил маркер.
	 * Используются поля {@link #markerId}
	 */
	public static final int MAP_MARKER_SELECTED_EVENT = 3;
	/**
	 * Пользователь передвинул маркер.
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int MAP_MARKER_MOVED_EVENT = 4;
	/**
	 * Маркер был создан вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_MARKER_CREATED_EVENT = 5;
	/**
	 * Маркер события был создан вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_EVENTMARKER_CREATED_EVENT = 6;
	/**
	 * Маркер сигнала тревоги был создан вне карты (например, в окне 
	 * рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_ALARMMARKER_CREATED_EVENT = 7;
	/**
	 * Маркер был выделен вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}
	 */
	public static final int DATA_MARKER_SELECTED_EVENT = 8;
	/**
	 * Маркер был выделен вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}
	 */
	public static final int DATA_MARKER_DESELECTED_EVENT = 9;
	/**
	 * Маркер был удален вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}
	 */
	public static final int DATA_MARKER_DELETED_EVENT = 10;
	/**
	 * Маркер был передвинут вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_MARKER_MOVED_EVENT = 11;
	/**
	 * Элемент выделен.
	 * Используются поля {@link #source}
	 */
	public static final int MAP_ELEMENT_SELECTED_EVENT = 13;
	/**
	 * Снято выбеление элемента.
	 * Используются поля {@link #source}
	 */
	public static final int MAP_ELEMENT_DESELECTED_EVENT = 14;

	/**
	 * Тип события.
	 */
	protected int mapEventType;

	/**
	 * Идентификатор маркера.
	 */
	protected Identifier markerId;

	/**
	 * Относительные координаты маркера.
	 */
	protected double distance;

	/**
	 * Идентификаторы схемного пути.
	 */
	protected Identifier schemePathId;

	/**
	 * Идентификаторы исследуемого объекта.
	 */
	protected Identifier meId;

	/**
	 * Идентификатор схемной Линии.
	 */
	protected Identifier schemePathElementId;

	/**
	 * Декомпозитор пути измерений.
	 */
	protected Object schemePath = null;
	
	public MapNavigateEvent(
			Object source,
			int mapEventType)
	{
		super(source, MAP_NAVIGATE);
		this.mapEventType = mapEventType;
	}

	public MapNavigateEvent(
			Object source,
			int mapEventType,
			Identifier markerId,
			double distance,
			Identifier schemePathId,
			Identifier meId)
	{
		super(source, MAP_NAVIGATE);
		this.markerId = markerId;
		this.distance = distance;
		this.schemePathId = schemePathId;
		this.meId = meId;
	}

	public MapNavigateEvent(
			Object source,
			int typ,
			Identifier markerId,
			double distance,
			Identifier schemePathId,
			Identifier meId,
			Identifier schemePathElementId)
	{
		this(source, typ, markerId, distance, schemePathId, meId);
		this.schemePathElementId = schemePathElementId;
	}

	public void setMarkerId(Identifier markerId)
	{
		this.markerId = markerId;
	}

	public Identifier getMarkerId()
	{
		return this.markerId;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return this.distance;
	}

	public void setSchemePathId(Identifier schemePathId)
	{
		this.schemePathId = schemePathId;
	}

	public com.syrus.AMFICOM.general.Identifier getSchemePathId()
	{
		return this.schemePathId;
	}

	public void setMeId(Identifier meId)
	{
		this.meId = meId;
	}

	public Identifier getMeId()
	{
		return this.meId;
	}

	public void setSchemePathElementId(Identifier schemePathElementId)
	{
		this.schemePathElementId = schemePathElementId;
	}

	public com.syrus.AMFICOM.general.Identifier getSchemePathElementId()
	{
		return this.schemePathElementId;
	}
/*
	public void setDescriptor(Object descriptor)
	{
		this.descriptor = descriptor;
	}

	public Object getDescriptor()
	{
		return descriptor;
	}
*/
	public void setSchemePath(final Object schemePath)
	{
		this.schemePath = schemePath;
	}

	public Object getSchemePath()
	{
		return this.schemePath;
	}

	public boolean isMapMarkerCreated()
	{
		return this.mapEventType == MAP_MARKER_CREATED_EVENT;
	}

	public boolean isMapMarkerDeleted()
	{
		return this.mapEventType == MAP_MARKER_DELETED_EVENT;
	}

	public boolean isMapMarkerSelected()
	{
		return this.mapEventType == MAP_MARKER_SELECTED_EVENT;
	}

	public boolean isMapMarkerMoved()
	{
		return this.mapEventType == MAP_MARKER_MOVED_EVENT;
	}

	public boolean isDataMarkerCreated()
	{
		return this.mapEventType == DATA_MARKER_CREATED_EVENT;
	}

	public boolean isDataMarkerDeleted()
	{
		return this.mapEventType == DATA_MARKER_DELETED_EVENT;
	}

	public boolean isDataMarkerSelected()
	{
		return this.mapEventType == DATA_MARKER_SELECTED_EVENT;
	}

	public boolean isDataMarkerDeselected()
	{
		return this.mapEventType == DATA_MARKER_DESELECTED_EVENT;
	}

	public boolean isDataMarkerMoved()
	{
		return this.mapEventType == DATA_MARKER_MOVED_EVENT;
	}

	public boolean isDataEventMarkerCreated()
	{
		return this.mapEventType == DATA_EVENTMARKER_CREATED_EVENT;
	}

	public boolean isDataAlarmMarkerCreated()
	{
		return this.mapEventType == DATA_ALARMMARKER_CREATED_EVENT;
	}

	public boolean isMapElementSelected()
	{
		return this.mapEventType == MAP_ELEMENT_SELECTED_EVENT;
	}

	public boolean isMapElementDeselected()
	{
		return this.mapEventType == MAP_ELEMENT_DESELECTED_EVENT;
	}
}
