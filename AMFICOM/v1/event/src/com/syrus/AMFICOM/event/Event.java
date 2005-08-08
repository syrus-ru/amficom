/*
 * $Id: Event.java,v 1.39 2005/08/08 11:32:37 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEvent;
import com.syrus.AMFICOM.event.corba.IdlEventHelper;
import com.syrus.AMFICOM.event.corba.IdlEventPackage.IdlEventParameter;
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.39 $, $Date: 2005/08/08 11:32:37 $
 * @author $Author: arseniy $
 * @module event
 */

public final class Event extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 3977015150102788401L;

	private EventType type;
	private String description;

	private Set<EventParameter> eventParameters;	//Set <EventParameter>
	private Set<Identifier> eventSourceIds; //Set <Identifier>

	Event(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.eventParameters = new HashSet<EventParameter>();
		this.eventSourceIds = new HashSet<Identifier>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.EVENT_CODE).retrieve(this);
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
			final StorableObjectVersion version,
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
			final Event event = new Event(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEvent et = (IdlEvent) transferable;

		super.fromTransferable(et);

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
		final IdlEventParameter[] ept = new IdlEventParameter[this.eventParameters.size()];
		for (final EventParameter eventParameter : this.eventParameters) {
			ept[i] = eventParameter.getTransferable(orb);
		}

		final IdlIdentifier[] esIdsT = Identifier.createTransferables(this.eventSourceIds);

		return IdlEventHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
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

	public Set<EventParameter> getParameters() {
		return Collections.unmodifiableSet(this.eventParameters);
	}

	public Set<Identifier> getEventSourceIds() {
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
			final StorableObjectVersion version,
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
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		for(EventParameter eventParameter: this.eventParameters) {
			dependencies.add(eventParameter);
		}

		return dependencies;
	}
}
