/*
 * $Id: AbstractBitmapImageResource.java,v 1.6 2005/04/02 15:29:47 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import java.util.Date;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/04/02 15:29:47 $
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

	protected AbstractBitmapImageResource(final ImageResource_Transferable imageResource) throws CreateObjectException {
		super(imageResource);
	}

	public abstract String getCodename();
	
	public abstract void setCodename(String codename);
}
