/*
 * $Id: AbstractBitmapImageResource.java,v 1.1 2004/12/15 10:31:59 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/15 10:31:59 $
 * @module resource_v1
 */
public abstract class AbstractBitmapImageResource extends AbstractImageResource {
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

	public abstract byte[] getImage();
}
