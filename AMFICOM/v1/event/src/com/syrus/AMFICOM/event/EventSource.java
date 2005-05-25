/*
 * $Id: EventSource.java,v 1.16 2005/05/25 13:01:02 bass Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/05/25 13:01:02 $
 * @author $Author: bass $
 * @module event_v1
 */
public final class EventSource extends StorableObject {
	private static final long serialVersionUID = 3833179220682682674L;

	private Identifier sourceEntityId;

	EventSource (Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		EventSourceDatabase database = (EventSourceDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENTSOURCE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	EventSource(EventSource_Transferable est) {
		this.fromTransferable(est);
	}

	EventSource(Identifier id,
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
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) {
		EventSource_Transferable est = (EventSource_Transferable) transferable;
		try {
			super.fromTransferable(est);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.sourceEntityId = new Identifier(est.source_entity_id);
	}

	public IDLEntity getTransferable() {
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
