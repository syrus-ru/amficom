/*
 * $Id: MonitoredElement.java,v 1.9 2004/08/03 17:15:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/03 17:15:58 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public abstract class MonitoredElement extends DomainMember {
	Identifier monitoredElementId;

	public MonitoredElement(Identifier id) {
		super(id);
	}
	
	MonitoredElement(Identifier id,
									 Date created,
									 Date modified,
									 Identifier creator_id,
									 Identifier modifier_id,
									 Identifier domainId,
									 Identifier monitoredElementId) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id,
					domainId);
		this.monitoredElementId = monitoredElementId;
	}
	
	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domainId,
																						Identifier monitoredElementId) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domainId);
		this.monitoredElementId = monitoredElementId;
	}
}
