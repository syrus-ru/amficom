/*
 * $Id: SchemeImageResource.java,v 1.16 2005/05/18 11:37:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceData;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/05/18 11:37:17 $
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
	 * @throws CreateObjectException
	 */
	public SchemeImageResource(final ImageResource_Transferable imageResource) throws CreateObjectException {
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
			final Identifier creatorId,
			final long version) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.data = new ArrayList(3);
	}

	/**
	 * There's no formal parameter for {@link #data}; this property has to
	 * be set explicitly via {@link #setData(List)} or
	 * {@link #setData0(List)}.
	 */
	public static SchemeImageResource createInstance(final Identifier creatorId) throws CreateObjectException {
		try {
			SchemeImageResource schemeImageResource = new SchemeImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE),
					creatorId,
					0L);
			schemeImageResource.changed = true;
			return schemeImageResource;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
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

	public IDLEntity getTransferable() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.image(ImageResourceSort.SCHEME, safePack(this.data));
		return new ImageResource_Transferable(getHeaderTransferable(), imageResourceData);
	}

	/**
	 * Client-side modifier for {@link #data} property, which increments
	 * entity version on every modification.
	 */
	public void setData(final List data) {
		this.changed = true;
		setData0(data);
	}

	public void setImage(final byte image[]) {
		this.changed = true;
		setImage0(image);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final byte packedData[]) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
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

	/**
	 * If return is put prior to finally clause, an underlying gzip output stream is not flushed.
	 * @param data1
	 * @return byte[]
	 * @throws IOException
	 */
	private byte[] pack(final List data1) throws IOException {
		ObjectOutputStream out = null;
		ByteArrayOutputStream subOut = null;
		try {
			subOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(new GZIPOutputStream(subOut));
			out.writeObject(data1);
			out.flush();
		}
		finally {
			if (out != null)
				out.close();
		}
		return subOut.toByteArray();
	}

	/**
	 * @todo Add error-handling mechanism.
	 */
	private byte[] safePack(final List data1) {
		try {
			return pack(data1);
		}
		catch (IOException ioe) {
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
		}
		catch (IOException ioe) {
			Log.errorException(ioe);
		}
		catch (ClassNotFoundException cnfe) {
			Log.errorException(cnfe);
		}
		return Collections.EMPTY_LIST;
	}

	private List unpack(final byte packedData[]) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(packedData)));
			return (List) in.readObject();
		}
		finally {
			if (in != null)
				in.close();
		}
	}
}
