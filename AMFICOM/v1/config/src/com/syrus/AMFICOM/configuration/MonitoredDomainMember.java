/*
 * $Id: MonitoredDomainMember.java,v 1.9 2005/03/03 21:30:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/03 21:30:43 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public abstract class MonitoredDomainMember extends DomainMember {
	private static final long serialVersionUID = 7920469930983188858L;

	Collection monitoredElementIds;

	protected MonitoredDomainMember(Identifier id) {
		super(id);
	}

	protected MonitoredDomainMember(StorableObject_Transferable transferable,
			 Identifier domainId) {
		super(transferable, domainId);
	}
	
	protected MonitoredDomainMember(Identifier id,
									Date created,
									Date modified,
									Identifier creatorId,
									Identifier modifierId,
									long version,
									Identifier domainId) {
		super(id,
			  created,
			  modified,
			  creatorId,
			  modifierId,
			  version,
			  domainId);
	}

	protected synchronized void setMonitoredElementIds0(Collection monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
				this.monitoredElementIds.addAll(monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds(Collection monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.changed = true;
	}

	public Collection getMonitoredElementIds() {
		return Collections.unmodifiableCollection(this.monitoredElementIds);
	}
}
