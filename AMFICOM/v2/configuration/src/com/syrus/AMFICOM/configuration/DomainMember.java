package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.util.StorableObject;

public abstract class DomainMember extends StorableObject {
	Identifier domain_id;

	DomainMember(Identifier id, Identifier domain_id) {
		super(id);
		this.domain_id = domain_id;
	}

	public Identifier getDomainId() {
		return this.domain_id;
	}
}