/*
 * $Id: MonitoredDomainMember.java,v 1.6 2004/12/27 09:56:24 arseniy Exp $
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

/**
 * @version $Revision: 1.6 $, $Date: 2004/12/27 09:56:24 $
 * @author $Author: arseniy $
 * @module configuration_v1
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

	protected synchronized void setMonitoredElementIds0(List monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
				this.monitoredElementIds.addAll(monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds(List monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.currentVersion = super.getNextVersion();
	}

	public List getMonitoredElementIds() {
		return Collections.unmodifiableList(this.monitoredElementIds);
	}
}
