/**
 * $Id: MapNavigateEvent.java,v 1.1 2004/12/20 14:11:51 stas Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;
import com.syrus.AMFICOM.general.corba.Identifier;

/**
 * ������� ���������/������ ��������� ��������(-��) �����
 *
 *
 *
 * @version $Revision: 1.1 $, $Date: 2004/12/20 14:11:51 $
 * @module map_v2
 * @author $Author: stas $
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
	public static final int MAP_MARKER_CREATED_EVENT = 1;
	/**
	 * ������������ ������ ������
	 * @param markerId
	 */
	public static final int MAP_MARKER_DELETED_EVENT = 2;
	/**
	 * ������������ ������� ������
	 * @param markerId
	 */
	public static final int MAP_MARKER_SELECTED_EVENT = 3;
	/**
	 * ������������ ���������� ������
	 * @param markerId
	 * @param distance
	 */
	public static final int MAP_MARKER_MOVED_EVENT = 4;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_MARKER_CREATED_EVENT = 5;
	/**
	 * ������ ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_EVENTMARKER_CREATED_EVENT = 6;
	/**
	 * ������ ������� ������� ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 * @param distance
	 */
	public static final int DATA_ALARMMARKER_CREATED_EVENT = 7;
	/**
	 * ������ ��� ������� ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final int DATA_MARKER_SELECTED_EVENT = 8;
	/**
	 * ������ ��� ������� ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final int DATA_MARKER_DESELECTED_EVENT = 9;
	/**
	 * ������ ��� ������ ��� ����� (��������, � ���� �������������)
	 * @param markerId
	 */
	public static final int DATA_MARKER_DELETED_EVENT = 10;
	/**
	 * @param markerId
	 * @param distance
	 * ������ ��� ���������� ��� ����� (��������, � ���� �������������)
	 */
	public static final int DATA_MARKER_MOVED_EVENT = 11;
	/**
	 * ������� �������
	 * @param source
	 */
	public static final int MAP_ELEMENT_SELECTED_EVENT = 13;
	/**
	 * ����� ��������� ��������
	 * @param source
	 */
	public static final int MAP_ELEMENT_DESELECTED_EVENT = 14;

	/**
	 * ��� �������
	 */
	protected int mapEventType;

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
		protected Identifier schemePathId;

	/**
	 * �������������� ������������ �������
	 */
		protected Identifier meId;

	/**
	 * ������������� ������� �����
	 */
	protected Identifier schemePathElementId;

	/**
	 * ������������ ���� ���������
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
			String markerId,
			double distance,
			Identifier schemePathId,
			Identifier meId,
			Identifier schemePathElementId)
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

	public void setSchemePathId(Identifier schemePathId)
	{
		this.schemePathId = schemePathId;
	}

	public Identifier getSchemePathId()
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

	public void setSchemePathElementId(Identifier schemePathElementId)
	{
		this.schemePathElementId = schemePathElementId;
	}

	public Identifier getSchemePathElementId()
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
