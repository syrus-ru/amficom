/*
 * $Id: SchemeImageResource.java,v 1.8 2004/12/17 14:05:35 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2004/12/17 14:05:35 $
 * @module resource_v1
 */
public final class SchemeImageResource extends AbstractImageResource {
	private static final long serialVersionUID = -5633433107083921318L;

	private List data;

	public SchemeImageResource(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 */
	public SchemeImageResource(final ImageResource_Transferable imageResource) {
		super(imageResource);
		final ImageResourceData imageResourceData = imageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._SCHEME;
		this.data = safeUnpack(imageResourceData.image());
	}

	/**
	 * There's no formal parameter for {@link #data}; this property has to
	 * be set explicitly via {@link #setData(List)} or
	 * {@link #setData0(List)}.
	 */
	protected SchemeImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId) {
		super(id, created, modified, creatorId, modifierId);
		this.data = new ArrayList(3);
	}

	/**
	 * There's no formal parameter for {@link #data}; this property has to
	 * be set explicitly via {@link #setData(List)} or
	 * {@link #setData0(List)}.
	 */
	public static SchemeImageResource createInstance(final Identifier creatorId) throws CreateObjectException {
		try {
			final Date created = new Date();
			return new SchemeImageResource(
				IdentifierPool.getGeneratedIdentifier(
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
				created,
				created,
				creatorId,
				creatorId);
		} catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("SchemeImageResource.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public static SchemeImageResource getInstance(final ImageResource_Transferable imageResource) throws CreateObjectException {
		final SchemeImageResource schemeImageResource = new SchemeImageResource(imageResource);
		final StorableObjectDatabase imageResourceDatabase = ResourceDatabaseContext.getImageResourceDatabase();
		if (imageResourceDatabase != null) {
			schemeImageResource.imageResourceDatabase = imageResourceDatabase;
			try {
				imageResourceDatabase.insert(schemeImageResource);
			} catch (IllegalDataException ide) {
				throw new CreateObjectException(ide.getMessage(), ide);
			}
		}
		return schemeImageResource;
	}

	/**
	 * Common (both client-side and server-side) accessor for {@link #data}
	 * property.
	 */
	public List getData() {
		return Collections.unmodifiableList(this.data);
	}
    
	/**
	 * @see AbstractImageResource#getImage()
	 */
	public byte[] getImage() {
		return this.safePack(this.data);
	}

	public Object getTransferable() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.image(ImageResourceSort.SCHEME, safePack(this.data));
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	/**
	 * Client-side modifier for {@link #data} property, which increments
	 * entity version on every modification.
	 */
	public void setData(final List data) {
		this.currentVersion = getNextVersion();
		setData0(data);
	}

	public void setImage(final byte image[]) {
		this.currentVersion = getNextVersion();
		setImage0(image);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final byte packedData[]) {
		super.setAttributes(created, modified, creatorId, modifierId);
		this.data = safeUnpack(packedData);
	}

	/**
	 * Server-side modifier for {@link #data} property, which doesn't honor
	 * version information.
	 */
	protected void setData0(final List data) {
		this.data.clear();
		if (data != null)
			this.data.addAll(data);
	}

	protected void setImage0(final byte image[]) {
		this.data = safeUnpack(image);
	}

	private byte[] pack(final List data) throws IOException {
		ObjectOutputStream out = null;
		try {
			final ByteArrayOutputStream subOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(new GZIPOutputStream(subOut));
			out.writeObject(data);
			out.flush();
			return subOut.toByteArray();
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * @todo Add error-handling mechanism.
	 */
	private byte[] safePack(final List data) {
		try {
			return pack(data);
		} catch (IOException ioe) {
			Log.errorException(ioe);
		}
		return new byte[0];
	}

	/**
	 * @todo Add error-handling mechanism.
	 */
	private List safeUnpack(final byte packedData[]) {
		try {
			return unpack(packedData);
		} catch (IOException ioe) {
			Log.errorException(ioe);
		} catch (ClassNotFoundException cnfe) {
			Log.errorException(cnfe);
		}
		return Collections.EMPTY_LIST;
	}

	private List unpack(final byte packedData[]) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(packedData)));
			return (List) in.readObject();
		} finally {
			if (in != null)
				in.close();
		}
	}
}
