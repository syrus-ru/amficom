/*
 * $Id: MonitoredDomainMember.java,v 1.7 2005/01/14 18:07:08 arseniy Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/01/14 18:07:08 $
 * @author $Author: arseniy $
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
