/*
 * $Id: AbstractBitmapImageResource.java,v 1.9 2005/07/26 08:51:42 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Date;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/07/26 08:51:42 $
 * @module resource_v1
 */
public abstract class AbstractBitmapImageResource extends AbstractImageResource {
	static final long serialVersionUID = -7202305745749708023L;

	protected AbstractBitmapImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	protected AbstractBitmapImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	protected AbstractBitmapImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	public abstract String getCodename();

	public abstract void setCodename(final String codename);
}
