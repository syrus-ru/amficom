/*
 * $Id: ActionType.java,v 1.18 2005/08/22 15:06:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @version $Revision: 1.18 $, $Date: 2005/08/22 15:06:21 $
 * @author $Author: arseniy $
 * @module measurement
 */

public abstract class ActionType extends StorableObjectType {
	private static final long serialVersionUID = 8566361712792210504L;

	public ActionType(Identifier id) {
		super(id);
	}

	ActionType() {
		// empty
	}

	public ActionType(final Identifier id,	
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id,
			  created,
			  modified,
			  creatorId,
			  modifierId,
			  version,
			  codename,
			  description);
	}

	@Override
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

	protected abstract void setParameterTypes(final Map<String, EnumSet<ParameterType>> parameterTypesModeMap);

	protected abstract Map<String, EnumSet<ParameterType>> getParameterTypesModeMap();
}
