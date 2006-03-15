/*
 * $Id: BitmapImageResource.java,v 1.39 2006/03/15 14:47:31 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourceHelper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.IdlBitmapImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.39 $, $Date: 2006/03/15 14:47:31 $
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
			final BitmapImageResource bitmapImageResource = new BitmapImageResource(IdentifierPool.getGeneratedIdentifier(IMAGERESOURCE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					image);

			assert bitmapImageResource.isValid() : OBJECT_STATE_ILLEGAL;

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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlImageResource getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final IdlBitmapImageResourceData bitmapImageResourceData = new IdlBitmapImageResourceData();
		bitmapImageResourceData.codename = this.codename;
		bitmapImageResourceData.image = this.image;
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.bitmapImageResourceData(bitmapImageResourceData);
		return IdlImageResourceHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
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
	public synchronized void fromIdlTransferable(final IdlImageResource idlImageResource)
	throws IdlConversionException {
		super.fromIdlTransferable(idlImageResource);

		final IdlImageResourceData imageResourceData = idlImageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._BITMAP;
		final IdlBitmapImageResourceData bitmapImageResourceData = imageResourceData.bitmapImageResourceData();
		this.codename = bitmapImageResourceData.codename;
		this.image = bitmapImageResourceData.image;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Override
	public BitmapImageResource clone() throws CloneNotSupportedException {
		BitmapImageResource clone = (BitmapImageResource) super.clone();
		clone.codename = clone.id.toString();
		clone.image = this.image.clone();

		return clone;
	}
}
