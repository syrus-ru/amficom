/*
 * $Id: MonitoredDomainMember.java,v 1.1 2004/08/04 08:59:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/04 08:59:27 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public abstract class MonitoredDomainMember extends DomainMember {
	List monitoredElementIds;

	protected MonitoredDomainMember(Identifier id) {
		super(id);
	}

	protected MonitoredDomainMember(Identifier id,
																	Date created,
																	Date modified,
																	Identifier creator_id,
																	Identifier modifier_id,
																	Identifier domainId) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id,
					domainId);
	}

	protected synchronized void setMonitoredElementIds(List monitoredElementIds) {
		this.monitoredElementIds = monitoredElementIds;
	}
	
	public List getMonitoredElementIds() {
		return this.monitoredElementIds;
	}

	public synchronized void addMonitoredElementId(Identifier monitoredElementId) {
		if (! this.hasMonitoredElementId(monitoredElementId))
			this.monitoredElementIds.add(monitoredElementId);
		else
			Log.errorMessage("Moniored domain member '" + this.id.toString() + "' already has monitored element '" + monitoredElementId.toString() + "'");
	}

	public synchronized void removeMonitoredElementId(Identifier monitoredElementId) {
		if (this.hasMonitoredElementId(monitoredElementId))
			this.monitoredElementIds.remove(monitoredElementId);
		else
			Log.errorMessage("Moniored domain member '" + this.id.toString() + "' does not has monitored element '" + monitoredElementId.toString() + "'");
	}

	public boolean hasMonitoredElementId(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}
}
