/*
 * $Id: BitmapImageResource.java,v 1.1 2004/12/03 19:11:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.*;
import com.syrus.util.Log;
import java.awt.*;
import java.util.Date;


/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/03 19:11:29 $
 * @module resource_v1
 */
public final class BitmapImageResource extends AbstractImageResource {
	private static final long serialVersionUID = 7625218221524477821L;

	private Image image;

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
		assert imageResourceData.discriminator().value() == ImageResourceSort._BYTES;
		this.image = safeUnpack(imageResourceData.image());
	}

	protected BitmapImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final Image image) {
		super(id, created, modified, creatorId, modifierId);
		this.image = image;
	}

	public static BitmapImageResource createInstance(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final Image image) {
		return new BitmapImageResource(IdentifierPool.generateId(
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
				created,
				modified,
				creatorId,
				modifierId,
				image);
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

	public Image getImage() {
		return this.image;
	}

	public Object getTransferable() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.image(ImageResourceSort.BYTES, safePack(this.image));
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	public void setImage(final Image image) {
		this.currentVersion = getNextVersion();
		setImage0(image);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final byte packedImage[]) {
		super.setAttributes(created, modified, creatorId, modifierId);
		this.image = safeUnpack(packedImage);
	}

	protected void setImage0(final Image image) {
		this.image = image;
	}

	/**
	 * @todo Implement image to byte array conversion.
	 */
	private byte[] pack(final Image image) {
		throw new UnsupportedOperationException();
	}

	private byte[] safePack(final Image image) {
		try {
			return pack(image);
		} catch (UnsupportedOperationException uoe) {
			Log.errorException(uoe);
		}
		return new byte[0];
	}

	private Image safeUnpack(final byte packedImage[]) {
		return unpack(packedImage);
	}

	private Image unpack(final byte packedImage[]) {
		return Toolkit.getDefaultToolkit().createImage(packedImage);
	}
}
