/*
 * $Id: AbstractBitmapImageResource.java,v 1.7 2005/06/24 09:40:48 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/06/24 09:40:48 $
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
			final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	protected AbstractBitmapImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	public abstract String getCodename();
	
	public abstract void setCodename(String codename);
}
