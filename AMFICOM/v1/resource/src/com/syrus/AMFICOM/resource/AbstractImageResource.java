/*
 * $Id: AbstractImageResource.java,v 1.7 2005/04/02 15:29:47 arseniy Exp $
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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/04/02 15:29:47 $
 * @module resource_v1
 */
public abstract class AbstractImageResource extends StorableObject {
	static final long serialVersionUID = -730035505208725678L;

	StorableObjectDatabase imageResourceDatabase;

	/**
	 * Server-side constructor. Shouldn't be invoked by clients.
	 */
	protected AbstractImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		try {
			this.imageResourceDatabase.retrieve(this);
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
		this.imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
	}

	/**
	 * Middle-tier constructor. Shouldn't be invoked by clients.
	 * @throws CreateObjectException 
	 */
	protected AbstractImageResource(final ImageResource_Transferable irt) throws CreateObjectException {
		this.imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		this.fromTransferable(irt);
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
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
