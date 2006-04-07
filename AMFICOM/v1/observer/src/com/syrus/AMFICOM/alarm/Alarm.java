/*-
 * $Id: Alarm.java,v 1.1 2006/04/07 10:50:17 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.alarm;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.AbstractReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
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
	
	private Set<LineMismatchEvent> lineMismatchEvents;
	
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
	
	public Alarm(LineMismatchEvent event) throws CreateObjectException {
		this.lineMismatchEvents = new HashSet<LineMismatchEvent>();
		
		try {
			this.pathElement = StorableObjectPool.getStorableObject(event.getAffectedPathElementId(), true);
			this.schemePath = this.pathElement.getParentPathOwner();
			AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event.getReflectogramMismatchEventId(), true);
			Measurement measurement = StorableObjectPool.getStorableObject(rme.getMeasurementId(), true);
			this.monitoredElementId = measurement.getMonitoredElementId();
			this.opticalDistance = event.getMismatchOpticalDistance();
			this.physicalDistance = event.getMismatchPhysicalDistance();
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
		
		addLineMismatchEvent(event);
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
		try {
			final PathElement pathElement1 = StorableObjectPool.getStorableObject(event1.getAffectedPathElementId(), true);
			AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event1.getReflectogramMismatchEventId(), true);
			Measurement measurement = StorableObjectPool.getStorableObject(rme.getMeasurementId(), true);
			final Identifier monitoredElementId1 = measurement.getMonitoredElementId();
			final double opticalDistance1 = event1.getMismatchOpticalDistance();
			final double physicalDistance1 = event1.getMismatchPhysicalDistance();
			
			if (this.pathElement.equals(pathElement1)
					&& this.monitoredElementId.equals(monitoredElementId1)
					&& Math.abs(this.opticalDistance - opticalDistance1) < this.delta_x
					&& Math.abs(this.physicalDistance - physicalDistance1) < this.delta_x) {
				return true;
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
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

	public void addLineMismatchEvent(LineMismatchEvent event) {
		this.lineMismatchEvents.add(event);
		try {
			update();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	public void addLineMismatchEvents(Set<LineMismatchEvent> events) {
		this.lineMismatchEvents.addAll(events);
		try {
			update();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	public Set<LineMismatchEvent> getLineMismatchEvents() {
		return Collections.unmodifiableSet(this.lineMismatchEvents);
	}
	
	private void update() throws ApplicationException {
		long first = Long.MAX_VALUE;
		long last = Long.MIN_VALUE;
		
		Identifier lastMeasurementId = Identifier.VOID_IDENTIFIER;
		for (LineMismatchEvent event : this.lineMismatchEvents) {
			final AbstractReflectogramMismatchEvent rme = StorableObjectPool.getStorableObject(event.getReflectogramMismatchEventId(), true);
			final long time = rme.getCreated().getTime();
			if (first > time) {
				first = time;
			}
			if (last < time) {
				last = time;
				lastMeasurementId = rme.getMeasurementId();
				this.lastMessage = event.getMessage();
			}
		}
		this.startDate = new Date(first);
		this.endDate = new Date(last);
		
		assert lastMeasurementId != Identifier.VOID_IDENTIFIER;
		this.lastMeasurement = StorableObjectPool.getStorableObject(lastMeasurementId, true);
	}
}
