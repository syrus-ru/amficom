/**
 * $Id: MarkerEvent.java,v 1.2 2005/09/02 09:21:53 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.Identifier;

/**
 * ������� ���������/������ ��������� ��������(-��) �����
 *
 *
 *
 * @version $Revision: 1.2 $, $Date: 2005/09/02 09:21:53 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public class MarkerEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = 7592135696104602801L;

	/**
	 * ������������ ������ ������.
	 * ������������ ���� {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int MARKER_CREATED_EVENT = 1;
	/**
	 * ������������ ������ ������.
	 * ������������ ���� {@link #markerId}
	 */
	public static final int MARKER_DELETED_EVENT = 2;
	/**
	 * ������������ ������� ������.
	 * ������������ ���� {@link #markerId}
	 */
	public static final int MARKER_SELECTED_EVENT = 3;
	/**
	 * ������������ ���������� ������.
	 * ������������ ���� {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int MARKER_MOVED_EVENT = 4;
	/**
	 * ������������ ���� ��������� �������.
	 * ������������ ���� {@link #markerId}
	 */
	public static final int MARKER_DESELECTED_EVENT = 5;
	/**
	 * ������ ������� ��� ������ ��� ����� (��������, � ���� �������������).
	 * ������������ ���� {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int EVENTMARKER_CREATED_EVENT = 6;
	/**
	 * ������ ������� ������� ��� ������ ��� ����� (��������, � ���� 
	 * �������������).
	 * ������������ ���� {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int ALARMMARKER_CREATED_EVENT = 7;

	/**
	 * ��� �������.
	 */
	protected int markerEventType;

	/**
	 * ������������� �������.
	 */
	protected Identifier markerId;

	/**
	 * ������������� ���������� �������.
	 */
	protected double opticalDistance;

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

	/** ���������� ���������� �� �����. */
	public static final String MARKER_EVENT_TYPE = "markerevent";

	public MarkerEvent(
			Object source,
			int markerEventType)
	{
		super(source, MarkerEvent.MARKER_EVENT_TYPE, null, null);
		this.markerEventType = markerEventType;
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Object newValue)
	{
		super(source, MarkerEvent.MARKER_EVENT_TYPE, null, newValue);
		this.markerEventType = markerEventType;
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			Identifier schemePathId,
			Identifier meId)
	{
		this(source, markerEventType);
		this.markerId = markerId;
		this.opticalDistance = opticalDistance;
		this.schemePathId = schemePathId;
		this.meId = meId;
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			Identifier schemePathId,
			Identifier meId,
			Identifier schemePathElementId)
	{
		this(source, markerEventType, markerId, opticalDistance, schemePathId, meId);
		this.schemePathElementId = schemePathElementId;
	}

	public Identifier getMarkerId()
	{
		return this.markerId;
	}

	public void setOpticalDistance(double opticalDistance)
	{
		this.opticalDistance = opticalDistance;
	}

	public double getOpticalDistance()
	{
		return this.opticalDistance;
	}

	public void setSchemePathId(Identifier schemePathId)
	{
		this.schemePathId = schemePathId;
	}

	public Identifier getSchemePathId()
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

	public Identifier getSchemePathElementId()
	{
		return this.schemePathElementId;
	}

	public void setSchemePath(final Object schemePath)
	{
		this.schemePath = schemePath;
	}

	public Object getSchemePath()
	{
		return this.schemePath;
	}

	public int getMarkerEventType() {
		return this.markerEventType;
	}

}
