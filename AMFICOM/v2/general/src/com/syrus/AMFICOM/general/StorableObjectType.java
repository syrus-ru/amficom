package com.syrus.AMFICOM.general;

import java.util.Date;

public abstract class StorableObjectType extends StorableObject {
	protected String codename;
	protected String description;

	public StorableObjectType(Identifier id) {
		super(id);
		this.codename = null;
		this.description = null;
	}

	public StorableObjectType(Identifier id,
														Date created,
														Date modified,
														Identifier creator_id,
														Identifier modifier_id,
														String codename,
														String description) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id);
		this.codename = codename;
		this.description = description;
	}

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.codename = codename;
		this.description = description;
	}
}