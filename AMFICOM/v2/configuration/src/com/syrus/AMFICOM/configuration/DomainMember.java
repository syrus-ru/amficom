package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;

public abstract class DomainMember extends StorableObject {
	protected Identifier domain_id;

	public DomainMember(Identifier id) {
		super(id);
		this.domain_id = null;
	}

	public DomainMember(Identifier id,
											Date created,
											Date modified,
											Identifier creator_id,
											Identifier modifier_id,
											Identifier domain_id) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id);
		this.domain_id = domain_id;
	}

	public Identifier getDomainId() {
		return this.domain_id;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domain_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.domain_id = domain_id;
	}
}