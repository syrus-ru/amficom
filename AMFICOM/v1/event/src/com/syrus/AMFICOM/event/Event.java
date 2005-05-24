/*
 * $Id: Event.java,v 1.23 2005/05/24 13:24:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.EventParameter_Transferable;
import com.syrus.AMFICOM.event.corba.Event_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.23 $, $Date: 2005/05/24 13:24:55 $
 * @author $Author: bass $
 * @module event_v1
 */

public final class Event extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 3977015150102788401L;

	protected static final int UPDATE_STATUS = 1;

	private EventType type;
	private String description;

	private Set eventParameters;	//Set <EventParameter>
	private Set eventSourceIds; //Set <Identifier>

	public Event(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.eventParameters = new HashSet();
		this.eventSourceIds = new HashSet();

		EventDatabase database = (EventDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENT_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public Event(Event_Transferable et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Event(Identifier id,
					   Identifier creatorId,
					   long version,
					   EventType type,
						 String description,
						 Set eventParameters,
						 Set eventSourceIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;

		this.eventParameters = new HashSet(eventParameters.size());
		this.setEventParameters0(eventParameters);
		
		this.eventSourceIds = new HashSet(eventSourceIds.size());
		this.setEventSourceIds0(eventSourceIds);
	}

	/**
	 * new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param eventSourceIds
	 * @return new instance
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static Event createInstance(Identifier creatorId,
		EventType type,
		String description,
		Set eventParameters,
		Set eventSourceIds) throws CreateObjectException {
		if (creatorId == null || type == null || description == null || eventParameters == null || eventSourceIds == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Event event = new Event(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_ENTITY_CODE),
											creatorId,
											0L,
											type,
											description,
											eventParameters,
											eventSourceIds);

			assert event.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			event.changed = true;
			return event;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Event_Transferable et = (Event_Transferable) transferable;

		super.fromTransferable(et.header);

		this.type = (EventType) StorableObjectPool.getStorableObject(new Identifier(et.type_id), true);

		this.description = et.description;

		this.eventParameters = new HashSet(et.parameters.length);
		for (int i = 0; i < et.parameters.length; i++)
			this.eventParameters.add(new EventParameter(et.parameters[i]));

		this.eventSourceIds = new HashSet(et.event_source_ids.length);
		this.setEventSourceIds0(Identifier.fromTransferables(et.event_source_ids));

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		int i = 0;
		EventParameter_Transferable[] ept = new EventParameter_Transferable[this.eventParameters.size()];
		for (Iterator it = this.eventParameters.iterator(); it.hasNext(); i++)
			ept[i] = (EventParameter_Transferable) ((EventParameter) it.next()).getTransferable();

		Identifier_Transferable[] esIdsT = Identifier.createTransferables(this.eventSourceIds);

		return new Event_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				this.description,
				ept,
				esIdsT);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public Set getParameters() {
		return Collections.unmodifiableSet(this.eventParameters);
	}

	public Set getEventSourceIds() {
		return Collections.unmodifiableSet(this.eventSourceIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid()
				&& this.type != null
				&& this.description != null
				&& this.eventParameters != null && this.eventParameters != Collections.EMPTY_SET
				&& this.eventSourceIds != null && this.eventSourceIds != Collections.EMPTY_SET;
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																  long version,
																	EventType type,
																	String description) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.description = description;
	}

	protected synchronized void setEventParameters0(Set eventParameters) {
		this.eventParameters.clear();
		if (eventParameters != null)
			this.eventParameters.addAll(eventParameters);
	}

	protected synchronized void setEventSourceIds0(Set eventSourceIds) {
		this.eventSourceIds.clear();
		if (eventSourceIds != null)
			this.eventSourceIds.addAll(eventSourceIds);
	}

	/**
	 * Set new type
	 * @param type
	 */
	public void setType(EventType type) {
		this.type = type;
		super.changed = true;
	}

	/**
	 * Set new description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	/**
	 * Set new array of event parameters
	 * @param eventParameters
	 */
	public void setEventParameters(Set eventParameters) {
		this.setEventParameters0(eventParameters);
		super.changed = true;
	}

	/**
	 * Set new list of event sources ids
	 * @param eventSourceIds
	 */
	public void setEventSourceIds(Set eventSourceIds) {
		this.setEventSourceIds0(eventSourceIds);
		super.changed = true;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();

		dependencies.add(this.type);

		for (Iterator it = this.eventParameters.iterator(); it.hasNext();)
			dependencies.add(((EventParameter) it.next()).getType());

		return dependencies;
	}
}
