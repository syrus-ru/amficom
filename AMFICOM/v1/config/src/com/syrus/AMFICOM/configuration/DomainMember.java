/*
 * $Id: DomainMember.java,v 1.6 2004/07/28 12:54:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/28 12:54:18 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public abstract class DomainMember extends StorableObject {
	
	public static final String	COLUMN_DOMAIN_ID	= "domain_id";
	
	protected Identifier domainId;

	protected DomainMember(Identifier id) {
		super(id);
	}

	protected DomainMember(Identifier id,
												 Date created,
												 Date modified,
												 Identifier creator_id,
												 Identifier modifier_id,
												 Identifier domainId) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id);
		this.domainId = domainId;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domainId) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.domainId = domainId;
	}
}
