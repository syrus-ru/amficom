/*
 * $Id: EventSource.java,v 1.5 2005/02/08 20:24:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.event.corba.EventSource_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
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
 * @version $Revision: 1.5 $, $Date: 2005/02/08 20:24:57 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventSource extends StorableObject {
	private static final long serialVersionUID = 3833179220682682674L;

	private Identifier sourceEntityId;

	private StorableObjectDatabase	eventSourceDatabase;

	protected EventSource (Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.eventSourceDatabase = EventDatabaseContext.eventSourceDatabase;
		try {
			this.eventSourceDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EventSource(EventSource_Transferable est) {
		super(est.header);

		this.sourceEntityId = new Identifier(est.source_entity_id);

		this.eventSourceDatabase = EventDatabaseContext.eventSourceDatabase;
	}

	protected EventSource(Identifier id,
			 Identifier creatorId,
			 Identifier sourceEntityId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId);
		this.sourceEntityId = sourceEntityId;

		super.currentVersion = super.getNextVersion();

		this.eventSourceDatabase = EventDatabaseContext.eventSourceDatabase;
	}

	public static EventSource createInstance(Identifier creatorId,
			Identifier sourceEntityId) throws CreateObjectException {
		if (creatorId == null || sourceEntityId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new EventSource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTSOURCE_ENTITY_CODE),
					creatorId,
					sourceEntityId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("EventSource.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.eventSourceDatabase != null)
				this.eventSourceDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
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
		super.currentVersion = super.getNextVersion();
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																	Identifier sourceEntityId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.sourceEntityId = sourceEntityId;
	}

	public List getDependencies() {
		return Collections.singletonList(this.sourceEntityId);
	}

}
