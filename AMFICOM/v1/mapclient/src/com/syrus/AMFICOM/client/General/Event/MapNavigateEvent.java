/**
 * $Id: MapNavigateEvent.java,v 1.7 2004/10/26 13:25:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;
import com.syrus.AMFICOM.general.Identifier;

/**
 * Событие выделения/снятия выделения элемента(-ов) карты 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/10/26 13:25:36 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapNavigateEvent extends MapEvent
{
	/**
	 * Пользователь создал маркер
	 * @param markerId
	 * @param distance
	 * @param spd
	 */
	public static final int MAP_MARKER_CREATED_EVENT = 1;
	/**
	 * Пользователь удалил маркер
	 * @param markerId
	 */
	public static final int MAP_MARKER_DELETED_EVENT = 2;
	/**
	 * Пользователь выделил маркер
	 * @param markerId
	 */
	public static final int MAP_MARKER_SELECTED_EVENT = 3;
	/**
	 * Пользователь передвинул маркер
	 * @param markerId
	 * @param distance
	 */
	public static final int MAP_MARKER_MOVED_EVENT = 4;
	/**
	 * Маркер был создан вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_MARKER_CREATED_EVENT = 5;
	/**
	 * Маркер события был создан вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_EVENTMARKER_CREATED_EVENT = 6;
	/**
	 * Маркер сигнала тревоги был создан вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_ALARMMARKER_CREATED_EVENT = 7;
	/**
	 * Маркер был выделен вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 */
	public static final int DATA_MARKER_SELECTED_EVENT = 8;
	/**
	 * Маркер был выделен вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 */
	public static final int DATA_MARKER_DESELECTED_EVENT = 9;
	/**
	 * Маркер был удален вне карты (например, в окне рефлектограмм)
	 * @param markerId
	 */
	public static final int DATA_MARKER_DELETED_EVENT = 10;
	/**
	 * @param markerId
	 * @param distance
	 * Маркер был передвинут вне карты (например, в окне рефлектограмм)
	 */
	public static final int DATA_MARKER_MOVED_EVENT = 11;
	/**
	 * Элемент выделен
	 * @param source
	 */
	public static final int MAP_ELEMENT_SELECTED_EVENT = 13;
	/**
	 * Снято выбеление элемента
	 * @param source
	 */
	public static final int MAP_ELEMENT_DESELECTED_EVENT = 14;

	/**
	 * Тип события
	 */
	protected int mapEventType;

	/**
	 * Идентификатор маркера
	 */	
	protected String markerId;
	
	/**
	 * Относительные координаты маркера
	 */
	protected double distance;

	/**
	 * Идентификаторы схемного пути 
	 */
    protected String schemePathId;

	/**
	 * Идентификаторы исследуемого объекта
	 */
    protected Identifier meId;
	
	/**
	 * Идентификатор схемной Линии
	 */
	protected String schemePathElementId;

//    protected Object descriptor;

	/**
	 * Декомпозитор пути измерений
	 */
	protected Object spd = null;

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
			String markerId,
			double distance,
			String schemePathId,
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
			String markerId,
			double distance,
			String schemePathId,
			Identifier meId,
			String schemePathElementId)
	{
		this(source, typ, markerId, distance, schemePathId, meId);
        this.schemePathElementId = schemePathElementId;
	}

	public void setMarkerId(String markerId)
	{
		this.markerId = markerId;
	}

	public String getMarkerId()
	{
		return markerId;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setSchemePathId(String schemePathId)
	{
		this.schemePathId = schemePathId;
	}

	public String getSchemePathId()
	{
		return schemePathId;
	}

	public void setMeId(Identifier meId)
	{
		this.meId = meId;
	}

	public Identifier getMeId()
	{
		return meId;
	}

	public void setSchemePathElementId(String schemePathElementId)
	{
		this.schemePathElementId = schemePathElementId;
	}

	public String getSchemePathElementId()
	{
		return schemePathElementId;
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
	public void setSchemePathDecompositor(Object spd)
	{
		this.spd = spd;
	}

	public Object getSchemePathDecompositor()
	{
		return spd;
	}

	public boolean isMapMarkerCreated()
	{
		return mapEventType == MAP_MARKER_CREATED_EVENT;
	}

	public boolean isMapMarkerDeleted()
	{
		return mapEventType == MAP_MARKER_DELETED_EVENT;
	}

	public boolean isMapMarkerSelected()
	{
		return mapEventType == MAP_MARKER_SELECTED_EVENT;
	}

	public boolean isMapMarkerMoved()
	{
		return mapEventType == MAP_MARKER_MOVED_EVENT;
	}

	public boolean isDataMarkerCreated()
	{
		return mapEventType == DATA_MARKER_CREATED_EVENT;
	}

	public boolean isDataMarkerDeleted()
	{
		return mapEventType == DATA_MARKER_DELETED_EVENT;
	}

	public boolean isDataMarkerSelected()
	{
		return mapEventType == DATA_MARKER_SELECTED_EVENT;
	}

	public boolean isDataMarkerDeselected()
	{
		return mapEventType == DATA_MARKER_DESELECTED_EVENT;
	}

	public boolean isDataMarkerMoved()
	{
		return mapEventType == DATA_MARKER_MOVED_EVENT;
	}

	public boolean isDataEventMarkerCreated()
	{
		return mapEventType == DATA_EVENTMARKER_CREATED_EVENT;
	}

	public boolean isDataAlarmMarkerCreated()
	{
		return mapEventType == DATA_ALARMMARKER_CREATED_EVENT;
	}

	public boolean isMapElementSelected()
	{
		return mapEventType == MAP_ELEMENT_SELECTED_EVENT;
	}

	public boolean isMapElementDeselected()
	{
		return mapEventType == MAP_ELEMENT_DESELECTED_EVENT;
	}

}
