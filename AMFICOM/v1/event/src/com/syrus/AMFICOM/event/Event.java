/*
 * $Id: Event.java,v 1.3 2005/01/31 13:17:01 arseniy Exp $
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
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.event.corba.EventParameter_Transferable;
import com.syrus.AMFICOM.event.corba.Event_Transferable;
import com.syrus.AMFICOM.event.corba.EventStatus;

/**
 * @version $Revision: 1.3 $, $Date: 2005/01/31 13:17:01 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class Event extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 3977015150102788401L;

	protected static final int UPDATE_STATUS = 1;

	private EventType type;
	private int status;
	private String description;

	private EventParameter[] parameters;

	private StorableObjectDatabase eventDatabase;

	public Event(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.eventDatabase = EventDatabaseContext.eventDatabase;
		try {
			this.eventDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Event(Event_Transferable et) throws CreateObjectException {
		super(et.header);

		try {
			this.type = (EventType) EventStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.status = et.status.value();
		this.description = new String(et.description);

		try {
			this.parameters = new EventParameter[et.parameters.length];
			for (int i = 0; i < this.parameters.length; i++)
				this.parameters[i] = new EventParameter(et.parameters[i]);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.eventDatabase = EventDatabaseContext.eventDatabase;
	}

	protected Event(Identifier id,
					   Identifier creatorId,
					   EventType type,
						 String description,
						 EventParameter[] parameters) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId);
		this.type = type;
		this.status = EventStatus._EVENT_STATUS_GENERATED;
		this.description = description;
		this.parameters = parameters;

		super.currentVersion = super.getNextVersion();

		this.eventDatabase = EventDatabaseContext.eventDatabase;
	}

	/**
	 * new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param parameters
	 * @return new instance
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static Event createInstance(Identifier creatorId,
													EventType type,
													String description,
													EventParameter[] parameters) throws CreateObjectException {
		if (creatorId == null || type == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Event(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_ENTITY_CODE),
											creatorId,
											type,
											description,
											parameters);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Event.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.eventDatabase != null)
				this.eventDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		EventParameter_Transferable[] ept = new EventParameter_Transferable[this.parameters.length];
		for (int i = 0; i < ept.length; i++)
			ept[i] = (EventParameter_Transferable) this.parameters[i].getTransferable();
		return new Event_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				EventStatus.from_int(this.status),
				this.description,
				ept);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public EventStatus getStatus() {
		return EventStatus.from_int(this.status);
	}

	public String getDescription() {
		return this.description;
	}

	public EventParameter[] getParameters() {
		return this.parameters;
	}

	public void updateStatus(EventStatus status, Identifier modifierId) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = (Identifier)modifierId.clone();
		try {
			this.eventDatabase.update(this, UPDATE_STATUS, null);
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
																	EventType type,
																	int status,
																	String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.status = status;
		this.description = description;
	}

	protected synchronized void setParameters(EventParameter[] parameters) {
		this.parameters = parameters;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		for (int i = 0; i < this.parameters.length; i++)
			dependencies.add(this.parameters[i].getType());
		return dependencies;
	}
}
