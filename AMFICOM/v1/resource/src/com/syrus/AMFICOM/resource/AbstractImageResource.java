/*
 * $Id: AbstractImageResource.java,v 1.3 2004/12/16 16:11:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/12/16 16:11:58 $
 * @module resource_v1
 */
public abstract class AbstractImageResource extends StorableObject {
	StorableObjectDatabase imageResourceDatabase;

	/**
	 * Server-side constructor. Shouldn't be invoked by clients.
	 */
	protected AbstractImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		try {
			this.imageResourceDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * Client-side constructor. Shouldn't be invoked directly but only via
	 * <code>createInstance(...)</code> method (if any).
	 */
	protected AbstractImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId) {
		super(id, created, modified, creatorId, modifierId);
	}

	/**
	 * Middle-tier constructor. Shouldn't be invoked by clients.
	 */
	protected AbstractImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource.header);
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}

	public abstract byte[] getImage();

	/**
	 * @throws CreateObjectException
	 * @see StorableObject#insert()
	 */
	public void insert() throws CreateObjectException {
		throw new UnsupportedOperationException();
	}

	ImageResourceSort getSort() {
		if (this instanceof BitmapImageResource)
			return ImageResourceSort.BITMAP;
		else if (this instanceof FileImageResource)
			return ImageResourceSort.FILE;
		return ImageResourceSort.SCHEME;
	}
}
