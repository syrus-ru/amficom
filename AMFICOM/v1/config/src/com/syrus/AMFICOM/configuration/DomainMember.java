/*
 * $Id: DomainMember.java,v 1.10 2004/12/27 09:56:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.10 $, $Date: 2004/12/27 09:56:24 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public abstract class DomainMember extends StorableObject {
	private static final long serialVersionUID = -5921180280594867215L;

	public static final String	COLUMN_DOMAIN_ID	= "domain_id";
	
	Identifier domainId;

	protected DomainMember(Identifier id) {
		super(id);
	}

	DomainMember(StorableObject_Transferable transferable,
				 Identifier domainId) {
		super(transferable);
		this.domainId = domainId;
	}
	
	DomainMember(Identifier id,
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
