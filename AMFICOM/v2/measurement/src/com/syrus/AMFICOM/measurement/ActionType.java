package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;

public abstract class ActionType extends StorableObject {
	String codename;
	String description;

	public ActionType(Identifier id) {
		super(id);
	}

	public ActionType(Identifier id,
										String codename,
										String description) {
		super(id);
		this.codename = codename;
		this.description = description;
	}

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
	}
}