/**
 * $Id: MapNavigateEvent.java,v 1.3 2004/10/09 13:34:33 krupenn Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/10/09 13:34:33 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapNavigateEvent extends MapEvent
{
	protected boolean mapMarkerCreated = false;
	protected boolean mapMarkerDeleted = false;
	protected boolean mapMarkerMoved = false;
	protected boolean mapMarkerSelected = false;
	protected boolean mapMarkerDeselected = false;

	protected boolean dataMarkerCreated = false;
	protected boolean dataMarkerDeleted = false;
	protected boolean dataMarkerMoved = false;
	protected boolean dataMarkerSelected = false;
	protected boolean dataMarkerDeselected = false;

	protected boolean dataEventMarkerCreated = false;
	protected boolean dataEventMarkerDeleted = false;

	protected boolean dataAlarmMarkerCreated = false;
	protected boolean dataAlarmMarkerDeleted = false;

	protected boolean mapPathSelected = false;
	protected boolean mapElementSelected = false;

	protected boolean mapPathDeselected = false;
	protected boolean mapElementDeselected = false;

	protected boolean mapPropertySelected = false;

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
		this.setTyp(typ);
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

		this.setTyp(typ);
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
			mapMarkerCreated = true;
		if((typ == MAP_MARKER_DELETED_EVENT))
			mapMarkerDeleted = true;
		if((typ == MAP_MARKER_MOVED_EVENT))
			mapMarkerMoved = true;
		if((typ == MAP_MARKER_SELECTED_EVENT))
			mapMarkerSelected = true;
		if((typ == MAP_MARKER_DESELECTED_EVENT))
			mapMarkerDeselected = true;

		if((typ == DATA_MARKER_CREATED_EVENT))
			dataMarkerCreated = true;
		if((typ == DATA_MARKER_DELETED_EVENT))
			dataMarkerDeleted = true;
		if((typ == DATA_MARKER_MOVED_EVENT))
			dataMarkerMoved = true;
		if((typ == DATA_MARKER_SELECTED_EVENT))
			dataMarkerSelected = true;
		if((typ == DATA_MARKER_DESELECTED_EVENT))
			dataMarkerDeselected = true;

		if((typ == DATA_EVENTMARKER_CREATED_EVENT))
			dataEventMarkerCreated = true;
		if((typ == DATA_EVENTMARKER_DELETED_EVENT))
			dataEventMarkerDeleted = true;

		if((typ == DATA_ALARMMARKER_CREATED_EVENT))
			dataAlarmMarkerCreated = true;
		if((typ == DATA_ALARMMARKER_DELETED_EVENT))
			dataAlarmMarkerDeleted = true;

		if((typ == MAP_PATH_SELECTED_EVENT))
			mapPathSelected = true;
		if((typ == MAP_ELEMENT_SELECTED_EVENT))
			mapElementSelected = true;
			
		if((typ == MAP_PATH_DESELECTED_EVENT))
			mapPathDeselected = true;
		if((typ == MAP_ELEMENT_DESELECTED_EVENT))
			mapElementDeselected = true;

		if((typ == MAP_PROPERTY_SELECTED_EVENT))
			mapPropertySelected = true;
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


	public boolean isMapMarkerCreated()
	{
		return mapMarkerCreated;
	}


	public boolean isMapMarkerDeleted()
	{
		return mapMarkerDeleted;
	}


	public boolean isMapMarkerMoved()
	{
		return mapMarkerMoved;
	}


	public boolean isMapMarkerSelected()
	{
		return mapMarkerSelected;
	}


	public boolean isMapMarkerDeselected()
	{
		return mapMarkerDeselected;
	}


	public boolean isDataMarkerCreated()
	{
		return dataMarkerCreated;
	}


	public boolean isDataMarkerDeleted()
	{
		return dataMarkerDeleted;
	}


	public boolean isDataMarkerMoved()
	{
		return dataMarkerMoved;
	}


	public boolean isDataMarkerSelected()
	{
		return dataMarkerSelected;
	}


	public boolean isDataMarkerDeselected()
	{
		return dataMarkerDeselected;
	}


	public boolean isDataEventMarkerCreated()
	{
		return dataEventMarkerCreated;
	}


	public boolean isDataEventMarkerDeleted()
	{
		return dataEventMarkerDeleted;
	}


	public boolean isDataAlarmMarkerCreated()
	{
		return dataAlarmMarkerCreated;
	}


	public boolean isDataAlarmMarkerDeleted()
	{
		return dataAlarmMarkerDeleted;
	}


	public boolean isMapPathSelected()
	{
		return mapPathSelected;
	}


	public boolean isMapElementSelected()
	{
		return mapElementSelected;
	}


	public boolean isMapPathDeselected()
	{
		return mapPathDeselected;
	}


	public boolean isMapElementDeselected()
	{
		return mapElementDeselected;
	}


	public boolean isMapPropertySelected()
	{
		return mapPropertySelected;
	}

}
