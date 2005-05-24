/*
 * $Id: AbstractImageResource.java,v 1.11 2005/05/24 13:24:56 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/05/24 13:24:56 $
 * @module resource_v1
 */
public abstract class AbstractImageResource extends StorableObject {
	static final long serialVersionUID = -730035505208725678L;

	/**
	 * Server-side constructor. Shouldn't be invoked by clients.
	 */
	protected AbstractImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		ImageResourceDatabase database = (ImageResourceDatabase) DatabaseContext.getDatabase(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
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
			final Identifier modifierId,
			final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * Middle-tier constructor. Shouldn't be invoked by clients.
	 * @throws CreateObjectException
	 */
	protected AbstractImageResource(final ImageResource_Transferable irt) throws CreateObjectException {
		try {
			this.fromTransferable(irt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		ImageResource_Transferable irt = (ImageResource_Transferable) transferable;
		super.fromTransferable(irt.header);
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public abstract byte[] getImage();	

	ImageResourceSort getSort() {
		if (this instanceof BitmapImageResource)
			return ImageResourceSort.BITMAP;
		else if (this instanceof FileImageResource)
			return ImageResourceSort.FILE;
		return ImageResourceSort.SCHEME;
	}
}
