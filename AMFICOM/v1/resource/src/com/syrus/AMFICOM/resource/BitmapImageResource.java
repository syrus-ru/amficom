/*
 * $Id: BitmapImageResource.java,v 1.34 2005/12/02 11:24:14 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourceHelper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.IdlBitmapImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: bass $
 * @version $Revision: 1.34 $, $Date: 2005/12/02 11:24:14 $
 * @module resource
 */
public final class BitmapImageResource extends AbstractBitmapImageResource implements Cloneable {
	private static final long serialVersionUID = 7625218221524477821L;

	private String codename;

	private byte image[];

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException
	 */
	public BitmapImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	BitmapImageResource(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
			final BitmapImageResource bitmapImageResource = new BitmapImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					image);

			assert bitmapImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			bitmapImageResource.markAsChanged();

			return bitmapImageResource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	@Override
	public String getCodename() {
		return this.codename;
	}

	/**
	 * @see AbstractImageResource#getImage()
	 */
	@Override
	public byte[] getImage() {
		return this.image;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlImageResource getTransferable(final ORB orb) {
		final IdlBitmapImageResourceData bitmapImageResourceData = new IdlBitmapImageResourceData();
		bitmapImageResourceData.codename = this.codename;
		bitmapImageResourceData.image = this.image;
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.bitmapImageResourceData(bitmapImageResourceData);
		return IdlImageResourceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				imageResourceData);
	}

	@Override
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
			final StorableObjectVersion version,
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

	@Override
	ImageResourceSort getSort() {
		return ImageResourceSort.BITMAP;
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlImageResource idlImageResource = (IdlImageResource) transferable;
		super.fromTransferable(idlImageResource);

		final IdlImageResourceData imageResourceData = idlImageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._BITMAP;
		final IdlBitmapImageResourceData bitmapImageResourceData = imageResourceData.bitmapImageResourceData();
		this.codename = bitmapImageResourceData.codename;
		this.image = bitmapImageResourceData.image;
	}

	@Override
	public BitmapImageResource clone() throws CloneNotSupportedException {
		BitmapImageResource clone = (BitmapImageResource) super.clone();
		clone.codename = clone.id.toString();
		clone.image = this.image.clone();

		return clone;
	}
}
