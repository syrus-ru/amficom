package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.report.corba.IdlImage;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * <p>Title: </p>
 * <p>Description: Панель для отображения GIF и JPG</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class ImageStorableElement extends StorableElement {

	private static final long serialVersionUID = 336147260496995306L;
	private Identifier bitmapImageResourceId;
	private BitmapImageResource bitmapImageResource;
	
	ImageStorableElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final Identifier bitmapImageResourceId) {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId);
		this.bitmapImageResourceId = bitmapImageResourceId;
	}
	
	public ImageStorableElement createInstance(Identifier creatorId, byte[] image) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert image != null : NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			Identifier imageId = IdentifierPool.getGeneratedIdentifier(IMAGERESOURCE_CODE);
			this.bitmapImageResource  = BitmapImageResource.createInstance(imageId, imageId.toString(), image);
			return new ImageStorableElement(
					IdentifierPool.getGeneratedIdentifier(REPORTDATA_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					new IntPoint(),
					new IntDimension(),
					VOID_IDENTIFIER,
					imageId);
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"ImageStorableElement.createInstance() | cannot generate identifier ", ige);
		}
	}
	
	public ImageStorableElement(IdlImage transferable) {
		fromTransferable(transferable);		
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) {
		IdlImage idlImage = (IdlImage) transferable;
		try {
			super.fromTransferable(idlImage);
		} catch (ApplicationException e) {
			// Never can happen
			assert false;
		}
		this.bitmapImageResourceId = new Identifier(idlImage.bitmapImageResource);
	}
	
	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.bitmapImageResourceId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}
	
	synchronized void setAttributes(Date created, 
			Date modified, 
			Identifier creatorId, 
			Identifier modifierId, 
			StorableObjectVersion version,
			final int locationX, 
			final int locationY, 
			final int width, 
			final int height,
			final Identifier reportTemplateId,
			final Identifier bitmapImageResourceId) {
		super.setAttributes(created, modified, creatorId, modifierId, version,
				locationX, locationY, width, height, reportTemplateId);
		this.bitmapImageResourceId = bitmapImageResourceId;
		
	}
	
	private BitmapImageResource getBitmapImageResource() throws ApplicationException {
		if(this.bitmapImageResource == null) {
			this.bitmapImageResource = StorableObjectPool.getStorableObject(this.bitmapImageResourceId, true);
		}
		return this.bitmapImageResource;
	}
	
	public byte[] getImage() throws ApplicationException {
		return getBitmapImageResource().getImage();
	}
	
	public void setImage(byte[] image) throws ApplicationException {
		getBitmapImageResource().setImage(image);
	}
	
	Identifier getBitmapImageResourceId() {
		return this.bitmapImageResourceId;
	}
}
