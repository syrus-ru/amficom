/*
 * $Id: DomainMember.java,v 1.8 2005/04/01 14:41:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/01 14:41:33 $
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

	protected DomainMember() {
		super();
	}

	protected DomainMember(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version);
		this.domainId = domainId;
	}	

	protected void fromTransferable(IDLEntity transferable, Identifier domainId) throws CreateObjectException {
		super.fromTransferable(transferable);
		this.domainId = domainId;
	}

	public final Identifier getDomainId() {
		return this.domainId;
	}
	
	protected final void setDomainId0(final Identifier domainId) {
		assert domainId.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE;
		this.domainId = domainId;
	}
	
	public final void setDomainId(final Identifier domainId) {
		this.setDomainId0(domainId);
		super.changed = true;
	}

	protected final synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId) {
		super.setAttributes(created, modified, creatorId, modifierId,
				version);
		this.domainId = domainId;
	}
}
