/*
 * $Id: BitmapImageResource.java,v 1.4 2004/12/15 14:44:39 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.*;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/12/15 14:44:39 $
 * @module resource_v1
 */
public final class BitmapImageResource extends AbstractBitmapImageResource {
	private static final long serialVersionUID = 7625218221524477821L;

	private String codename;

	private byte image[];

	public BitmapImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 */
	public BitmapImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._BITMAP;
		final BitmapImageResourceData bitmapImageResourceData = imageResourceData.bitmapImageResourceData();
		this.codename = bitmapImageResourceData.codename;
		this.image = bitmapImageResourceData.image;
	}

	protected BitmapImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final String codename,
			final byte image[]) {
		super(id, created, modified, creatorId, modifierId);
		this.codename = codename;
		this.image = image;
	}

	public static BitmapImageResource createInstance(final Identifier creatorId,
			final String codename,
			final byte image[]) throws CreateObjectException {
		try {
			final Date created = new Date();
			return new BitmapImageResource(
				IdentifierPool.getGeneratedIdentifier(
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
				created,
				created,
				creatorId,
				creatorId,
				codename,
				image);
		} catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("BitmapImageResource.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public static BitmapImageResource getInstance(final ImageResource_Transferable imageResource) throws CreateObjectException {
		final BitmapImageResource bitmapImageResource = new BitmapImageResource(imageResource);
		final StorableObjectDatabase imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		if (imageResourceDatabase != null) {
			bitmapImageResource.imageResourceDatabase = imageResourceDatabase;
			try {
				imageResourceDatabase.insert(bitmapImageResource);
			} catch (IllegalDataException ide) {
				throw new CreateObjectException(ide.getMessage(), ide);
			}
		}
		return bitmapImageResource;
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	public String getCodename() {
		return this.codename;
	}

	/**
	 * @see AbstractBitmapImageResource#getImage()
	 */
	public byte[] getImage() {
		return this.image;
	}

	public Object getTransferable() {
		final BitmapImageResourceData bitmapImageResourceData = new BitmapImageResourceData();
		bitmapImageResourceData.codename = this.codename;
		bitmapImageResourceData.image = this.image;
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.bitmapImageResourceData(bitmapImageResourceData);
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	public void setCodename(final String codename) {
		this.currentVersion = getNextVersion();
		setCodename0(codename);
	}

	public void setImage(final byte image[]) {
		this.currentVersion = getNextVersion();
		setImage0(image);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final String codename,
			final byte image[]) {
		super.setAttributes(created, modified, creatorId, modifierId);
		this.codename = codename;
		this.image = image;
	}

	protected void setCodename0(final String codename) {
		this.codename = codename;
	}

	protected void setImage0(final byte image[]) {
		this.image = image;
	}
}
