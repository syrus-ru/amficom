/*
 * $Id: FileImageResource.java,v 1.17 2005/06/02 14:27:40 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM
 */

package com.syrus.AMFICOM.resource;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.17 $, $Date: 2005/06/02 14:27:40 $
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
	FileImageResource(final ImageResource_Transferable imageResource) throws CreateObjectException {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._FILE;
		this.fileName = imageResourceData.fileName();
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
			FileImageResource fileImageResource = new FileImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
					creatorId,
					0L,
					fileName);

			assert fileImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			fileImageResource.changed = true;
			try {
				StorableObjectPool.putStorableObject(fileImageResource);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return fileImageResource;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	public String getCodename() {
		return getFileName();
	}
	
	
	public void setCodename(String codename) {
		this.setFileName(codename);
	}
	
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @see AbstractImageResource#getImage()
	 */
	public byte[] getImage() {
		throw new UnsupportedOperationException();
	}

	public IDLEntity getTransferable() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.fileName(this.fileName);
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	public void setFileName(final String fileName) {
		this.changed = true;
		setFileName0(fileName);
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
}
