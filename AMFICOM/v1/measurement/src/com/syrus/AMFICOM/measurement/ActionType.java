/*
 * $Id: ActionType.java,v 1.4 2004/07/27 15:52:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/27 15:52:25 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

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
