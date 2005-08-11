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
	 */
	public void setHorizAttachment(DataRenderingElement hAttacher,
		String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setHorizAttachment | attachmentType can't be null!");
		
		this.horizAttacher = hAttacher;
		this.horizontalAttachType = attachmentType;
	}

	/**
	 * Задаёт привязку по вертикали
	 * @param vAttacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 */
	public void setVertAttachment(DataRenderingElement vAttacher,
		String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setVertAttachment | attachmentType can't be null!");
		
		this.vertAttacher = vAttacher;
		this.verticalAttachType = attachmentType;
	}

//	/**
//	 * Задаёт новые значения distanceX,distanceY.
//	 * Используется при перемещении привязанной надписи относительно
//	 * объекта, к которому она привязана.
//	 */
//	public void setFirmingDistance()
//	{
//		if (horizAttacher == null)
//			distanceX = this.getX();
//		if (vertAttacher == null)
//			distanceY = this.getY();
//
//		int width = 0;
//		int height = 0;
//
//		if (vertAttacher != null)
//		{
//			if (vertAttacher.rendererPanel == null)
//				height = vertAttacher.height;
//			else
//				height = vertAttacher.rendererPanel.getHeight();
//
//			if (verticalAttachTo.equals(TextAttachingType.toFieldsTop))
//				distanceY = this.getY();
//			if (verticalAttachTo.equals(TextAttachingType.toTop))
//				distanceY = this.getY() - vertAttacher.y;
//			if (verticalAttachTo.equals(TextAttachingType.toBottom))
//				distanceY = this.getY() - vertAttacher.y - height;
//		}
//
//		if (horizAttacher != null)
//		{
//			if (horizAttacher.rendererPanel == null)
//				width = horizAttacher.width;
//			else
//				width = horizAttacher.rendererPanel.getWidth();
//
//			if (horizontalAttachTo.equals(TextAttachingType.toFieldsLeft))
//				distanceX = this.getX();
//			if (horizontalAttachTo.equals(TextAttachingType.toLeft))
//				distanceX = this.getX() - horizAttacher.x;
//			if (horizontalAttachTo.equals(TextAttachingType.toRight))
//				distanceX = this.getX() - horizAttacher.x - width;
//		}
//	}
//
//	/**
//	 * Используется при перемещении объекта, к которому привязана
//	 * надпись. Изменяет координаты надписи так, чтобы расстояние
//	 * между надписью и объектом осталось неизменным.
//	 * @param imagableRect поля
//	 */
//	public void refreshCoords(Rectangle imagableRect)
//	{
//		int newX = 0;
//		int newY = 0;
//
//		if (horizAttacher == null)
//			newX = this.getX();
//		if (vertAttacher == null)
//			newY = this.getY();
//
//		if (vertAttacher != null)
//		{
//			int height = 0;
//			int vertFirmerY = 0;
//			if (vertAttacher.rendererPanel == null)
//			{
//				height = vertAttacher.height;
//				vertFirmerY = vertAttacher.y;
//			}
//			else
//			{
//				height = vertAttacher.rendererPanel.getHeight();
//				vertFirmerY = vertAttacher.rendererPanel.getY();
//			}
//
//			if (verticalAttachTo.equals(TextAttachingType.toTop))
//				newY = vertFirmerY + distanceY;
//
//			if (verticalAttachTo.equals(TextAttachingType.toBottom))
//				newY = vertFirmerY + height + distanceY;
//
//			if (imagableRect != null)
//			{
//				if (newY < imagableRect.y)
//					newY = imagableRect.y;
//
//				int tpheight = (int) this.getPreferredSize().getWidth();
//				if (newY + tpheight > imagableRect.y + imagableRect.height)
//					newY = imagableRect.y + imagableRect.height - tpheight;
//			}
//		}
//
//		if (horizAttacher != null)
//		{
//			int width = 0;
//			int horizFirmerX = 0;
//
//			if (horizAttacher.rendererPanel == null)
//			{
//				width = horizAttacher.width;
//				horizFirmerX = horizAttacher.x;
//			}
//			else
//			{
//				width = horizAttacher.rendererPanel.getWidth();
//				horizFirmerX = horizAttacher.rendererPanel.getX();
//			}
//
//			if (horizontalAttachTo.equals(TextAttachingType.toLeft))
//				newX = horizFirmerX + distanceX;
//
//			if (horizontalAttachTo.equals(TextAttachingType.toRight))
//				newX = horizFirmerX + width + distanceX;
//
//			if (imagableRect != null)
//			{
//				if (newX < imagableRect.x)
//					newX = imagableRect.x;
//
//				int tpwidth = (int) this.getPreferredSize().getWidth();
//
//				if (newX + tpwidth > imagableRect.x + imagableRect.width)
//					newX = imagableRect.x + imagableRect.width - tpwidth;
//			}
//		}
//
//		this.setLocation(newX, newY);
//	}

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
}
