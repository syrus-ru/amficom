/*
 * $Id: Alarm.java,v 1.1 2004/12/27 22:07:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.event.corba.Alarm_Transferable;
import com.syrus.AMFICOM.event.corba.AlarmStatus;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/27 22:07:46 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class Alarm extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 7434164393062127091L;

	protected static final int UPDATE_STATUS = 1;

	private AlarmType type;
	private Identifier eventId;
	private int status;
	private int level;
	private String description;

	private StorableObjectDatabase alarmDatabase;

	public Alarm(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.alarmDatabase = EventDatabaseContext.alarmDatabase;
		try {
			this.alarmDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Alarm(Alarm_Transferable at) throws CreateObjectException {
		super(at.header);

		try {
			this.type = (AlarmType)EventStorableObjectPool.getStorableObject(new Identifier(at.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.eventId = new Identifier(at.event_id);
		this.status = at.status.value();
		this.level = at.level.value();
		this.description = new String(at.description);

		this.alarmDatabase = EventDatabaseContext.alarmDatabase;
	}

	protected Alarm(Identifier id,
					   Identifier creatorId,
					   AlarmType type,
						 Identifier eventId,
						 AlarmLevel level,
						 String description) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId);
		this.type = type;
		this.eventId = eventId;
		this.status = AlarmStatus._ALARM_STATUS_GENERATED;
		this.level = level.value();
		this.description = description;

		super.currentVersion = super.getNextVersion();

		this.alarmDatabase = EventDatabaseContext.alarmDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param eventId
	 * @param level
	 * @param description
	 * @return new instance
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static Alarm createInstance(Identifier creatorId,
													AlarmType type,
													Identifier eventId,
													AlarmLevel level,
													String description) throws CreateObjectException {
		if (creatorId == null || type == null || eventId == null || level == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Alarm(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ALARM_ENTITY_CODE),
						creatorId,
						type,
						eventId,
						level,
						description);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Alarm.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.alarmDatabase != null)
				this.alarmDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		return new Alarm_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)this.type.getId().getTransferable(),
									(Identifier_Transferable)this.eventId.getTransferable(),
									AlarmStatus.from_int(this.status),
									AlarmLevel.from_int(this.level),
									this.description);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public Identifier getEventId() {
		return this.eventId;
	}

	public AlarmStatus getStatus() {
		return AlarmStatus.from_int(this.status);
	}

	public AlarmLevel getLevel() {
		return AlarmLevel.from_int(this.level);
	}

	public String getDescription() {
		return this.description;
	}

	public void updateStatus(AlarmStatus status, Identifier modifierId) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = (Identifier)modifierId.clone();
		try {
			this.alarmDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
		catch (VersionCollisionException vce){
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																	AlarmType type,
																	Identifier eventId,
																	int status,
																	int level,
																	String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.eventId = eventId;
		this.status = status;
		this.level = level;
		this.description = description;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.eventId);
		return dependencies;
	}
}
