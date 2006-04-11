/*-
 * $Id: Alarm.java,v 1.2 2006/04/11 09:41:54 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.alarm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.AbstractReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class Alarm {
	private double delta_x = 0.5;
	
	private Set<AlarmEvent> lineMismatchEvents;
	
	private final SchemePath schemePath;
	private final PathElement pathElement;
	private final Identifier monitoredElementId;
	private final double physicalDistance;
	private final double opticalDistance;

	private Identifier markerId;
	
	private Date startDate;
	private Date endDate;
	private Measurement lastMeasurement;
	private String lastMessage;
	
	private class AlarmEvent {
		LineMismatchEvent lme;
		ReflectogramMismatchEvent rme;
		
		AlarmEvent(LineMismatchEvent lineMismatchEvent, ReflectogramMismatchEvent reflectogramMismatchEvent) {
			this.lme = lineMismatchEvent;
			this.rme = reflectogramMismatchEvent;
		}
	}
	
	public Alarm(LineMismatchEvent event) throws CreateObjectException {
		this.lineMismatchEvents = new HashSet<AlarmEvent>();

		try {
			this.pathElement = StorableObjectPool.getStorableObject(event.getAffectedPathElementId(), true);
			this.schemePath = this.pathElement.getParentPathOwner();
			AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event.getReflectogramMismatchEventId(), true);
			Measurement measurement = StorableObjectPool.getStorableObject(rme.getMeasurementId(), true);
			this.monitoredElementId = measurement.getMonitoredElementId();
			this.opticalDistance = event.getMismatchOpticalDistance();
			this.physicalDistance = event.getMismatchPhysicalDistance();
			
			addLineMismatchEvent(event);
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}

	public double getMismatchPhysicalDistance() {
		return this.physicalDistance;
	}
	
	public double getMismatchOpticalDistance() {
		return this.opticalDistance;
	}
	
	public Identifier getMarkerId() {
		if (this.markerId == null) {
			try {
				this.markerId = LocalIdentifierGenerator.generateIdentifier(ObjectEntities.MARK_CODE);
			} catch (IllegalObjectEntityException e) {
				Log.errorMessage(e);
			}
		}
		return this.markerId;
	}
	
	public PathElement getPathElement() {
		return this.pathElement;
	}
	
	public SchemePath getSchemePath() {
		return this.schemePath;
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public Measurement getLastMeasurement() {
		return this.lastMeasurement;
	}
	
	public String getMessage() {
		return this.lastMessage;
	}
	
	public boolean isSuitable(LineMismatchEvent event1) {
		if (this.pathElement.getId().equals(event1.getAffectedPathElementId())
				&& Math.abs(this.opticalDistance - event1.getMismatchOpticalDistance()) < this.delta_x
				&& Math.abs(this.physicalDistance - event1.getMismatchPhysicalDistance()) < this.delta_x) {
			return true;
		}
		return false;
	}
	
	public boolean isSuitable(Set<LineMismatchEvent> events) {
		for (LineMismatchEvent event1 : events) {
			if (!isSuitable(event1)) {
				return false;
			}
		}
		return true;
	}

	public void addLineMismatchEvent(LineMismatchEvent event) throws ApplicationException {
		final AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event.getReflectogramMismatchEventId(), true);
		this.lineMismatchEvents.add(new AlarmEvent(event, rme));
		update();
	}
	
	public void addLineMismatchEvents(Set<LineMismatchEvent> events) throws ApplicationException {
		for (LineMismatchEvent event : events) {
			final AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event.getReflectogramMismatchEventId(), true);
			this.lineMismatchEvents.add(new AlarmEvent(event, rme));
		}
		update();
	}
	
	public int getEventsCount() {
		return this.lineMismatchEvents.size();
	}
	
	private void update() throws ApplicationException {
		long first = Long.MAX_VALUE;
		long last = Long.MIN_VALUE;
		
		Identifier lastMeasurementId = Identifier.VOID_IDENTIFIER;
		for (AlarmEvent event : this.lineMismatchEvents) {
			final long time = event.rme.getCreated().getTime();
			if (first > time) {
				first = time;
			}
			if (last < time) {
				last = time;
				lastMeasurementId = event.rme.getMeasurementId();
				this.lastMessage = event.lme.getMessage();
			}
		}
		this.startDate = new Date(first);
		this.endDate = new Date(last);
		
		assert lastMeasurementId != Identifier.VOID_IDENTIFIER;
		this.lastMeasurement = StorableObjectPool.getStorableObject(lastMeasurementId, true);
	}
}
