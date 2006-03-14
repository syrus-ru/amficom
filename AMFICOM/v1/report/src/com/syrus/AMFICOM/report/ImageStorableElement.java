package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTIMAGE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.omg.CORBA.ORB;

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
import com.syrus.AMFICOM.report.corba.IdlImageHelper;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * <p>Title: </p>
 * <p>Description: ������ ��� ����������� GIF � JPG</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 * @module report
 */

public final class ImageStorableElement
		extends StorableElement {

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
	
	ImageStorableElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final byte[] image) throws CreateObjectException {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId);
		this.bitmapImageResource  = BitmapImageResource.createInstance(creatorId , "ILLEGAL_CODENAME", image);
		this.bitmapImageResource.setCodename(this.bitmapImageResource.getId().getIdentifierString());
		this.bitmapImageResourceId = this.bitmapImageResource.getId();
	}
	
	public static ImageStorableElement createInstance(
			Identifier creatorId,
			BufferedImage image,
			IntDimension size,
			IntPoint location) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert image != null : NON_NULL_EXPECTED;
		assert location != null : NON_NULL_EXPECTED;
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//TODO: thing about it.
			ImageIO.write(image, "bmp", baos);
			final Date created = new Date();
			ImageStorableElement reportImage = new ImageStorableElement(
					IdentifierPool.getGeneratedIdentifier(REPORTIMAGE_CODE),
					created,
					created,
					creatorId,
					creatorId,
					INITIAL_VERSION,
					location,
					size,
					VOID_IDENTIFIER,
					baos.toByteArray());
			reportImage.markAsChanged();
			return reportImage;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"ImageStorableElement.createInstance() | cannot generate identifier ", ige);
		} catch (final IOException ioe) {
			throw new CreateObjectException(
					"ImageStorableElement.createInstance() | stream error ", ioe);
		}
	}
	
	public ImageStorableElement(IdlImage transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}		
	}
	
	@Override
	protected synchronized void fromIdlTransferable(IdlStorableObject transferable)
	throws IdlConversionException {
		IdlImage idlImage = (IdlImage) transferable;
		super.fromIdlTransferable(idlImage);
		this.bitmapImageResourceId = new Identifier(idlImage.bitmapImageResource);
	}
	
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.bitmapImageResourceId);
		return dependencies;
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
	
	@Override
	public IdlStorableObject getIdlTransferable(ORB orb) {
		return IdlImageHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.location.getX(),
				this.location.getY(),
				this.size.getWidth(),
				this.size.getHeight(),
				this.reportTemplateId.getIdlTransferable(),
				this.bitmapImageResourceId.getIdlTransferable());
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
		super.markAsChanged();
	}

	public BufferedImage getBufferedImage() throws ApplicationException, IOException {
		return ImageIO.read(new ByteArrayInputStream(getBitmapImageResource().getImage()));
	}
	
	public void setBufferedImage(BufferedImage image) throws ApplicationException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// TODO: thing about it.
		ImageIO.write(image, "bmp", baos);
		getBitmapImageResource().setImage(baos.toByteArray());
		super.markAsChanged();
	}
	
	Identifier getBitmapImageResourceId() {
		return this.bitmapImageResourceId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ImageWrapper getWrapper() {
		return ImageWrapper.getInstance();
	}
	
	@Override
	protected ImageStorableElement clone() throws CloneNotSupportedException {
		ImageStorableElement clone = (ImageStorableElement) super.clone();
		try {
			BitmapImageResource image = this.getBitmapImageResource();
			BitmapImageResource imageClone = image.clone();
			clone.bitmapImageResourceId = imageClone.getId();
		} catch (ApplicationException e) {
			CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(e);
			throw cnse;
		}	
		
		return clone;
		
	}
}
