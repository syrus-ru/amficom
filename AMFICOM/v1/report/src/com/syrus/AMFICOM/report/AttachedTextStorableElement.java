package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.report.corba.IdlAttachedText;
import com.syrus.AMFICOM.report.corba.IdlAttachedTextHelper;
import com.syrus.AMFICOM.report.corba.IdlAttachedTextPackage.IdlFont;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * <p>Title: </p>
 * <p>Description: ������� � ������������ ��������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public final class AttachedTextStorableElement extends StorableElement
{
	private static final long serialVersionUID = 276389622206172004L;
	
	private String text;
	/**
	 * ������� �������, � �������� ������������ �������� �� ���������
	 */
	private Identifier verticalAttacherId;
	/**
	 * ������� �������, � �������� ������������ �������� �� �����������
	 */
	private Identifier horizontalAttacherId;
	/**
	 * ����� �������
	 */
	private Font font;
	/**
	 * ��� �������� �� ���������
	 */
	private TextAttachingType verticalAttachType;
	/**
	 * ��� �������� �� �����������
	 */
	private TextAttachingType horizontalAttachType;
	/**
	 * ������� ����� x ������� � x �������, � �������� ��� ���������.
	 */
	private int distanceX;
	/**
	 * ������� ����� y ������� � y �������, � �������� ��� ���������.
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
	
	public static AttachedTextStorableElement createInstance(Identifier creatorId, IntPoint location) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert location != null : NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final AttachedTextStorableElement atse = new AttachedTextStorableElement(IdentifierPool.getGeneratedIdentifier(ATTACHEDTEXT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					location,
					new IntDimension(),
					VOID_IDENTIFIER,
					"",
					VOID_IDENTIFIER,
					VOID_IDENTIFIER,
					// TODO: use real font
					new Font("qwe", 0, 0),
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
	
	public AttachedTextStorableElement(IdlAttachedText transferable) {
		this.fromTransferable(transferable);
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) {
		IdlAttachedText iat = (IdlAttachedText) transferable;
		try {
			super.fromTransferable(transferable);
		} catch (ApplicationException e) {
			// this shit cann't happen
			assert false;
		}
		this.text = iat.text;
		this.verticalAttacherId = new Identifier(iat.verticalAttacherId);
		this.horizontalAttacherId = new Identifier(iat.horizontalAttacherId);
		this.font = new Font(iat.font.name, iat.font.style, iat.font.size);
		this.verticalAttachType = TextAttachingType.fromInt(iat.verticalAttachType);
		this.horizontalAttachType = TextAttachingType.fromInt(iat.horizontalAttachType);
		this.distanceX = iat.distanceX;
		this.distanceY = iat.distanceY;
		
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlStorableObject getTransferable(ORB orb) {
		return IdlAttachedTextHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.location.getX(),
				this.location.getY(),
				this.size.getWidth(),
				this.size.getHeight(),
				this.reportTemplateId.getTransferable(),
				this.text,
				this.verticalAttacherId.getTransferable(),
				this.horizontalAttacherId.getTransferable(),
				this.distanceX,
				this.distanceY,
				new IdlFont(this.font.getName(), this.font.getStyle(), this.font.getSize()),
				this.verticalAttachType.intValue(),
				this.horizontalAttachType.intValue());
	}
	
	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.horizontalAttacherId);
		dependencies.add(this.verticalAttacherId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
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
	 * ����� ��������.
	 * @param attacher ������, � �������� �������������� ��������
	 * @param attachmentType ��� ��������
	 */
	public void setAttachment(
			DataStorableElement attacher,
			TextAttachingType attachmentType)
	{
		assert attacher != null : NON_NULL_EXPECTED;
		
		if (	attachmentType.equals(TextAttachingType.TO_FIELDS_LEFT)		
			||	attachmentType.equals(TextAttachingType.TO_LEFT)
			||	attachmentType.equals(TextAttachingType.TO_WIDTH_CENTER)			
			||	attachmentType.equals(TextAttachingType.TO_RIGHT)){
			this.horizontalAttacherId = attacher.getId();
			this.horizontalAttachType = attachmentType;
			//��������� ���������� �� ��������������� ���� �������, � ��������
			//������������ ��������
			if (attachmentType.equals(TextAttachingType.TO_FIELDS_LEFT))		
				this.distanceX = this.getX();
			else if (attachmentType.equals(TextAttachingType.TO_LEFT))
				this.distanceX = this.getX() - attacher.getX();
			else if (attachmentType.equals(TextAttachingType.TO_WIDTH_CENTER))
				this.distanceX = this.getX() - (attacher.getX() + attacher.getWidth() / 2);
			else if (attachmentType.equals(TextAttachingType.TO_RIGHT))
				this.distanceX = this.getX() - (attacher.getX() + attacher.getWidth());
		}
		else if (	attachmentType.equals(TextAttachingType.TO_FIELDS_TOP)		
				||	attachmentType.equals(TextAttachingType.TO_TOP)
				||	attachmentType.equals(TextAttachingType.TO_BOTTOM)){
				this.verticalAttacherId = attacher.getId();
				this.verticalAttachType = attachmentType;
				//��������� ���������� �� ��������������� ���� �������, � ��������
				//������������ ��������
				if (attachmentType.equals(TextAttachingType.TO_FIELDS_TOP))		
					this.distanceY = this.getY();
				else if (attachmentType.equals(TextAttachingType.TO_TOP))
					this.distanceY = this.getY() - attacher.getY();
				else if (attachmentType.equals(TextAttachingType.TO_BOTTOM))
					this.distanceY = this.getY() - (attacher.getY() + attacher.getHeight());
			}
	}
	
	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	Identifier getHorizontalAttacherId() {
		return this.horizontalAttacherId;
	}
	
	public DataStorableElement getHorizontalAttacher() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.horizontalAttacherId, true);
	}

	public TextAttachingType getHorizontalAttachType() {
		return this.horizontalAttachType;
	}

	Identifier getVerticalAttacherId() {
		return this.verticalAttacherId;
	}
	
	public DataStorableElement getVerticalAttacher() throws ApplicationException {
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
	}

	/**
	 * @param text
	 */
	public AttachedTextStorableElement(String text) {
		this.text = text;
	}

	/**
	 * ����� ������������, ����� �������� �������������� �������� � ��������
	 * ���, ����� �� ��������� �� �������� ��� �������� ���������� ��
	 * �������� ����������� ������, � �������� �� ��������.
	 * @throws ApplicationException 
	 */
	//���� ������� ������� �� ������� ������� - �� ��������� � ���������� ��
	//�������� ������� Attacher
	public void suiteAttachingDistances(Rectangle templateBounds) throws ApplicationException {
		int newX = 0;
		int newY = 0;
		DataStorableElement horizontalAttacher = this.getHorizontalAttacher();
		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			newX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT)) {
			newX = horizontalAttacher.getX() + this.distanceX;
//			//���� ������� �� ������� ������� - �� ��������� � ���������� ��
//			//�������� ������� Attacher
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
//			//���� ������� �� ������� ������� - �� ��������� � ���������� ��
//			//�������� ������� Attacher
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
//			//���� ������� �� ������� ������� - �� ��������� � ���������� ��
//			//�������� ������� Attacher
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
		
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
			newY = this.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP)) {
			newY = horizontalAttacher.getY() + this.distanceY;
//			//���� ������� �� ������� ������� - �� ��������� � ���������� ��
//			//�������� ������� Attacher
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
			newY = horizontalAttacher.getY() + horizontalAttacher.getHeight() + this.distanceY;
//			//���� ������� �� ������� ������� - �� ��������� � ���������� ��
//			//�������� ������� Attacher
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
	 * ����� ������������, ����� �������� �������������� �������� � ��������
	 * ���, ����� �� ��������� �� �������� ��� �������� ���������� ��
	 * �������� ����������� ������, � �������� �� ��������.
	 * @throws ApplicationException 
	 */
	public void refreshAttachingDistances() throws ApplicationException {
		DataStorableElement horizontalAttacher = this.getHorizontalAttacher();
		DataStorableElement verticalAttacher = this.getVerticalAttacher();
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
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP)) {
			this.distanceY = this.getY();
		} else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP)) {
			this.distanceY = this.getY() - verticalAttacher.getY();
		} else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM)) {
			this.distanceY = this.getY() - verticalAttacher.getY()
				- verticalAttacher.getHeight();
		}
	}
	
	public int getDistanceX() {
		return this.distanceX;
	}
	
	public int getDistanceY() {
		return this.distanceY;
	}
}
