/*
 * $Id: FileImageResource.java,v 1.12 2005/04/04 13:10:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import java.util.Date;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/04 13:10:29 $
 * @module resource_v1
 */
public final class FileImageResource extends AbstractBitmapImageResource {
	private static final long serialVersionUID = -3374486515234713818L;

	private String fileName;

	public FileImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException 
	 */
	public FileImageResource(final ImageResource_Transferable imageResource) throws CreateObjectException {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._FILE;
		this.fileName = imageResourceData.fileName();
	}

	protected FileImageResource(final Identifier id,
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

	public static FileImageResource createInstance(final Identifier creatorId,
			final String fileName) throws CreateObjectException {
		try {
			FileImageResource fileImageResource = new FileImageResource(
				IdentifierPool.getGeneratedIdentifier(
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
				creatorId,
				0L,
				fileName);
			fileImageResource.changed = true;
			return fileImageResource;
		} catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("FileImageResource.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public static FileImageResource getInstance(final ImageResource_Transferable imageResource) throws CreateObjectException {
		final FileImageResource fileImageResource = new FileImageResource(imageResource);
		final StorableObjectDatabase imageResourceDatabase1 = ResourceDatabaseContext.getImageResourceDatabase();
		if (imageResourceDatabase1 != null) {
			fileImageResource.imageResourceDatabase = imageResourceDatabase1;
			try {
				imageResourceDatabase1.insert(fileImageResource);
			} catch (IllegalDataException ide) {
				throw new CreateObjectException(ide.getMessage(), ide);
			}
		}
		return fileImageResource;
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
