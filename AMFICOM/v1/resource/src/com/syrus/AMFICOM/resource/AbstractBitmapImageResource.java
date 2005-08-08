/*
 * $Id: AbstractBitmapImageResource.java,v 1.11 2005/08/08 11:33:54 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Date;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/08 11:33:54 $
 * @module resource
 */
public abstract class AbstractBitmapImageResource extends AbstractImageResource {
	static final long serialVersionUID = -7202305745749708023L;

	protected AbstractBitmapImageResource(final Identifier id) throws ApplicationException {
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
