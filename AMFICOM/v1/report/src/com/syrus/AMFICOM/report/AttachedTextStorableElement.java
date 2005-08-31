package com.syrus.AMFICOM.report;

import java.awt.Font;
import java.io.IOException;

import com.syrus.AMFICOM.general.Identifier;
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
	private String text = "";
	/**
	 * ������� �������, � �������� ������������ �������� �� ���������
	 */
	private DataStorableElement vertAttacher = null;
	/**
	 * ������� �������, � �������� ������������ �������� �� �����������
	 */
	private DataStorableElement horizAttacher = null;
	/**
	 * ����� �������
	 */
	private Font font = null;
	/**
	 * ��� �������� �� ���������
	 */
	private String verticalAttachType = TextAttachingType.TO_FIELDS_TOP;
	/**
	 * ��� �������� �� �����������
	 */
	private String horizontalAttachType = TextAttachingType.TO_FIELDS_LEFT;
	/**
	 * ������� ����� x ������� � x �������, � �������� ��� ���������.
	 */
	private int distanceX = 0;
	/**
	 * ������� ����� y ������� � y �������, � �������� ��� ���������.
	 */
	private int distanceY = 0;
	
	public AttachedTextStorableElement (IntPoint location)
	{
		this.setLocation(location);
	}
	
	/**
	 * ����� �������� �� �����������
	 * @param hAttacher ������, � �������� �������������� ��������
	 * @param attachmentType ��� ��������
	 */
	public void setHorizontalAttachment(
			DataStorableElement hAttacher,
			String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setHorizAttachment | attachmentType can't be null!");
		
		this.horizAttacher = hAttacher;
		this.horizontalAttachType = attachmentType;
		//��������� ���������� �� ��������������� ���� �������, � ��������
		//������������ ��������
		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))		
			this.distanceX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT))
			this.distanceX = this.getX() - this.horizAttacher.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT))
			this.distanceX = this.getX() - (this.horizAttacher.getX() + this.horizAttacher.getWidth());
	}

	/**
	 * ����� �������� �� ���������
	 * @param vAttacher ������, � �������� �������������� ��������
	 * @param attachmentType ��� ��������
	 */
	public void setVerticalAttachment(
			DataStorableElement vAttacher,
			String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setVertAttachment | attachmentType can't be null!");
		
		this.vertAttacher = vAttacher;
		this.verticalAttachType = attachmentType;
		
		//��������� ���������� �� ��������������� ���� �������, � ��������
		//������������ ��������
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))		
			this.distanceY = this.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP))
			this.distanceY = this.getY() - this.vertAttacher.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM))
			this.distanceY = this.getY() - (this.vertAttacher.getY() + this.vertAttacher.getHeight());
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public DataStorableElement getHorizontalAttacher() {
		return this.horizAttacher;
	}

	public String getHorizontalAttachType() {
		return this.horizontalAttachType;
	}

	public DataStorableElement getVerticalAttacher() {
		return this.vertAttacher;
	}

	public String getVerticalAttachType() {
		return this.verticalAttachType;
	}

//	/**
//	 * @return ���������� �������� ������, ������������ ��������
//	 */
//	public Dimension getContentBounds()
//	{
//		String strToConsider = this.getText();
//		if (strToConsider.equals(""))
//			strToConsider = " ";
//
//		int stringCount = 0;
//
//		int maxStringLength = 0;
//		int mSLstartIndex = 0;
//
//		int stringStart = 0;
//		for (int i = 0; i < strToConsider.length(); i++)
//			if ((strToConsider.charAt(i) == '\n') ||
//				(i == strToConsider.length() - 1))
//			{
//				int curStringLength = i - stringStart;
//				if (i == strToConsider.length() - 1)
//					curStringLength++;
//
//				if (curStringLength > maxStringLength)
//				{
//					maxStringLength = curStringLength;
//					mSLstartIndex = stringStart;
//				}
//				stringCount++;
//				stringStart = i + 1;
//			}
//
//		Dimension result = new Dimension();
//
//		result.height =
//			(this.getFont().getStringBounds(
//			strToConsider,
//			new FontRenderContext(null, true, true))).
//			getBounds().height * stringCount + 3;
//
//		result.width =
//			(this.getFont().getStringBounds(
//			strToConsider.substring(mSLstartIndex,
//			mSLstartIndex + maxStringLength),
//			new FontRenderContext(null, true, true))).
//			getBounds().width + 3;
//
//		return result;
//	}
	
	/**
	 * ������ ����� �� ��������� ��������� Serializable, ��������� ��� ������ �� ������
	 * ��� ��������� ������ DataRenderingElement'�� ��� ��������� attacher'��. ������
	 * ������� �������� ���� ������ ��� ��������.
	 */
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.getSize());
		out.writeObject(this.getLocation());		
		out.writeObject(this.text);
		out.writeObject(this.font);

		out.writeObject(this.verticalAttachType);
		if (!this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
			out.writeObject(this.vertAttacher.getId());

		out.writeObject(this.horizontalAttachType);
		if (!this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			out.writeObject(this.horizAttacher.getId());
	}

	public void readObject(java.io.ObjectInputStream in, ReportTemplate rt) throws IOException, ClassNotFoundException {
		this.setSize((IntDimension)in.readObject());
		this.setLocation((IntPoint)in.readObject());
		
		this.text = (String)in.readObject();
		this.font = (Font)in.readObject();		

		this.verticalAttachType = (String)in.readObject();		
		if (!this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
		{
			Identifier vaId = (Identifier)in.readObject();
			this.vertAttacher = rt.getDataRElement(vaId);
		}

		this.horizontalAttachType = (String)in.readObject();		
		if (!this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
		{
			Identifier haId = (Identifier)in.readObject();
			this.horizAttacher = rt.getDataRElement(haId);
		}
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

	public AttachedTextStorableElement() {
	}
	
	/**
	 * ����� ������������, ����� �������� �������������� �������� � ��������
	 * ���, ����� �� ��������� �� �������� ��� �������� ���������� ��
	 * �������� ����������� ������, � �������� �� ��������.
	 */
	public void suiteAttachingDistances()
	{
		int newX = 0;
		int newY = 0;

		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			newX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT))
			newX = this.horizAttacher.getX() + this.distanceX;
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT))
			newX = this.horizAttacher.getX() + this.horizAttacher.getWidth() + this.distanceX;
		
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
			newY = this.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP))
			newY = this.vertAttacher.getY() + this.distanceY;
		else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM))
			newY = this.vertAttacher.getY() + this.vertAttacher.getHeight() + this.distanceY;

		this.setLocation(newX, newY);
	}

	public void setDistanceX(int distanceX) {
		this.distanceX = distanceX;
	}

	public void setDistanceY(int distanceY) {
		this.distanceY = distanceY;
	}
}
