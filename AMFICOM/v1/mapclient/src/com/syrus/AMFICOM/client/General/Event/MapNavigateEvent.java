/**
 * $Id: MapNavigateEvent.java,v 1.14 2005/02/09 11:40:52 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.IdentifierDefaultFactory;

/**
 * ������� ���������/������ ��������� ��������(-��) �����
 *
 *
 *
 * @version $Revision: 1.14 $, $Date: 2005/02/09 11:40:52 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public class MapNavigateEvent extends MapEvent
{
	/**
	 * ������������ ������ ������.
	 * ������������ ���� {@link #markerId}, {@link #distance}, {@link #spd}
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
	 * ������� �������.
	 * ������������ ���� {@link #source}
	 */
	public static final int MAP_ELEMENT_SELECTED_EVENT = 13;
	/**
	 * ����� ��������� ��������.
	 * ������������ ���� {@link #source}
	 */
	public static final int MAP_ELEMENT_DESELECTED_EVENT = 14;

	/**
	 * ��� �������.
	 */
	protected int mapEventType;

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
	protected Object spd = null;
	
	/**
	 * ��������� �����, ������������ ��� ����������� ���������������
	 * {@link com.syrus.AMFICOM.general.Identifier} �
	 * {@link com.syrus.AMFICOM.general.corba.Identifier} �
	 */
	private static IdentifierDefaultFactory identifierFactory = new IdentifierDefaultFactory();

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
			com.syrus.AMFICOM.general.corba.Identifier schemePathId,
			Identifier meId)
	{
		super(source, MAP_NAVIGATE);
		this.markerId = markerId;
		this.distance = distance;
		this.schemePathId = new Identifier(schemePathId.transferable());
		this.meId = meId;
	}

	public MapNavigateEvent(
			Object source,
			int typ,
			Identifier markerId,
			double distance,
			com.syrus.AMFICOM.general.corba.Identifier schemePathId,
			Identifier meId,
			com.syrus.AMFICOM.general.corba.Identifier schemePathElementId)
	{
		this(source, typ, markerId, distance, schemePathId, meId);
		this.schemePathElementId = new Identifier(schemePathElementId.transferable());
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

	public void setSchemePathId(com.syrus.AMFICOM.general.corba.Identifier schemePathId)
	{
		this.schemePathId = new Identifier(schemePathId.transferable());
	}

	public com.syrus.AMFICOM.general.corba.Identifier getSchemePathId()
	{
		return identifierFactory.newInstanceFromPrimitive(
				this.schemePathId.getMajor(), 
				this.schemePathId.getMinor());
	}

	public void setMeId(Identifier meId)
	{
		this.meId = meId;
	}

	public Identifier getMeId()
	{
		return this.meId;
	}

	public void setSchemePathElementId(com.syrus.AMFICOM.general.corba.Identifier schemePathElementId)
	{
		this.schemePathElementId = new Identifier(schemePathElementId.transferable());
	}

	public com.syrus.AMFICOM.general.corba.Identifier getSchemePathElementId()
	{
		return identifierFactory.newInstanceFromPrimitive(
				this.schemePathElementId.getMajor(), 
				this.schemePathElementId.getMinor());
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
		return this.spd;
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
