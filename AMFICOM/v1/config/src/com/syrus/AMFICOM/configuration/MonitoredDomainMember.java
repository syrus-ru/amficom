/*
 * $Id: MonitoredDomainMember.java,v 1.8 2005/02/11 07:49:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.administration.DomainMember;

/**
 * @version $Revision: 1.8 $, $Date: 2005/02/11 07:49:43 $
 * @author $Author: bob $
 * @module config_v1
 */

public abstract class MonitoredDomainMember extends DomainMember {
	private static final long serialVersionUID = 7920469930983188858L;

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

	protected synchronized void setMonitoredElementIds0(List monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
				this.monitoredElementIds.addAll(monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds(List monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.changed = true;
	}

	public List getMonitoredElementIds() {
		return Collections.unmodifiableList(this.monitoredElementIds);
	}
}
