/**
 * $Id: MapNavigateEvent.java,v 1.4 2004/10/15 14:09:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

/**
 * ������� ���������/������ ��������� ��������(-��) ����� 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/15 14:09:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapNavigateEvent extends MapEvent
{
	/**
	 * ������������ ������ ������
	 * @param markerId
	 * @param distance
	 * @param spd
	 */
	public static final long MAP_MARKER_CREATED_EVENT = 0x00000001;
	/**
	 * ������������ ������ ������
	 * @param markerId
	 */
	public static final long MAP_MARKER_DELETED_EVENT = 0x00000002;
	/**
	 * ������������ ���������� ������
	 * @param markerId
	 * @param distance
	 */
	public static final long MAP_MARKER_MOVED_EVENT = 0x00000004;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final long DATA_MARKER_CREATED_EVENT = 0x00000008;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final long DATA_MARKER_DELETED_EVENT = 0x00000010;
	/**
	 * @param markerId
	 * @param distance
	 * ������ ��� ���������� ��� ����� (��������, � ���� �������������)
	 */
	public static final long DATA_MARKER_MOVED_EVENT = 0x00000020;
	/**
	 * ������ ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final long DATA_EVENTMARKER_CREATED_EVENT = 0x00000040;
	/**
	 * ������ ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final long DATA_EVENTMARKER_DELETED_EVENT = 0x00000080;
	/**
	 * ������ ������� ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final long DATA_ALARMMARKER_CREATED_EVENT = 0x00000100;
	/**
	 * ������ ������� ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final long DATA_ALARMMARKER_DELETED_EVENT = 0x00000200;
	/**
	 * ������� �������
	 * @param source
	 */
	public static final long MAP_ELEMENT_SELECTED_EVENT = 0x00000400;
	/**
	 * ����� ��������� ��������
	 * @param source
	 */
	public static final long MAP_ELEMENT_DESELECTED_EVENT = 0x00000800;

	/**
	 * ��� �������
	 */
	protected long mapEventType;

	/**
	 * ������������� �������
	 */	
	protected String markerId;
	
	/**
	 * ������������� ���������� �������
	 */
	protected double distance;

	/**
	 * �������������� �������� ���� 
	 */
    protected String schemePathId;

	/**
	 * �������������� ������������ �������
	 */
    protected String meId;
	
	/**
	 * ������������� ������� �����
	 */
	protected String schemeLinkId;

//    protected Object descriptor;

	/**
	 * ������������ ���� ���������
	 */
	protected Object spd = null;

	public MapNavigateEvent(
			Object source,
			long mapEventType)
	{
		super(source, MAP_NAVIGATE);
		this.mapEventType = mapEventType;
	}
	
	public MapNavigateEvent(
			Object source,
			long mapEventType,
			String markerId,
			double distance,
			String schemePathId,
			String meId)
	{
		super(source, MAP_NAVIGATE);
        this.markerId = markerId;
		this.distance = distance;
        this.schemePathId = schemePathId;
        this.meId = meId;
	}

	public MapNavigateEvent(
			Object source,
			long typ,
			String markerId,
			double distance,
			String schemePathId,
			String meId,
			String schemeLinkId)
	{
		this(source, typ, markerId, distance, schemePathId, meId);
        this.schemeLinkId = schemeLinkId;
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

	public void setMeId(String meId)
	{
		this.meId = meId;
	}

	public String getMeId()
	{
		return meId;
	}

	public void setSchemeLinkId(String schemeLinkId)
	{
		this.schemeLinkId = schemeLinkId;
	}

	public String getSchemeLinkId()
	{
		return schemeLinkId;
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

	public boolean isDataMarkerMoved()
	{
		return mapEventType == DATA_MARKER_MOVED_EVENT;
	}

	public boolean isDataEventMarkerCreated()
	{
		return mapEventType == DATA_EVENTMARKER_CREATED_EVENT;
	}

	public boolean isDataEventMarkerDeleted()
	{
		return mapEventType == DATA_EVENTMARKER_DELETED_EVENT;
	}

	public boolean isDataAlarmMarkerCreated()
	{
		return mapEventType == DATA_ALARMMARKER_CREATED_EVENT;
	}

	public boolean isDataAlarmMarkerDeleted()
	{
		return mapEventType == DATA_ALARMMARKER_DELETED_EVENT;
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
