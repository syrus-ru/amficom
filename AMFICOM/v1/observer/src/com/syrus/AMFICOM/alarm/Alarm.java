/*-
 * $Id: Alarm.java,v 1.6 2006/06/08 19:21:10 bass Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.alarm;

import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.SortedSet;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class Alarm {
	private LineMismatchEvent leader;
	
	private final SchemePath schemePath;
	private final PathElement pathElement;
	private final Identifier monitoredElementId;
	private final Severity severity;
	private int count;

	private Identifier markerId;
	
	private Date startDate;
	private Date endDate;
	private Measurement lastMeasurement;
	private String lastMessage;
	
	public Alarm(LineMismatchEvent leader) throws CreateObjectException {
		this.leader = leader;
		
		try {
			this.pathElement = StorableObjectPool.getStorableObject(leader.getAffectedPathElementId(), true);
			this.schemePath = this.pathElement.getParentPathOwner();
			ReflectogramMismatchEvent rme = leader.getReflectogramMismatchEvent();
			Measurement measurement = StorableObjectPool.getStorableObject(rme.getMeasurementId(), true);
			this.monitoredElementId = measurement.getMonitoredElementId();
			this.severity = rme.getSeverity();
			this.startDate = rme.getCreated();
			
			final SortedSet<LineMismatchEvent> lineMismatchEvents = leader.getChildLineMismatchEvents();
			this.count = lineMismatchEvents.size();
			
			LineMismatchEvent last = lineMismatchEvents.last();
			ReflectogramMismatchEvent lastrme = last.getReflectogramMismatchEvent();
			this.endDate = lastrme.getCreated();
			this.lastMeasurement = StorableObjectPool.getStorableObject(lastrme.getMeasurementId(), true);
			this.lastMessage = last.getRichTextMessage();
			
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
		return this.leader.getMismatchPhysicalDistance();
	}
	
	public double getMismatchOpticalDistance() {
		return this.leader.getMismatchOpticalDistance();
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
	
	public Severity getSeverity() {
		return this.severity;
	}

	public AlarmStatus getState() {
		return this.leader.getAlarmStatus();
	}
	
	public void setState(AlarmStatus alarmStatus) {
		try {
			this.leader.setAlarmStatus(alarmStatus);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}
	}

	public String getMessage() {
		return this.lastMessage;
	}

	public int getEventsCount() {
		return this.count;
	}
	
	public LineMismatchEvent getLeadEvent() {
		return this.leader;
	}
	
	public void addLineMismatchEvent(final LineMismatchEvent lineMismatchEvent) throws ApplicationException {
		this.count++;
		
		ReflectogramMismatchEvent lastrme = lineMismatchEvent.getReflectogramMismatchEvent();
		this.endDate = lastrme.getCreated();
		this.lastMeasurement = StorableObjectPool.getStorableObject(lastrme.getMeasurementId(), true);
		this.lastMessage = lineMismatchEvent.getRichTextMessage();
	}
}
