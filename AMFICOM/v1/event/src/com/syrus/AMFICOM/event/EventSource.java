/*
 * $Id: EventSource.java,v 1.37 2005/10/30 15:20:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventSource;
import com.syrus.AMFICOM.event.corba.IdlEventSourceHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.37 $, $Date: 2005/10/30 15:20:47 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventSource extends StorableObject<EventSource> {
	private static final long serialVersionUID = 3833179220682682674L;

	private Identifier sourceEntityId;

	public EventSource(final IdlEventSource est) {
		this.fromTransferable(est);
	}

	EventSource(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier sourceEntityId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.sourceEntityId = sourceEntityId;
	}

	public static EventSource createInstance(final Identifier creatorId, final Identifier sourceEntityId)
			throws CreateObjectException {
		if (creatorId == null || sourceEntityId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final EventSource eventSource = new EventSource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTSOURCE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					sourceEntityId);

			assert eventSource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			eventSource.markAsChanged();

			return eventSource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) {
		final IdlEventSource est = (IdlEventSource) transferable;
		try {
			super.fromTransferable(est);
		}
		catch (ApplicationException ae) {
			// Never
			assert Log.errorMessage(ae);
		}
		this.sourceEntityId = new Identifier(est.sourceEntityId);
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEventSource getTransferable(final ORB orb) {
		return IdlEventSourceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.sourceEntityId.getTransferable());
	}

	public Identifier getSourceEntityId() {
		return this.sourceEntityId;
	}

	public void setSourceEntityId(final Identifier sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier sourceEntityId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.sourceEntityId = sourceEntityId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.sourceEntityId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected EventSourceWrapper getWrapper() {
		return EventSourceWrapper.getInstance();
	}
}
