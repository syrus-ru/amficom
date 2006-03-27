/*-
 * $Id: MarkerEvent.java,v 1.6 2006/03/27 14:29:50 stas Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;
import java.util.Set;
import java.util.SortedSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * Событие выделения/снятия выделения элемента(-ов) карты
 *
 * @version $Revision: 1.6 $, $Date: 2006/03/27 14:29:50 $
 * @module mapclient
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 */
public class MarkerEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = 7592135696104602801L;

	/**
	 * Пользователь создал маркер.
	 * Используются поля {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int MARKER_CREATED_EVENT = 1;
	/**
	 * Пользователь удалил маркер.
	 * Используются поля {@link #markerId}
	 */
	public static final int MARKER_DELETED_EVENT = 2;
	/**
	 * Пользователь выделил маркер.
	 * Используются поля {@link #markerId}
	 */
	public static final int MARKER_SELECTED_EVENT = 3;
	/**
	 * Пользователь передвинул маркер.
	 * Используются поля {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int MARKER_MOVED_EVENT = 4;
	/**
	 * Пользователь снял выделение маркера.
	 * Используются поля {@link #markerId}
	 */
	public static final int MARKER_DESELECTED_EVENT = 5;

	/**
	 * Маркер сигнала тревоги был создан вне карты (например, в окне 
	 * рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int ALARMMARKER_CREATED_EVENT = 7;

	/**
	 * Тип события.
	 */
	protected int markerEventType;

	/**
	 * Идентификатор маркера.
	 */
	protected Identifier markerId;

	/**
	 * Относительные координаты маркера.
	 */
	// TODO notify about physical distance 
	protected double opticalDistance;

	/**
	 * Идентификатор исследуемого объекта.
	 */
	protected Identifier meId;

	/**
	 * Идентификатор пути.
	 */
	protected Identifier schemePathId;
	
	/**
	 * Идентификатор элемента пути.
	 */
	protected Identifier pathElementId;
	
	
	
	/** Управление элементами на карте. */
	public static final String MARKER_EVENT_TYPE = "markerevent"; //$NON-NLS-1$

	public MarkerEvent(Object source, int markerEventType, Identifier markerId) {
		this(source, markerEventType, markerId, 0, null, null, null);
		assert markerEventType != MARKER_MOVED_EVENT
				: "Short constructor is not allowed for MOVED action";
		assert markerEventType != MARKER_CREATED_EVENT 
				&& markerEventType != ALARMMARKER_CREATED_EVENT 
				: "Short constructor is not allowed for CREATE action";
	}
	
//	public MarkerEvent(Object source, int markerEventType, Identifier markerId, double opticalDistance) {
//		this(source, markerEventType, markerId, opticalDistance, null, null, null);
//		assert markerEventType != MARKER_CREATED_EVENT 
//				&& markerEventType != ALARMMARKER_CREATED_EVENT 
//				: "Short constructor is not allowed for CREATE action";
//	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			PathElement pe) {
		this(source, markerEventType, markerId, opticalDistance,  pe.getId(), pe.getParentPathOwner().getId(), getMonitoredElementId(pe.getParentPathOwner()));
		this.pathElementId = pe.getId();
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			MonitoredElement me) {
		this(source, markerEventType, markerId, opticalDistance, null, getSchemePathId(me), me.getId());
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			Identifier pathElementId,
			Identifier schemePathId,
			Identifier meId) {
		super(source, MarkerEvent.MARKER_EVENT_TYPE, null, null);
		this.markerEventType = markerEventType;
		this.markerId = markerId;
		this.opticalDistance = opticalDistance;
		this.pathElementId = pathElementId;
		this.schemePathId = schemePathId;
		this.meId = meId;
	}
	
	private static final Identifier getMonitoredElementId(final SchemePath schemePath) {
		try {
			final SortedSet<PathElement> pathMemebers = schemePath.getPathMembers();
			if (!pathMemebers.isEmpty()) {
				AbstractSchemePort startPort = pathMemebers.first().getEndAbstractSchemePort();
				if (startPort != null) {
					Identifier measurementPortId = startPort.getMeasurementPortId();
					if (!measurementPortId.isVoid()) {
						LinkedIdsCondition condition = new LinkedIdsCondition(measurementPortId, ObjectEntities.MONITOREDELEMENT_CODE);
						Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						if (!mes.isEmpty()) {
							MonitoredElement me = mes.iterator().next();
							return me.getId();
						}
					}
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return null;
	}
	
	private static final Identifier getSchemePathId(final MonitoredElement me) {
		try {
			Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();
			
			if (!tpathIds.isEmpty()) {
				Set<SchemePath> schemePaths = StorableObjectPool.getStorableObjectsByCondition(
						new LinkedIdsCondition(tpathIds.iterator().next(), ObjectEntities.SCHEMEPATH_CODE), true);
				
				if (!schemePaths.isEmpty()) {
					return schemePaths.iterator().next().getId();
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return null;
	}
	
	public Identifier getMarkerId() {
		return this.markerId;
	}

	public double getOpticalDistance() {
		return this.opticalDistance;
	}

	public Identifier getMeId() {
		return this.meId;
	}
	
	public Identifier getPathElementId() {
		return this.pathElementId;
	}
	
	public Identifier getSchemePathId() {
		return this.schemePathId;
	}

	public int getMarkerEventType() {
		return this.markerEventType;
	}
}
