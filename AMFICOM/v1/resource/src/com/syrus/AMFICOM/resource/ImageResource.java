/*
 * $Id: ImageResource.java,v 1.2 2004/11/29 11:48:05 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/11/29 11:48:05 $
 * @module resource_v1
 */
public final class ImageResource extends StorableObject {
	private static final long serialVersionUID = 4115004659204330486L;

	/**
	 * @see StorableObject#StorableObject(Identifier)
	 */
	public ImageResource(final Identifier id) {
		super(id);
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#StorableObject(StorableObject_Transferable)
	 */
	public ImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource.header);
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}
}
