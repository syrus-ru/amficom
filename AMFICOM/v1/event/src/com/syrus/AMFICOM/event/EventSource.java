/*
 * $Id: EventSource.java,v 1.3 2005/02/02 15:09:47 arseniy Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/02/02 15:09:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventSource implements TransferableObject, Identified {
	private Identifier id;
	private Identifier sourceId;

	public EventSource(EventSource_Transferable est) {
		this.id = new Identifier(est.id);
		this.sourceId = new Identifier(est.source_id);
	}

	protected EventSource (Identifier id, Identifier sourceId) {
		this.id = id;
		this.sourceId = sourceId;
	}

	public Object getTransferable() {
		return new EventSource_Transferable((Identifier_Transferable) this.id.getTransferable(),
				(Identifier_Transferable) this.sourceId.getTransferable());
	}

	public Identifier getId() {
		return this.id;
	}

	public Identifier getSourceId() {
		return this.sourceId;
	}
}
