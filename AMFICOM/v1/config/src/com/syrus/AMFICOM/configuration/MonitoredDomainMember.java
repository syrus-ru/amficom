/*
 * $Id: MonitoredDomainMember.java,v 1.2 2004/08/19 12:21:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/19 12:21:22 $
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
}
