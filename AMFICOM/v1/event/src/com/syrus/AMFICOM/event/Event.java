/*-
 * $Id: Event.java,v 1.45 2005/10/25 19:53:15 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_CODE;

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
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.45 $, $Date: 2005/10/25 19:53:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */

public final class Event extends StorableObject<Event>
		implements TypedObject<EventType> {
	private static final long serialVersionUID = 3977015150102788401L;

	private EventType type;
	private String description;

	private Set<EventParameter> eventParameters;
	private Set<Identifier> eventSourceIds;

	public Event(final IdlEvent event) throws CreateObjectException {
		try {
			this.fromTransferable(event);
		} catch (final ApplicationException ae) {
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
				new Date(),
				new Date(),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;

		this.setEventParameters0(eventParameters);

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
			final EventType type,
			final String description,
			final Set<EventParameter> eventParameters,
			final Set<Identifier> eventSourceIds)
	throws CreateObjectException {
		if (creatorId == null || type == null || description == null
				|| eventParameters == null
				|| eventSourceIds == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final Event event = new Event(IdentifierPool.getGeneratedIdentifier(EVENT_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					type,
					description,
					eventParameters,
					eventSourceIds);

			assert event.isValid() : OBJECT_STATE_ILLEGAL;

			event.markAsChanged();
			return event;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(
			final IdlStorableObject transferable)
	throws ApplicationException {
		final IdlEvent event = (IdlEvent) transferable;

		super.fromTransferable(event);

		this.type = StorableObjectPool.getStorableObject(new Identifier(event._typeId), true);

		this.description = event.description;

		this.eventParameters = new HashSet<EventParameter>(event.parameters.length);
		for (final IdlEventParameter eventParameter : event.parameters) {
			this.eventParameters.add(new EventParameter(eventParameter));
		}

		this.setEventSourceIds0(Identifier.fromTransferables(event.eventSourceIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEvent getTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		int i = 0;
		final IdlEventParameter[] idlEventParameters = new IdlEventParameter[this.eventParameters.size()];
		for (final EventParameter eventParameter : this.eventParameters) {
			idlEventParameters[i++] = eventParameter.getTransferable(orb);
		}

		final IdlIdentifier[] idlEventSourceIds = Identifier.createTransferables(this.eventSourceIds);

		return IdlEventHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.type.getId().getTransferable(),
				this.description,
				idlEventParameters,
				idlEventSourceIds);
	}

	public EventType getType() {
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
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.type != null
				&& this.description != null
				&& this.eventParameters != null && this.eventParameters != Collections.EMPTY_SET
				&& this.eventSourceIds != null && this.eventSourceIds != Collections.EMPTY_SET;
	}

	synchronized void setAttributes(final Date created,
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

	synchronized void setEventParameters0(final Set<EventParameter> eventParameters) {
		if (eventParameters == null) {
			throw new NullPointerException();
		}

		if (this.eventParameters == null) {
			this.eventParameters = new HashSet<EventParameter>(eventParameters.size());
		} else {
			this.eventParameters.clear();
		}
		this.eventParameters.addAll(eventParameters);
	}

	synchronized void setEventSourceIds0(final Set<Identifier> eventSourceIds) {
		if (eventSourceIds == null) {
			throw new NullPointerException();
		}

		if (this.eventSourceIds == null) {
			this.eventSourceIds = new HashSet<Identifier>(eventSourceIds.size());
		} else {
			this.eventSourceIds.clear();
		}
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

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		for(EventParameter eventParameter: this.eventParameters) {
			dependencies.add(eventParameter);
		}
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected EventWrapper getWrapper() {
		return EventWrapper.getInstance();
	}
}
