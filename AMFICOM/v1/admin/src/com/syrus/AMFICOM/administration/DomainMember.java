/*
 * $Id: DomainMember.java,v 1.3 2005/02/01 11:36:51 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/01 11:36:51 $
 * @author $Author: bob $
 * @module administration_v1
 */

public abstract class DomainMember extends StorableObject {
	private static final long serialVersionUID = -5921180280594867215L;

	public static final String	COLUMN_DOMAIN_ID	= "domain_id";

	Identifier domainId;

	protected DomainMember(Identifier id) {
		super(id);
	}

	protected DomainMember(StorableObject_Transferable transferable,
				 Identifier domainId) {
		super(transferable);
		this.domainId = domainId;
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
	
	protected void setDomainId0(Identifier domainId) {
		this.domainId = domainId;
	}
	
	public void setDomainId(Identifier domainId) {
		this.setDomainId0(domainId);
		super.currentVersion = super.getNextVersion();
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
