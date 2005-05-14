/*
 * $Id: ActionType.java,v 1.11 2005/05/14 09:43:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;

/**
 * @version $Revision: 1.11 $, $Date: 2005/05/14 09:43:14 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public abstract class ActionType extends StorableObjectType {
	private static final long serialVersionUID = 8566361712792210504L;

	public ActionType(Identifier id) {
		super(id);
	}

	ActionType() {
		// empty
	}

	public ActionType(Identifier id,	
					  Date created,
					  Date modified,
					  Identifier creatorId,
					  Identifier modifierId,
					  long version,
					  String codename,
					  String description) {
		super(id,
			  created,
			  modified,
			  creatorId,
			  modifierId,
			  version,
			  codename,
			  description);
	}

	protected synchronized void setAttributes(Date created,
											Date modified,
											Identifier creatorId,
											Identifier modifierId,
											long version,
											String codename,
											String description) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							codename,
							description);
	}

	protected abstract void setParameterTypeIds(Map parameterTypeIdsModeMap);

	protected abstract Map getParameterTypeIdsModeMap();
}
