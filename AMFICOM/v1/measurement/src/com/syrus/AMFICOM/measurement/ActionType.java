package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;

public abstract class ActionType extends StorableObjectType {

	public ActionType(Identifier id) {
		super(id);
	}

	public ActionType(Identifier id,
										Date created,
										Date modified,
										Identifier creatorId,
										Identifier modifierId,
										String codename,
										String description) {
		super(id,
					created,
					modified,
					creatorId,
					modifierId,
					codename,
					description);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
	}
}
