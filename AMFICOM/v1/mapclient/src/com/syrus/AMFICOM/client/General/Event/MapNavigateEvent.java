/**
 * $Id: MapNavigateEvent.java,v 1.1 2004/09/13 12:00:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Событие выделения/снятия выделения элемента(-ов) карты 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:00:25 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapNavigateEvent extends MapEvent
{
	public boolean MAP_MARKER_CREATED = false;
	public boolean MAP_MARKER_DELETED = false;
	public boolean MAP_MARKER_MOVED = false;
	public boolean MAP_MARKER_SELECTED = false;
	public boolean MAP_MARKER_DESELECTED = false;

	public boolean DATA_MARKER_CREATED = false;
	public boolean DATA_MARKER_DELETED = false;
	public boolean DATA_MARKER_MOVED = false;
	public boolean DATA_MARKER_SELECTED = false;
	public boolean DATA_MARKER_DESELECTED = false;

	public boolean DATA_EVENTMARKER_CREATED = false;
	public boolean DATA_EVENTMARKER_DELETED = false;

	public boolean DATA_ALARMMARKER_CREATED = false;
	public boolean DATA_ALARMMARKER_DELETED = false;

	public boolean MAP_PATH_SELECTED = false;
	public boolean MAP_ELEMENT_SELECTED = false;

	public boolean MAP_PATH_DESELECTED = false;
	public boolean MAP_ELEMENT_DESELECTED = false;

	public boolean MAP_PROPERTY_SELECTED = false;

	public static final long MAP_MARKER_CREATED_EVENT = 0x00000001;
	public static final long MAP_MARKER_DELETED_EVENT = 0x00000004;
	public static final long MAP_MARKER_MOVED_EVENT = 0x00000002;
	public static final long MAP_MARKER_SELECTED_EVENT = 0x00000008;
    public static final long MAP_MARKER_DESELECTED_EVENT = 0x00000010;

	public static final long DATA_MARKER_CREATED_EVENT = 0x00000040;
	public static final long DATA_MARKER_DELETED_EVENT = 0x00000080;
	public static final long DATA_MARKER_MOVED_EVENT = 0x00000020;
	public static final long DATA_MARKER_SELECTED_EVENT = 0x00000100;
	public static final long DATA_MARKER_DESELECTED_EVENT = 0x00000200;

	public static final long DATA_EVENTMARKER_CREATED_EVENT = 0x00000041;
	public static final long DATA_EVENTMARKER_DELETED_EVENT = 0x00000042;

	public static final long DATA_ALARMMARKER_CREATED_EVENT = 0x00000043;
	public static final long DATA_ALARMMARKER_DELETED_EVENT = 0x00000044;

	public static final long MAP_PATH_SELECTED_EVENT = 0x00008000;
	public static final long MAP_ELEMENT_SELECTED_EVENT = 0x00000014;

	public static final long MAP_PATH_DESELECTED_EVENT = 0x00200000;
	public static final long MAP_ELEMENT_DESELECTED_EVENT = 0x00000015;

	public static final long MAP_PROPERTY_SELECTED_EVENT = 0x00000013;

	protected String markerId;
	protected double distance;

    protected String mapPathId;
    protected String meId;
    protected String linkId;

    protected Object descriptor;

	protected Object spd = null;

	public MapNavigateEvent(
			Object source,
			long typ)
	{
		super(source, MAP_NAVIGATE);
		setTyp(typ);
	}
	
	public MapNavigateEvent(
			Object source,
			long typ,
			String markerId,
			double distance,
			String mapPathId,
			String meId)
	{
		super(source, MAP_NAVIGATE);
        this.markerId = markerId;
		this.distance = distance;
        this.mapPathId = mapPathId;
        this.meId = meId;

		setTyp(typ);
	}

	public MapNavigateEvent(
			Object source,
			long typ,
			String markerId,
			double distance,
			String schemePathId,
			String meId,
			String linkId)
	{
		this(source, typ, markerId, distance, schemePathId, meId);
        this.linkId = linkId;
	}

	private void setTyp(long typ)
	{
		if((typ == MAP_MARKER_CREATED_EVENT))
			MAP_MARKER_CREATED = true;
		if((typ == MAP_MARKER_DELETED_EVENT))
			MAP_MARKER_DELETED = true;
		if((typ == MAP_MARKER_MOVED_EVENT))
			MAP_MARKER_MOVED = true;
		if((typ == MAP_MARKER_SELECTED_EVENT))
			MAP_MARKER_SELECTED = true;
		if((typ == MAP_MARKER_DESELECTED_EVENT))
			MAP_MARKER_DESELECTED = true;

		if((typ == DATA_MARKER_CREATED_EVENT))
			DATA_MARKER_CREATED = true;
		if((typ == DATA_MARKER_DELETED_EVENT))
			DATA_MARKER_DELETED = true;
		if((typ == DATA_MARKER_MOVED_EVENT))
			DATA_MARKER_MOVED = true;
		if((typ == DATA_MARKER_SELECTED_EVENT))
			DATA_MARKER_SELECTED = true;
		if((typ == DATA_MARKER_DESELECTED_EVENT))
			DATA_MARKER_DESELECTED = true;

		if((typ == DATA_EVENTMARKER_CREATED_EVENT))
			DATA_EVENTMARKER_CREATED = true;
		if((typ == DATA_EVENTMARKER_DELETED_EVENT))
			DATA_EVENTMARKER_DELETED = true;

		if((typ == DATA_ALARMMARKER_CREATED_EVENT))
			DATA_ALARMMARKER_CREATED = true;
		if((typ == DATA_ALARMMARKER_DELETED_EVENT))
			DATA_ALARMMARKER_DELETED = true;

		if((typ == MAP_PATH_SELECTED_EVENT))
			MAP_PATH_SELECTED = true;
		if((typ == MAP_ELEMENT_SELECTED_EVENT))
			MAP_ELEMENT_SELECTED = true;
			
		if((typ == MAP_PATH_DESELECTED_EVENT))
			MAP_PATH_DESELECTED = true;
		if((typ == MAP_ELEMENT_DESELECTED_EVENT))
			MAP_ELEMENT_DESELECTED = true;

		if((typ == MAP_PROPERTY_SELECTED_EVENT))
			MAP_PROPERTY_SELECTED = true;
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


	public void setMapPathId(String mapPathId)
	{
		this.mapPathId = mapPathId;
	}


	public String getMapPathId()
	{
		return mapPathId;
	}


	public void setMeId(String meId)
	{
		this.meId = meId;
	}


	public String getMeId()
	{
		return meId;
	}


	public void setLinkId(String linkId)
	{
		this.linkId = linkId;
	}


	public String getLinkId()
	{
		return linkId;
	}


	public void setDescriptor(Object descriptor)
	{
		this.descriptor = descriptor;
	}


	public Object getDescriptor()
	{
		return descriptor;
	}


	public void setSchemePathDecompositor(Object spd)
	{
		this.spd = spd;
	}


	public Object getSchemePathDecompositor()
	{
		return spd;
	}

}
