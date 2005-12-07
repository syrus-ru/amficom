/*-
 * $Id: SchemeImageResource.java,v 1.43 2005/12/07 17:17:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.43 $, $Date: 2005/12/07 17:17:17 $
 * @module resource
 */
public final class SchemeImageResource extends AbstractCloneableImageResource {
	private static final long serialVersionUID = -5633433107083921318L;

	private byte image[];

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException
	 */
	public SchemeImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	/**
	 * There's no formal parameter for {@link #image}; this property has to
	 * be set explicitly via {@link #setData(List)} or
	 * {@link #setData0(List)}.
	 */
	SchemeImageResource(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.image = new byte[0];
	}

	/**
	 * There's no formal parameter for {@link #image}; this property has to
	 * be set explicitly via {@link #setData(List)} or
	 * {@link #setData0(List)}.
	 */
	public static SchemeImageResource createInstance(final Identifier creatorId) throws CreateObjectException {
		try {
			final SchemeImageResource schemeImageResource = new SchemeImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION);

			assert schemeImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			schemeImageResource.markAsChanged();

			return schemeImageResource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Common (both client-side and server-side) accessor for {@link #image}
	 * property.
	 */
	public List getData() {
		return Collections.unmodifiableList(this.safeUnpack(this.image));
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
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.image(ImageResourceSort.SCHEME, this.image);
		return IdlImageResourceHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				imageResourceData);
	}

	/**
	 * Client-side modifier for {@link #image} property, which increments
	 * entity version on every modification.
	 */
	public void setData(final List<Object> data) {
		setData0(data);
		super.markAsChanged();
	}

	public void setImage(final byte image[]) {
		setImage0(image);
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final byte packedData[]) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.image = packedData;
	}

	/**
	 * Server-side modifier for {@link #image} property, which doesn't honor
	 * version information.
	 */
	protected void setData0(final List<Object> data) {
		this.image = this.safePack(data);
	}

	protected void setImage0(final byte image[]) {
		assert image != null : ErrorMessages.NON_NULL_EXPECTED;
		this.image = image;
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
			out = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(subOut)));
			out.writeObject(data1);
			out.flush();
		} finally {
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
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		}
		return new byte[0];
	}

	/**
	 * @todo Add error-handling mechanism.
	 */
	private List<Object> safeUnpack(final byte packedData[]) {
		try {
			return unpack(packedData);
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage(cnfe);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private List<Object> unpack(final byte packedData[]) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(packedData))));
			return (List) in.readObject();
		} finally {
			if (in != null)
				in.close();
		}
	}

	@Override
	ImageResourceSort getSort() {
		return ImageResourceSort.SCHEME;
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlImageResource idlImageResource = (IdlImageResource) transferable;
		super.fromTransferable(idlImageResource);

		final IdlImageResourceData imageResourceData = idlImageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._SCHEME;
		this.image = imageResourceData.image();
	}

	@Override
	public SchemeImageResource clone() throws CloneNotSupportedException {
		final SchemeImageResource clone = (SchemeImageResource) super.clone();

		if (clone.clonedIdMap == null) {
			clone.clonedIdMap = new HashMap<Identifier, Identifier>();
		}

		clone.clonedIdMap.put(this.id, clone.id);

		clone.image = this.image.clone();
		return clone;
	}
}
