/*
 * $Id: MonitoredDomainMember.java,v 1.3 2004/11/12 10:25:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/12 10:25:32 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public abstract class MonitoredDomainMember extends DomainMember {
	List monitoredElementIds;

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
