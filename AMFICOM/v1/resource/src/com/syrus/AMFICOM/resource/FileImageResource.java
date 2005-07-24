/*
 * $Id: FileImageResource.java,v 1.26 2005/07/24 16:08:06 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM
 */

package com.syrus.AMFICOM.resource;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourceHelper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.26 $, $Date: 2005/07/24 16:08:06 $
 * @module resource_v1
 */
public final class FileImageResource extends AbstractBitmapImageResource {
	private static final long serialVersionUID = -3374486515234713818L;

	private String fileName;

	FileImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException
	 */
	public FileImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	FileImageResource(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String fileName) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.fileName = fileName;
	}

	public static FileImageResource createInstance(final Identifier creatorId, final String fileName) throws CreateObjectException {
		try {
			FileImageResource fileImageResource = new FileImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE),
					creatorId,
					0L,
					fileName);

			assert fileImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			fileImageResource.markAsChanged();

			return fileImageResource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	@Override
	public String getCodename() {
		return getFileName();
	}
	
	
	@Override
	public void setCodename(final String codename) {
		this.setFileName(codename);
	}
	
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @see AbstractImageResource#getImage()
	 */
	@Override
	public byte[] getImage() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlImageResource getTransferable(final ORB orb) {
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.fileName(this.fileName);
		return IdlImageResourceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version,
				imageResourceData);
	}

	public void setFileName(final String fileName) {
		setFileName0(fileName);
		this.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String fileName) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.fileName = fileName;
	}

	protected void setFileName0(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	ImageResourceSort getSort() {
		return ImageResourceSort.FILE;
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlImageResource idlImageResource = (IdlImageResource) transferable;
		super.fromTransferable(idlImageResource);

		final IdlImageResourceData imageResourceData = idlImageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._FILE;
		this.fileName = imageResourceData.fileName();
	}
}
