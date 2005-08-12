package com.syrus.AMFICOM.report;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import com.syrus.AMFICOM.general.Identifier;

/**
 * <p>Title: </p>
 * <p>Description: Надпись с возможностью привязки</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class AttachedTextRenderingElement extends RenderingElement
{
	private static final long serialVersionUID = 276389622206172004L;
	private String text = "";
	/**
	 * Элемент шаблона, к которому осуществлена привязка по вертикали
	 */
	private DataRenderingElement vertAttacher = null;
	/**
	 * Элемент шаблона, к которому осуществлена привязка по горизонтали
	 */
	private DataRenderingElement horizAttacher = null;
	/**
	 * Шрифт надписи
	 */
	private Font font = null;
	/**
	 * Тип привязки по вертикали
	 */
	private String verticalAttachType = TextAttachingType.toFieldsTop;
	/**
	 * Тип привязки по горизонтали
	 */
	private String horizontalAttachType = TextAttachingType.toFieldsLeft;
	/**
	 * Разница между x надписи и x объекта, к которому она привязана.
	 */
	private int distanceX = 0;
	/**
	 * Разница между y надписи и y объекта, к которому она привязана.
	 */
	private int distanceY = 0;
	
	public AttachedTextRenderingElement (Point location)
	{
		this.setLocation(location);
	}
	
	/**
	 * Задаёт привязку по горизонтали
	 * @param hAttacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 * @param distance расстояние до элемента отображения данных, к которому
	 * осуществляется привязка.
	 */
	public void setHorizAttachment(
			DataRenderingElement hAttacher,
			String attachmentType,
			int distance)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setHorizAttachment | attachmentType can't be null!");
		
		this.horizAttacher = hAttacher;
		this.horizontalAttachType = attachmentType;
		this.distanceX = distance;
	}

	/**
	 * Задаёт привязку по вертикали
	 * @param vAttacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 * @param distance расстояние до элемента отображения данных, к которому
	 * осуществляется привязка.
	 */
	public void setVertAttachment(
			DataRenderingElement vAttacher,
			String attachmentType,
			int distance)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setVertAttachment | attachmentType can't be null!");
		
		this.vertAttacher = vAttacher;
		this.verticalAttachType = attachmentType;
		this.distanceY = distance;		
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public DataRenderingElement getHorizAttacher() {
		return this.horizAttacher;
	}

	public String getHorizontalAttachType() {
		return this.horizontalAttachType;
	}

	public DataRenderingElement getVertAttacher() {
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
		out.writeObject(this.getBounds());
		out.writeObject(this.text);
		out.writeObject(this.font);

		out.writeObject(this.verticalAttachType);
		if (!this.verticalAttachType.equals(TextAttachingType.toFieldsTop))
			out.writeObject(this.vertAttacher.getId());

		out.writeObject(this.horizontalAttachType);
		if (!this.horizontalAttachType.equals(TextAttachingType.toFieldsLeft))
			out.writeObject(this.horizAttacher.getId());
	}

	public void readObject(java.io.ObjectInputStream in, ReportTemplate rt) throws IOException, ClassNotFoundException {
		this.setBounds((Rectangle)in.readObject());
		
		this.text = (String)in.readObject();
		this.font = (Font)in.readObject();		

		this.verticalAttachType = (String)in.readObject();		
		if (!this.verticalAttachType.equals(TextAttachingType.toFieldsTop))
		{
			Identifier vaId = (Identifier)in.readObject();
			this.vertAttacher = rt.getDataRElement(vaId);
		}

		this.horizontalAttachType = (String)in.readObject();		
		if (!this.horizontalAttachType.equals(TextAttachingType.toFieldsLeft))
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
	public AttachedTextRenderingElement(String text) {
		this.text = text;
	}

	public AttachedTextRenderingElement() {
	}
	
	/**
	 * Метод используется, чтобы изменить местоположение элемента с надписью
	 * так, чтобы он находился на заданном при привязке расстоянии от
	 * элемента отображения данных, к которому он привязан.
	 */
	public void setAttachingDistances()
	{
		int newX = 0;
		int newY = 0;

		if (this.horizontalAttachType.equals(TextAttachingType.toFieldsLeft))
			newX = this.x;
		else if (this.horizontalAttachType.equals(TextAttachingType.toLeft))
			newX = this.horizAttacher.x + this.distanceX;
		else if (this.horizontalAttachType.equals(TextAttachingType.toRight))
			newX = this.horizAttacher.x + this.horizAttacher.width + this.distanceX;
		
		if (this.verticalAttachType.equals(TextAttachingType.toFieldsTop))
			newX = this.x;
		else if (this.verticalAttachType.equals(TextAttachingType.toTop))
			newX = this.vertAttacher.y + this.distanceY;
		else if (this.verticalAttachType.equals(TextAttachingType.toBottom))
			newX = this.vertAttacher.y + this.vertAttacher.height + this.distanceY;

		this.setLocation(newX, newY);
	}

	public void setDistanceX(int distanceX) {
		this.distanceX = distanceX;
	}

	public void setDistanceY(int distanceY) {
		this.distanceY = distanceY;
	}
}
