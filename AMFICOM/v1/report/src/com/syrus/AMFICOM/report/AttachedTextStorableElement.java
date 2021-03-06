package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.report.corba.IdlAttachedText;
import com.syrus.AMFICOM.report.corba.IdlAttachedTextHelper;
import com.syrus.AMFICOM.report.corba.IdlAttachedTextPackage.IdlFont;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * <p>Title: </p>
 * <p>Description: ??????? ? ???????????? ????????</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ?????????? ϸ??
 * @version 1.0
 * @module report
 */

public final class AttachedTextStorableElement extends StorableElement
		implements IdlTransferableObjectExt<IdlAttachedText> {
	private static final long serialVersionUID = 276389622206172004L;
	
	private String text;
	/**
	 * ??????? ???????, ? ???????? ???????????? ???????? ?? ?????????
	 */
	private Identifier verticalAttacherId;
	/**
	 * ??????? ???????, ? ???????? ???????????? ???????? ?? ???????????
	 */
	private Identifier horizontalAttacherId;
	/**
	 * ????? ???????
	 */
	private Font font;
	/**
	 * ??? ???????? ?? ?????????
	 */
	private TextAttachingType verticalAttachType;
	/**
	 * ??? ???????? ?? ???????????
	 */
	private TextAttachingType horizontalAttachType;
	/**
	 * ??????? ????? x ??????? ? x ???????, ? ???????? ??? ?????????.
	 */
	private int distanceX;
	/**
	 * ??????? ????? y ??????? ? y ???????, ? ???????? ??? ?????????.
	 */
	private int distanceY;
	
	AttachedTextStorableElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final String text,
			final Identifier verticalAttacherId,
			final Identifier horizontalAttacherId,
			final Font font,
			final TextAttachingType horizontalAttachType,
			final TextAttachingType verticalAttachType,
			final int distanceX,
			final int distanceY) {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId);
		this.text = text;
		this.verticalAttacherId = verticalAttacherId;
		this.horizontalAttacherId = horizontalAttacherId;
		this.font = font;
		this.horizontalAttachType = horizontalAttachType;
		this.verticalAttachType = verticalAttachType;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		
	}
	
	public static AttachedTextStorableElement createInstance(Identifier creatorId, Font font, IntPoint location, IntDimension size) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert location != null : NON_NULL_EXPECTED;
		assert size != null : NON_NULL_EXPECTED;
		assert font != null : NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			final AttachedTextStorableElement atse = new AttachedTextStorableElement(IdentifierPool.getGeneratedIdentifier(ATTACHEDTEXT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					INITIAL_VERSION,
					location,
					size,
					VOID_IDENTIFIER,
					"",
					VOID_IDENTIFIER,
					VOID_IDENTIFIER,
					font,
					TextAttachingType.TO_FIELDS_LEFT,
					TextAttachingType.TO_FIELDS_TOP,
					0,
					0);
			atse.markAsChanged();
			return atse;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"AttachedTextStorableElement.createInstance() | cannot generate identifier ", ige);
		}
	}
	
	public AttachedTextStorableElement(IdlAttachedText transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}
	
	public synchronized void fromIdlTransferable(final IdlAttachedText iat)
	throws IdlConversionException {
		super.fromIdlTransferable(iat);
		this.text = iat.text;
		this.verticalAttacherId = Identifier.valueOf(iat.verticalAttacherId);
		this.horizontalAttacherId = Identifier.valueOf(iat.horizontalAttacherId);
		this.font = new Font(iat.font.name, iat.font.style, iat.font.size);
		this.verticalAttachType = TextAttachingType.fromInt(iat.verticalAttachType);
		this.horizontalAttachType = TextAttachingType.fromInt(iat.horizontalAttachType);
		this.distanceX = iat.distanceX;
		this.distanceY = iat.distanceY;
		
	}
	
	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.StorableObject#getIdlTransferable(ORB)
	 */
	@Override
	public IdlAttachedText getIdlTransferable(final ORB orb) {
		return IdlAttachedTextHelper.init(orb,
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
				this.text,
				this.verticalAttacherId.getIdlTransferable(),
				this.horizontalAttacherId.getIdlTransferable(),
				this.distanceX,
				this.distanceY,
				new IdlFont(this.font.getName(), this.font.getStyle(), this.font.getSize()),
				this.verticalAttachType.intValue(),
				this.horizontalAttachType.intValue());
	}
	
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.reportTemplateId);
		dependencies.add(this.horizontalAttacherId);
		dependencies.add(this.verticalAttacherId);
		return dependencies;
	}
	
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final int locationX,
			final int locationY,
			final int width,
			final int height,
			final Identifier reportTemplateId,
			final String text,
			final Identifier verticalAttacherId,
			final Identifier horizontalAttacherId,
			final int distanceX,
			final int distanceY,
			final Font font,
			final TextAttachingType vType,
			final TextAttachingType hType) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				locationX,
				locationY,
				width,
				height,
				reportTemplateId);
		this.text = text;
		this.verticalAttacherId = verticalAttacherId;
		this.horizontalAttacherId = horizontalAttacherId;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.font = font;
		this.verticalAttachType = vType;
		this.horizontalAttachType = hType;
	}
	
	/**
	 * ?????? ????????.
	 * @param attacher ??????, ? ???????? ?????????????? ????????
	 * @param attachmentType ??? ????????
	 */
	public void setAttachment(
			AbstractDataStorableElement attacher,
			TextAttachingType attachmentType) {
		if (	attachmentType.equals(TextAttachingType.TO_FIELDS_LEFT)		
			||	attachmentType.equals(TextAttachingType.TO_LEFT)
			||	attachmentType.equals(TextAttachingType.TO_WIDTH_CENTER)			
			||	attachmentType.equals(TextAttachingType.TO_RIGHT)){
			this.horizontalAttacherId = (attacher != null) ? attacher.getId() : Identifier.VOID_IDENTIFIER;
			this.horizontalAttachType = attachmentType;
			//????????? ?????????? ?? ??????????????? ???? ???????, ? ????????
			//???????????? ????????
			if (attachmentType.equals(TextAttachingType.TO_FIELDS_LEFT))		
				this.distanceX = this.getX();
			else if (attachmentType.equals(TextAttachingType.TO_LEFT))
				this.distanceX = this.getX() - attacher.getX();
			else if (attachmentType.equals(TextAttachingType.TO_WIDTH_CENTER))
				this.distanceX = this.getX() - (attacher.getX() + attacher.getWidth() / 2);
			else if (attachmentType.equals(TextAttachingType.TO_RIGHT))
				this.distanceX = this.getX() - (attacher.getX() + attacher.getWidth());
		}
		else if (attachmentType.equals(TextAttachingType.TO_FIELDS_TOP)
				|| attachmentType.equals(TextAttachingType.TO_TOP)
				|| attachmentType.equals(TextAttachingType.TO_BOTTOM)) {
			this.verticalAttacherId = (attacher != null) ? attacher.getId() : Identifier.VOID_IDENTIFIER;
			this.verticalAttachType = attachmentType;
			// ????????? ?????????? ?? ??????????????? ???? ???????, ? ????????
			// ???????????? ????????
			if (attachmentType.equals(TextAttachingType.TO_FIELDS_TOP))
				this.distanceY = this.getY();
			else if (attachmentType.equals(TextAttachingType.TO_TOP))
				this.distanceY = this.getY() - attacher.getY();
			else if (attachmentType.equals(TextAttachingType.TO_BOTTOM))
				this.distanceY = this.getY()
						- (attacher.getY() + attacher.getHeight());
		}
		super.markAsChanged();
	}
	
	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
		super.markAsChanged();
	}

	Identifier getHorizontalAttacherId() {
		return this.horizontalAttacherId;
	}
	
	public AbstractDataStorableElement getHorizontalAttacher() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.horizontalAttacherId, true);
	}

	public TextAttachingType getHorizontalAttachType() {
		return this.horizontalAttachType;
	}

	Identifier getVerticalAttacherId() {
		return this.verticalAttacherId;
	}
	
	public AbstractDataStorableElement getVerticalAttacher() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.verticalAttacherId, true);
	}

	public TextAttachingType getVerticalAttachType() {
		return this.verticalAttachType;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		super.markAsChanged();
	}

	/**
	 * ????? ????????????, ????? ???????? ?????????????? ???????? ? ????????
	 * ???, ????? ?? ????????? ?? ???????? ??? ???????? ?????????? ??
	 * ???????? ??????????? ??????, ? ???????? ?? ????????.
	 * @throws ApplicationException 
	 */
	//???? ??????? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
	//???????? ??????? Attacher
	public void suiteAttachingDistances(Rectangle templateBounds) throws ApplicationException {
		int newX = 0;
		int newY = 0;
		AbstractDataStorableElement horizontalAttacher = this.getHorizontalAttacher();
		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			newX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT)) {
			newX = horizontalAttacher.getX() + this.distanceX;
//			//???? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
//			//???????? ??????? Attacher
//			if (newX < templateBounds.x) {
//				newX = templateBounds.x;
//				this.horizAttacher.setLocation(
//						newX - this.distanceX,
//						this.horizAttacher.getY());
//			}
//			else if (newX + this.getWidth() > templateBounds.x + templateBounds.width) {
//				newX = templateBounds.x + templateBounds.width - this.getWidth();
//				this.horizAttacher.setLocation(
//						newX - this.distanceX,
//						this.horizAttacher.getY());
//			}
		}
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_WIDTH_CENTER)) {
			newX = horizontalAttacher.getX() + horizontalAttacher.getWidth() / 2  + this.distanceX;
//			//???? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
//			//???????? ??????? Attacher
//			if (newX < templateBounds.x) {
//				newX = templateBounds.x;
//				this.horizAttacher.setLocation(
//						newX - this.horizAttacher.getWidth() / 2 - this.distanceX,
//						this.horizAttacher.getY());
//			}
//			else if (newX + this.getWidth() > templateBounds.x + templateBounds.width) {
//				newX = templateBounds.x + templateBounds.width - this.getWidth();
//				this.horizAttacher.setLocation(
//						newX - this.horizAttacher.getWidth() / 2 - this.distanceX,
//						this.horizAttacher.getY());
//			}
		}
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT)) {
			newX = horizontalAttacher.getX() + horizontalAttacher.getWidth() + this.distanceX;
//			//???? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
//			//???????? ??????? Attacher
//			if (newX < templateBounds.x) {
//				newX = templateBounds.x;
//				this.horizAttacher.setLocation(
//						newX - this.horizAttacher.getWidth() - this.distanceX,
//						this.horizAttacher.getY());
//			}
//			else if (newX + this.getWidth() > templateBounds.x + templateBounds.width) {
//				newX = templateBounds.x + templateBounds.width - this.getWidth();
//				this.horizAttacher.setLocation(
//						newX - this.horizAttacher.getWidth() - this.distanceX,
//						this.horizAttacher.getY());
//			}
		}
		
		AbstractDataStorableElement verticalAttacher = this.getVerticalAttacher();
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
			newY = this.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP)) {
			newY = verticalAttacher.getY() + this.distanceY;
//			//???? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
//			//???????? ??????? Attacher
//			if (newY < templateBounds.y) {
//				newY = templateBounds.y;
//				this.vertAttacher.setLocation(
//						this.vertAttacher.getX(),
//						newY - this.distanceY);
//			}
//			else if (newY + this.getHeight() > templateBounds.y + templateBounds.height) {
//				newY = templateBounds.y + templateBounds.height - this.getHeight();
//				this.vertAttacher.setLocation(
//						this.vertAttacher.getX(),
//						newY - this.distanceY);
//			}
		}
		else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM)) {
			newY = verticalAttacher.getY() + verticalAttacher.getHeight() + this.distanceY;
//			//???? ??????? ?? ??????? ??????? - ?? ????????? ? ?????????? ??
//			//???????? ??????? Attacher
//			if (newY < templateBounds.y) {
//				newY = templateBounds.y;
//				this.vertAttacher.setLocation(
//						this.vertAttacher.getX(),
//						newY - this.vertAttacher.getHeight() - this.distanceY);
//			}
//			else if (newY + this.getHeight() > templateBounds.y + templateBounds.height) {
//				newY = templateBounds.y + templateBounds.height - this.getHeight();
//				this.vertAttacher.setLocation(
//						this.vertAttacher.getX(),
//						newY - this.vertAttacher.getHeight() - this.distanceY);
//			}
		}
		
		if (newX < templateBounds.x)
			newX = templateBounds.x;
		else if (newX + this.getWidth() > templateBounds.x + templateBounds.width)
			newX = templateBounds.x + templateBounds.width - this.getWidth();

		if (newY < templateBounds.y)
			newY = templateBounds.y;
		else if (newY + this.getHeight() > templateBounds.y + templateBounds.height)
			newY = templateBounds.y + templateBounds.height - this.getHeight();
		
		this.setLocation(newX, newY);
	}
	
	/**
	 * ????? ????????????, ????? ???????? ?????????????? ???????? ? ????????
	 * ???, ????? ?? ????????? ?? ???????? ??? ???????? ?????????? ??
	 * ???????? ??????????? ??????, ? ???????? ?? ????????.
	 * @throws ApplicationException 
	 */
	public void refreshAttachingDistances() throws ApplicationException {
		if (!this.horizontalAttacherId.equals(Identifier.VOID_IDENTIFIER)) {
			AbstractDataStorableElement horizontalAttacher = this.getHorizontalAttacher();			
			if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT)) {
				this.distanceX = this.getX();
			} else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT)) {
				this.distanceX = this.getX() - horizontalAttacher.getX();
			} else if (this.horizontalAttachType.equals(TextAttachingType.TO_WIDTH_CENTER)) {
				this.distanceX = this.getX() - horizontalAttacher.getX()
					- horizontalAttacher.getWidth() / 2;
			} else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT)) {
				this.distanceX = this.getX() - horizontalAttacher.getX()
					- horizontalAttacher.getWidth();			
			}
		}
		
		if (!this.verticalAttacherId.equals(Identifier.VOID_IDENTIFIER)) {		
			AbstractDataStorableElement verticalAttacher = this.getVerticalAttacher();
			if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP)) {
				this.distanceY = this.getY();
			} else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP)) {
				this.distanceY = this.getY() - verticalAttacher.getY();
			} else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM)) {
				this.distanceY = this.getY() - verticalAttacher.getY()
					- verticalAttacher.getHeight();
			}
		}
	}
	
	public int getDistanceX() {
		return this.distanceX;
	}
	
	public int getDistanceY() {
		return this.distanceY;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected AttachedTextWrapper getWrapper() {
		return AttachedTextWrapper.getInstance();
	}
	
	@Override
	protected AttachedTextStorableElement clone() throws CloneNotSupportedException {
		Map clonedIdIdMap = new HashMap();
		AttachedTextStorableElement clone = (AttachedTextStorableElement) super.clone();
		clone.text = this.text;
		clone.distanceX = this.distanceX;
		clone.distanceY = this.distanceY;
		clone.font = new Font(this.font.getName(), this.font.getStyle(), this.font.getSize());
		clone.verticalAttachType = this.verticalAttachType;
		clone.horizontalAttachType = this.horizontalAttachType;
		
		clone.horizontalAttacherId = VOID_IDENTIFIER;
		clone.verticalAttacherId = VOID_IDENTIFIER;
		
		//TODO: fix warnings. Do not use Map. Use simple if
		try {
			AbstractDataStorableElement verticalDataElement = this.getVerticalAttacher();
			if (verticalDataElement != null) {
				AbstractDataStorableElement clonedDataElement = (AbstractDataStorableElement) clonedIdIdMap.get(verticalDataElement);
				if (clonedDataElement == null) {
					clonedDataElement = verticalDataElement.clone();
					clonedIdIdMap.put(verticalDataElement, clonedDataElement);
				}
				clone.verticalAttacherId = clonedDataElement.getId();
				clonedDataElement.setReportTemplateId(clone.reportTemplateId);
			}
			
			AbstractDataStorableElement horizontalDataElement = this.getHorizontalAttacher();
			if (horizontalDataElement != null) {
				AbstractDataStorableElement clonedDataElement = (AbstractDataStorableElement) clonedIdIdMap.get(verticalDataElement);
				if (clonedDataElement == null) {
					clonedDataElement = verticalDataElement.clone();
				}
				clone.horizontalAttacherId = clonedDataElement.getId();
				clonedDataElement.setReportTemplateId(clone.reportTemplateId);
			}
		} catch (ApplicationException e) {
			CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(e);
			throw cnse;
		}
		
		return clone;
	}
}
