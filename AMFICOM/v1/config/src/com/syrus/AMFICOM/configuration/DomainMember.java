/*
 * $Id: DomainMember.java,v 1.9 2004/11/15 14:02:55 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2004/11/15 14:02:55 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public abstract class DomainMember extends StorableObject {
	static final long serialVersionUID = -5921180280594867215L;

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
