/*
 * $Id: AbstractBitmapImageResource.java,v 1.3 2004/12/21 10:37:42 arseniy Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/12/21 10:37:42 $
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
			final Identifier modifierId) {
		super(id, created, modified, creatorId, modifierId);
	}

	protected AbstractBitmapImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource);
	}

	public abstract String getCodename();
}
