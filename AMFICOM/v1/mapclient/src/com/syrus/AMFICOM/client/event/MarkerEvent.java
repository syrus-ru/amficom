/*-
 * $Id: MarkerEvent.java,v 1.3 2005/09/29 10:58:28 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Событие выделения/снятия выделения элемента(-ов) карты
 *
 * @version $Revision: 1.3 $, $Date: 2005/09/29 10:58:28 $
 * @module mapclient
 * @author $Author: krupenn $
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
	 * Маркер события был создан вне карты (например, в окне рефлектограмм).
	 * Используются поля {@link #markerId}, {@link #opticalDistance}
	 */
	public static final int EVENTMARKER_CREATED_EVENT = 6;
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
	protected double opticalDistance;

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

	/** Управление элементами на карте. */
	public static final String MARKER_EVENT_TYPE = "markerevent";

	public MarkerEvent(Object source, int markerEventType) {
		super(source, MarkerEvent.MARKER_EVENT_TYPE, null, null);
		this.markerEventType = markerEventType;
	}

	public MarkerEvent(Object source, int markerEventType, Object newValue) {
		super(source, MarkerEvent.MARKER_EVENT_TYPE, null, newValue);
		this.markerEventType = markerEventType;
	}

	public MarkerEvent(
			Object source,
			int markerEventType,
			Identifier markerId,
			double opticalDistance,
			Identifier schemePathId,
			Identifier meId) {
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
			Identifier schemePathElementId) {
		this(
				source,
				markerEventType,
				markerId,
				opticalDistance,
				schemePathId,
				meId);
		this.schemePathElementId = schemePathElementId;
	}

	public Identifier getMarkerId() {
		return this.markerId;
	}

	public void setOpticalDistance(double opticalDistance) {
		this.opticalDistance = opticalDistance;
	}

	public double getOpticalDistance() {
		return this.opticalDistance;
	}

	public void setSchemePathId(Identifier schemePathId) {
		this.schemePathId = schemePathId;
	}

	public Identifier getSchemePathId() {
		return this.schemePathId;
	}

	public void setMeId(Identifier meId) {
		this.meId = meId;
	}

	public Identifier getMeId() {
		return this.meId;
	}

	public void setSchemePathElementId(Identifier schemePathElementId) {
		this.schemePathElementId = schemePathElementId;
	}

	public Identifier getSchemePathElementId() {
		return this.schemePathElementId;
	}

	public void setSchemePath(final Object schemePath) {
		this.schemePath = schemePath;
	}

	public Object getSchemePath() {
		return this.schemePath;
	}

	public int getMarkerEventType() {
		return this.markerEventType;
	}

}
