package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;

public abstract class ActionType extends StorableObject {
	String codename;
	String description;

	public ActionType(Identifier id) {
		super(id);
		this.codename = null;
		this.description = null;
	}

	public ActionType(Identifier id,
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