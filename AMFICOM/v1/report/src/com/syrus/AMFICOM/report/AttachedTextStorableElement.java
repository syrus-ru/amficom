package com.syrus.AMFICOM.report;

import java.awt.Font;
import java.io.IOException;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * <p>Title: </p>
 * <p>Description: Надпись с возможностью привязки</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class AttachedTextStorableElement extends StorableElement
{
	private static final long serialVersionUID = 276389622206172004L;
	private String text = "";
	/**
	 * Элемент шаблона, к которому осуществлена привязка по вертикали
	 */
	private DataStorableElement vertAttacher = null;
	/**
	 * Элемент шаблона, к которому осуществлена привязка по горизонтали
	 */
	private DataStorableElement horizAttacher = null;
	/**
	 * Шрифт надписи
	 */
	private Font font = null;
	/**
	 * Тип привязки по вертикали
	 */
	private String verticalAttachType = TextAttachingType.TO_FIELDS_TOP;
	/**
	 * Тип привязки по горизонтали
	 */
	private String horizontalAttachType = TextAttachingType.TO_FIELDS_LEFT;
	/**
	 * Разница между x надписи и x объекта, к которому она привязана.
	 */
	private int distanceX = 0;
	/**
	 * Разница между y надписи и y объекта, к которому она привязана.
	 */
	private int distanceY = 0;
	
	public AttachedTextStorableElement (IntPoint location)
	{
		this.setLocation(location);
	}
	
	/**
	 * Задаёт привязку по горизонтали
	 * @param hAttacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 */
	public void setHorizontalAttachment(
			DataStorableElement hAttacher,
			String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setHorizAttachment | attachmentType can't be null!");
		
		this.horizAttacher = hAttacher;
		this.horizontalAttachType = attachmentType;
		//Фиксируем расстояние до соответсвующего края объекта, к которому
		//осуществлена привязка
		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))		
			this.distanceX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT))
			this.distanceX = this.getX() - this.horizAttacher.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT))
			this.distanceX = this.getX() - (this.horizAttacher.getX() + this.horizAttacher.getWidth());
	}

	/**
	 * Задаёт привязку по вертикали
	 * @param vAttacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 */
	public void setVerticalAttachment(
			DataStorableElement vAttacher,
			String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setVertAttachment | attachmentType can't be null!");
		
		this.vertAttacher = vAttacher;
		this.verticalAttachType = attachmentType;
		
		//Фиксируем расстояние до соответсвующего края объекта, к которому
		//осуществлена привязка
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
//	 * @return Возвращает габариты строки, отображаемой надписью
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
	 * Данный класс НЕ выполняет интерфейс Serializable, поскольку при чтении из потока
	 * ему необходим список DataRenderingElement'ов для установки attacher'ов. Данные
	 * функции вынесены сюда просто для удобства.
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
	 * Метод используется, чтобы изменить местоположение элемента с надписью
	 * так, чтобы он находился на заданном при привязке расстоянии от
	 * элемента отображения данных, к которому он привязан.
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
