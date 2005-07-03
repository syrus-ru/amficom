/*
 * $Id: Event.java,v 1.32 2005/06/25 17:07:53 bass Exp $
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

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.IdlEventParameter;
import com.syrus.AMFICOM.event.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.32 $, $Date: 2005/06/25 17:07:53 $
 * @author $Author: bass $
 * @module event_v1
 */

public final class Event extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 3977015150102788401L;

	protected static final int UPDATE_STATUS = 1;

	private EventType type;
	private String description;

	private Set<EventParameter> eventParameters;	//Set <EventParameter>
	private Set<Identifier> eventSourceIds; //Set <Identifier>

	Event(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.eventParameters = new HashSet<EventParameter>();
		this.eventSourceIds = new HashSet<Identifier>();

		final EventDatabase database = (EventDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENT_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public Event(final IdlEvent et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Event(final Identifier id,
			final Identifier creatorId,
			final long version,
			final EventType type,
			final String description,
			final Set<EventParameter> eventParameters,
			final Set<Identifier> eventSourceIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;

		this.eventParameters = new HashSet<EventParameter>(eventParameters.size());
		this.setEventParameters0(eventParameters);
		
		this.eventSourceIds = new HashSet<Identifier>(eventSourceIds.size());
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
	public static Event createInstance(final Identifier creatorId,
		EventType type,
		String description,
		Set<EventParameter> eventParameters,
		Set<Identifier> eventSourceIds) throws CreateObjectException {
		if (creatorId == null || type == null || description == null || eventParameters == null || eventSourceIds == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Event event = new Event(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_CODE),
					creatorId,
					0L,
					type,
					description,
					eventParameters,
					eventSourceIds);

			assert event.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			event.markAsChanged();

			return event;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected @Override void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlEvent et = (IdlEvent) transferable;

		super.fromTransferable(et.header);

		this.type = (EventType) StorableObjectPool.getStorableObject(new Identifier(et._typeId), true);

		this.description = et.description;

		this.eventParameters = new HashSet<EventParameter>(et.parameters.length);
		for (int i = 0; i < et.parameters.length; i++)
			this.eventParameters.add(new EventParameter(et.parameters[i]));

		this.eventSourceIds = new HashSet<Identifier>(et.eventSourceIds.length);
		this.setEventSourceIds0(Identifier.fromTransferables(et.eventSourceIds));

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEvent getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		int i = 0;
		IdlEventParameter[] ept = new IdlEventParameter[this.eventParameters.size()];
		for (Iterator<EventParameter> it = this.eventParameters.iterator(); it.hasNext(); i++)
			ept[i] = it.next().getTransferable(orb);

		IdlIdentifier[] esIdsT = Identifier.createTransferables(this.eventSourceIds);

		return new IdlEvent(super.getHeaderTransferable(orb),
				this.type.getId().getTransferable(),
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
	protected @Override boolean isValid() {
		return super.isValid()
				&& this.type != null
				&& this.description != null
				&& this.eventParameters != null && this.eventParameters != Collections.EMPTY_SET
				&& this.eventSourceIds != null && this.eventSourceIds != Collections.EMPTY_SET;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final EventType type,
			final String description) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.description = description;
	}

	protected synchronized void setEventParameters0(final Set<EventParameter> eventParameters) {
		this.eventParameters.clear();
		if (eventParameters != null)
			this.eventParameters.addAll(eventParameters);
	}

	protected synchronized void setEventSourceIds0(final Set<Identifier> eventSourceIds) {
		this.eventSourceIds.clear();
		if (eventSourceIds != null)
			this.eventSourceIds.addAll(eventSourceIds);
	}

	/**
	 * Set new type
	 * @param type
	 */
	public void setType(final EventType type) {
		this.type = type;
		super.markAsChanged();
	}

	/**
	 * Set new description
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * Set new array of event parameters
	 * @param eventParameters
	 */
	public void setEventParameters(final Set<EventParameter> eventParameters) {
		this.setEventParameters0(eventParameters);
		super.markAsChanged();
	}

	/**
	 * Set new list of event sources ids
	 * @param eventSourceIds
	 */
	public void setEventSourceIds(final Set<Identifier> eventSourceIds) {
		this.setEventSourceIds0(eventSourceIds);
		super.markAsChanged();
	}

	public @Override Set<Identifiable> getDependencies() {
		Set<Identifiable> dependencies = new HashSet<Identifiable>();

		dependencies.add(this.type);

		for(EventParameter eventParameter: this.eventParameters) {
			dependencies.add(eventParameter);
		}
		
		return dependencies;
	}
}
