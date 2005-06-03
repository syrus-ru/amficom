/*
 * $Id: BitmapImageResource.java,v 1.17 2005/06/03 20:38:27 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.BitmapImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.17 $, $Date: 2005/06/03 20:38:27 $
 * @module resource_v1
 */
public final class BitmapImageResource extends AbstractBitmapImageResource {
	private static final long serialVersionUID = 7625218221524477821L;

	private String codename;

	private byte image[];

	BitmapImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException
	 */
	BitmapImageResource(final ImageResource_Transferable imageResource) throws CreateObjectException {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._BITMAP;
		final BitmapImageResourceData bitmapImageResourceData = imageResourceData.bitmapImageResourceData();
		this.codename = bitmapImageResourceData.codename;
		this.image = bitmapImageResourceData.image;
	}

	BitmapImageResource(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final byte image[]) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.codename = codename;
		this.image = image;
	}

	public static BitmapImageResource createInstance(final Identifier creatorId, final String codename, final byte image[])
			throws CreateObjectException {
		try {
			BitmapImageResource bitmapImageResource = new BitmapImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					image);

			assert bitmapImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			bitmapImageResource.markAsChanged();

			return bitmapImageResource;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	public String getCodename() {
		return this.codename;
	}

	/**
	 * @see AbstractImageResource#getImage()
	 */
	public byte[] getImage() {
		return this.image;
	}

	public IDLEntity getTransferable() {
		final BitmapImageResourceData bitmapImageResourceData = new BitmapImageResourceData();
		bitmapImageResourceData.codename = this.codename;
		bitmapImageResourceData.image = this.image;
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.bitmapImageResourceData(bitmapImageResourceData);
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	public void setCodename(final String codename) {
		setCodename0(codename);
		this.markAsChanged();
	}

	public void setImage(final byte image[]) {
		setImage0(image);
		this.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final byte image[]) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
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
