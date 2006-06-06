/*
 * $Id: EventSource.java,v 1.49 2006/06/06 11:27:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENTSOURCE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventSource;
import com.syrus.AMFICOM.event.corba.IdlEventSourceHelper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.49 $, $Date: 2006/06/06 11:27:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventSource extends StorableObject
		implements IdlTransferableObjectExt<IdlEventSource> {
	private static final long serialVersionUID = 3833179220682682674L;

	/**
	 * @serial include
	 */
	private Identifier sourceEntityId;

	public EventSource(final IdlEventSource est) {
		try {
			this.fromIdlTransferable(est);
		} catch (final IdlConversionException ice) {
			/*
			 * Never.
			 */
			Log.debugMessage(ice, SEVERE);
		}
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
			final EventSource eventSource = new EventSource(IdentifierPool.getGeneratedIdentifier(EVENTSOURCE_CODE),
					creatorId,
					INITIAL_VERSION,
					sourceEntityId);

			assert eventSource.isValid() : OBJECT_STATE_ILLEGAL;

			eventSource.markAsChanged();

			return eventSource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public synchronized void fromIdlTransferable(final IdlEventSource est)
	throws IdlConversionException {
		super.fromIdlTransferable(est);
		this.sourceEntityId = Identifier.valueOf(est.sourceEntityId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEventSource getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlEventSourceHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.sourceEntityId.getIdlTransferable());
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
	protected Set<Identifiable> getDependenciesTmpl() {
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
