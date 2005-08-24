/**
 * $Id: MapNavigateEvent.java,v 1.6 2005/08/24 08:18:23 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.event;

import com.syrus.AMFICOM.general.Identifier;

/**
 * ������� ���������/������ ��������� ��������(-��) �����
 *
 *
 *
 * @version $Revision: 1.6 $, $Date: 2005/08/24 08:18:23 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public class MapNavigateEvent extends MapEvent {
	private static final long serialVersionUID = 7592135696104602801L;

	/**
	 * ������������ ������ ������.
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int MAP_MARKER_CREATED_EVENT = 1;
	/**
	 * ������������ ������ ������.
	 * ������������ ���� {@link #markerId}
	 */
	public static final int MAP_MARKER_DELETED_EVENT = 2;
	/**
	 * ������������ ������� ������.
	 * ������������ ���� {@link #markerId}
	 */
	public static final int MAP_MARKER_SELECTED_EVENT = 3;
	/**
	 * ������������ ���������� ������.
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int MAP_MARKER_MOVED_EVENT = 4;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_MARKER_CREATED_EVENT = 5;
	/**
	 * ������ ������� ��� ������ ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_EVENTMARKER_CREATED_EVENT = 6;
	/**
	 * ������ ������� ������� ��� ������ ��� ����� (��������, � ���� 
	 * �������������).
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_ALARMMARKER_CREATED_EVENT = 7;
	/**
	 * ������ ��� ������� ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}
	 */
	public static final int DATA_MARKER_SELECTED_EVENT = 8;
	/**
	 * ������ ��� ������� ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}
	 */
	public static final int DATA_MARKER_DESELECTED_EVENT = 9;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}
	 */
	public static final int DATA_MARKER_DELETED_EVENT = 10;
	/**
	 * ������ ��� ���������� ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}, {@link #distance}
	 */
	public static final int DATA_MARKER_MOVED_EVENT = 11;

	/**
	 * ��� �������.
	 */
	protected int mapNavigateEventType;

	/**
	 * ������������� �������.
	 */
	protected Identifier markerId;

	/**
	 * ������������� ���������� �������.
	 */
	protected double distance;

	/**
	 * �������������� �������� ����.
	 */
	protected Identifier schemePathId;

	/**
	 * �������������� ������������ �������.
	 */
	protected Identifier meId;

	/**
	 * ������������� ������� �����.
	 */
	protected Identifier schemePathElementId;

	/**
	 * ������������ ���� ���������.
	 */
	protected Object schemePath = null;

	public MapNavigateEvent(
			Object source,
			int mapNavigateEventType)
	{
		super(source, MAP_NAVIGATE);
		this.mapNavigateEventType = mapNavigateEventType;
	}

	public MapNavigateEvent(
			Object source,
			int mapNavigateEventType,
			Object newValue)
	{
		super(source, MAP_NAVIGATE, newValue);
		this.mapNavigateEventType = mapNavigateEventType;
	}

	public MapNavigateEvent(
			Object source,
			int mapNavigateEventType,
			Identifier markerId,
			double distance,
			Identifier schemePathId,
			Identifier meId)
	{
		this(source, mapNavigateEventType);
		this.markerId = markerId;
		this.distance = distance;
		this.schemePathId = schemePathId;
		this.meId = meId;
	}

	public MapNavigateEvent(
			Object source,
			int mapNavigateEventType,
			Identifier markerId,
			double distance,
			Identifier schemePathId,
			Identifier meId,
			Identifier schemePathElementId)
	{
		this(source, mapNavigateEventType, markerId, distance, schemePathId, meId);
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
		return this.mapNavigateEventType == MAP_MARKER_CREATED_EVENT;
	}

	public boolean isMapMarkerDeleted()
	{
		return this.mapNavigateEventType == MAP_MARKER_DELETED_EVENT;
	}

	public boolean isMapMarkerSelected()
	{
		return this.mapNavigateEventType == MAP_MARKER_SELECTED_EVENT;
	}

	public boolean isMapMarkerMoved()
	{
		return this.mapNavigateEventType == MAP_MARKER_MOVED_EVENT;
	}

	public boolean isDataMarkerCreated()
	{
		return this.mapNavigateEventType == DATA_MARKER_CREATED_EVENT;
	}

	public boolean isDataMarkerDeleted()
	{
		return this.mapNavigateEventType == DATA_MARKER_DELETED_EVENT;
	}

	public boolean isDataMarkerSelected()
	{
		return this.mapNavigateEventType == DATA_MARKER_SELECTED_EVENT;
	}

	public boolean isDataMarkerDeselected()
	{
		return this.mapNavigateEventType == DATA_MARKER_DESELECTED_EVENT;
	}

	public boolean isDataMarkerMoved()
	{
		return this.mapNavigateEventType == DATA_MARKER_MOVED_EVENT;
	}

	public boolean isDataEventMarkerCreated()
	{
		return this.mapNavigateEventType == DATA_EVENTMARKER_CREATED_EVENT;
	}

	public boolean isDataAlarmMarkerCreated()
	{
		return this.mapNavigateEventType == DATA_ALARMMARKER_CREATED_EVENT;
	}

}
