/*
 * $Id: FileImageResource.java,v 1.2 2004/12/03 19:11:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.*;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/12/03 19:11:29 $
 * @module resource_v1
 */
public final class FileImageResource extends AbstractImageResource {
	private static final long serialVersionUID = -3374486515234713818L;

	private String fileName;

	public FileImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 */
	public FileImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._FILE;
		this.fileName = imageResourceData.fileName();
	}

	protected FileImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final String fileName) {
		super(id, created, modified, creatorId, modifierId);
		this.fileName = fileName;
	}

	public static FileImageResource createInstance(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final String fileName) {
		return new FileImageResource(IdentifierPool.generateId(
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
				created,
				modified,
				creatorId,
				modifierId,
				fileName);
	}

	public static FileImageResource getInstance(final ImageResource_Transferable imageResource) throws CreateObjectException {
		final FileImageResource fileImageResource = new FileImageResource(imageResource);
		final StorableObjectDatabase imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		if (imageResourceDatabase != null) {
			fileImageResource.imageResourceDatabase = imageResourceDatabase;
			try {
				imageResourceDatabase.insert(fileImageResource);
			} catch (IllegalDataException ide) {
				throw new CreateObjectException(ide.getMessage(), ide);
			}
		}
		return fileImageResource;
	}

	public String getFileName() {
		return this.fileName;
	}

	public Object getTransferable() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.fileName(this.fileName);
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	public void setFileName(final String fileName) {
		this.currentVersion = getNextVersion();
		setFileName0(fileName);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final String fileName) {
		super.setAttributes(created, modified, creatorId, modifierId);
		this.fileName = fileName;
	}

	protected void setFileName0(final String fileName) {
		this.fileName = fileName;
	}
}
