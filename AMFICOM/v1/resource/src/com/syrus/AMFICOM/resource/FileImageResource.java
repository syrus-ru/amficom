/*
 * $Id: FileImageResource.java,v 1.30 2005/08/05 16:50:21 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM
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
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.IdlFileImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.30 $, $Date: 2005/08/05 16:50:21 $
 * @module resource_v1
 */
public final class FileImageResource extends AbstractBitmapImageResource {
	private static final long serialVersionUID = -3374486515234713818L;

	private String fileName;
	private String codeName;

	FileImageResource(final Identifier id) throws ApplicationException {
		super(id);
	}

	/**
	 * If given a bad argument, will raise an AssertionError if assertions
	 * enabled, and ::CORBA::BAD_OPERATION otherwise.
	 * @throws CreateObjectException
	 */
	public FileImageResource(final IdlImageResource imageResource) throws CreateObjectException {
		super(imageResource);
	}

	FileImageResource(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codeName,
			final String fileName) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.fileName = fileName;
		this.codeName = codeName;
	}

	public static FileImageResource createInstance(final Identifier creatorId, final String codeName ,final String fileName) throws CreateObjectException {
		try {
			final FileImageResource fileImageResource = new FileImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codeName,
					fileName);

			assert fileImageResource.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			fileImageResource.markAsChanged();

			return fileImageResource;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @see AbstractBitmapImageResource#getCodename()
	 */
	@Override
	public String getCodename() {
		return this.codeName;
	}
	
	
	@Override
	public void setCodename(final String codename) {
		this.setCodeName(codename);
	}
	
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @see AbstractImageResource#getImage()
	 */
	@Override
	public byte[] getImage() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlImageResource getTransferable(final ORB orb) {
		final IdlFileImageResourceData fileImageResourceData = new IdlFileImageResourceData();
		fileImageResourceData.codename = this.codeName;
		fileImageResourceData.fileName = this.fileName;
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.fileImageResourceData(fileImageResourceData);
		return IdlImageResourceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				imageResourceData);
	}

	public void setCodeName(final String codeName) {
		setCodeName0(codeName);
		this.markAsChanged();
	}
	
	public void setFileName(final String fileName) {
		setFileName0(fileName);
		this.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codeName,
			final String fileName) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.fileName = fileName;
		this.codeName = codeName;
	}

	protected void setCodeName0(final String codeName) {
		this.codeName = codeName;
	}
	
	protected void setFileName0(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	ImageResourceSort getSort() {
		return ImageResourceSort.FILE;
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlImageResource idlImageResource = (IdlImageResource) transferable;
		super.fromTransferable(idlImageResource);

		final IdlImageResourceData imageResourceData = idlImageResource.data;
		assert imageResourceData.discriminator().value() == ImageResourceSort._FILE;
		final IdlFileImageResourceData fileImageResourceData = imageResourceData.fileImageResourceData(); 
		this.codeName = fileImageResourceData.codename;
		this.fileName = fileImageResourceData.fileName;
	}
}
