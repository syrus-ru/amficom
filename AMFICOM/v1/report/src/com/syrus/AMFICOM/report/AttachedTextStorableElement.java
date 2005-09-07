package com.syrus.AMFICOM.report;

import java.awt.Font;
import java.awt.Rectangle;
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
	 * Задаёт привязку.
	 * @param attacher объект, к которому осуществляется привязка
	 * @param attachmentType тип привязки
	 */
	public void setAttachment(
			DataStorableElement attacher,
			String attachmentType)
	{
		if (attachmentType == null)
			throw new AssertionError("AttachedTextRenderingElement.setAttachment | attachmentType can't be null!");
		
		if (	attachmentType.equals(TextAttachingType.TO_FIELDS_LEFT)		
			||	attachmentType.equals(TextAttachingType.TO_LEFT)
			||	attachmentType.equals(TextAttachingType.TO_WIDTH_CENTER)			
			||	attachmentType.equals(TextAttachingType.TO_RIGHT)){
			this.horizAttacher = attacher;
			this.horizontalAttachType = attachmentType;
			//Фиксируем расстояние до соответсвующего края объекта, к которому
			//осуществлена привязка
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
				this.vertAttacher = attacher;
				this.verticalAttachType = attachmentType;
				//Фиксируем расстояние до соответсвующего края объекта, к которому
				//осуществлена привязка
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
	//Если элемент выходит за границы шаблона - не двигаемся и возвращаем на
	//исходную позицию Attacher
	public void suiteAttachingDistances(Rectangle templateBounds) {
		int newX = 0;
		int newY = 0;

		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			newX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT)) {
			newX = this.horizAttacher.getX() + this.distanceX;
//			//Если выходит за границы шаблона - не двигаемся и возвращаем на
//			//исходную позицию Attacher
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
			newX = this.horizAttacher.getX() + this.horizAttacher.getWidth() / 2  + this.distanceX;
//			//Если выходит за границы шаблона - не двигаемся и возвращаем на
//			//исходную позицию Attacher
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
			newX = this.horizAttacher.getX() + this.horizAttacher.getWidth() + this.distanceX;
//			//Если выходит за границы шаблона - не двигаемся и возвращаем на
//			//исходную позицию Attacher
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
			newY = this.vertAttacher.getY() + this.distanceY;
//			//Если выходит за границы шаблона - не двигаемся и возвращаем на
//			//исходную позицию Attacher
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
			newY = this.vertAttacher.getY() + this.vertAttacher.getHeight() + this.distanceY;
//			//Если выходит за границы шаблона - не двигаемся и возвращаем на
//			//исходную позицию Attacher
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
	 * Метод используется, чтобы изменить местоположение элемента с надписью
	 * так, чтобы он находился на заданном при привязке расстоянии от
	 * элемента отображения данных, к которому он привязан.
	 */
	public void refreshAttachingDistances()
	{
		if (this.horizontalAttachType.equals(TextAttachingType.TO_FIELDS_LEFT))
			this.distanceX = this.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_LEFT))
			this.distanceX = this.getX() - this.horizAttacher.getX();
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_WIDTH_CENTER))
			this.distanceX = this.getX() - this.horizAttacher.getX()
				- this.horizAttacher.getWidth() / 2;			
		else if (this.horizontalAttachType.equals(TextAttachingType.TO_RIGHT))
			this.distanceX = this.getX() - this.horizAttacher.getX()
				- this.horizAttacher.getWidth();			
		
		if (this.verticalAttachType.equals(TextAttachingType.TO_FIELDS_TOP))
			this.distanceY = this.getY();			
		else if (this.verticalAttachType.equals(TextAttachingType.TO_TOP))
			this.distanceY = this.getY() - this.vertAttacher.getY();
		else if (this.verticalAttachType.equals(TextAttachingType.TO_BOTTOM))
			this.distanceY = this.getY() - this.vertAttacher.getY()
				- this.vertAttacher.getHeight();
	}
}
