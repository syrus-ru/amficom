/*
 * $Id: EventSource.java,v 1.4 2005/02/03 14:48:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.event.corba.EventSource_Transferable;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/03 14:48:31 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventSource implements TransferableObject, Identified {
	private Identifier id;
	private Identifier entityId;

	public EventSource(EventSource_Transferable est) {
		this.id = new Identifier(est.id);
		this.entityId = new Identifier(est.entity_id);
	}

	protected EventSource (Identifier id, Identifier entityId) {
		this.id = id;
		this.entityId = entityId;
	}

	public Object getTransferable() {
		return new EventSource_Transferable((Identifier_Transferable) this.id.getTransferable(),
				(Identifier_Transferable) this.entityId.getTransferable());
	}

	public Identifier getId() {
		return this.id;
	}

	public Identifier getEntityId() {
		return this.entityId;
	}
}
