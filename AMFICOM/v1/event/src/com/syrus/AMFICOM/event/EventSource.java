/*
 * $Id: EventSource.java,v 1.9 2005/04/01 15:20:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.EventSource_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2005/04/01 15:20:22 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventSource extends StorableObject {
	private static final long serialVersionUID = 3833179220682682674L;

	private Identifier sourceEntityId;

	private StorableObjectDatabase	eventSourceDatabase;

	protected EventSource (Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.eventSourceDatabase = EventDatabaseContext.getEventSourceDatabase();
		try {
			this.eventSourceDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EventSource(EventSource_Transferable est) throws CreateObjectException {
		this.eventSourceDatabase = EventDatabaseContext.getEventSourceDatabase();
		this.fromTransferable(est);
	}

	protected EventSource(Identifier id,
			 Identifier creatorId,
			 long version,
			 Identifier sourceEntityId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.sourceEntityId = sourceEntityId;

		this.eventSourceDatabase = EventDatabaseContext.getEventSourceDatabase();
	}

	public static EventSource createInstance(Identifier creatorId,
			Identifier sourceEntityId) throws CreateObjectException {
		if (creatorId == null || sourceEntityId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			EventSource eventSource = new EventSource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTSOURCE_ENTITY_CODE),
					creatorId,
					0L,
					sourceEntityId);
			eventSource.changed = true;
			return eventSource;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("EventSource.createInstance | cannot generate identifier ", e);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		EventSource_Transferable est = (EventSource_Transferable) transferable;
		super.fromTransferable(est);
		this.sourceEntityId = new Identifier(est.source_entity_id);
	}

	public Object getTransferable() {
		return new EventSource_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.sourceEntityId.getTransferable());
	}

	public Identifier getSourceEntityId() {
		return this.sourceEntityId;
	}

	public void setSourceEntityId(Identifier sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																	long version,
																	Identifier sourceEntityId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												version);
		this.sourceEntityId = sourceEntityId;
	}

	public Set getDependencies() {
		return Collections.singleton(this.sourceEntityId);
	}

}
