/*
 * $Id: ReportTemplate.java,v 1.2 2005/08/12 10:21:47 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.report;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;

/**
 * <p>Класс шаблона отчёта - включает в себя списки элементов, надписей и
 * фильтров, используемых в нём, а также средства их сериализации и подготовки
 * к передаче по CORBA. Также включает ряд служебных функций.</p>
 * 
 * <p>Тип шаблона характеризует из какого модуля по нему можно построить
 * отчёт </p>
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/08/12 10:21:47 $
 * @module generalclient_v1
 */
public class ReportTemplate implements Serializable
{
	private static final long serialVersionUID = 6270406142449624592L;
	
	public static final Dimension A4 = new Dimension(827,1170);
	public static final Dimension A3 = new Dimension(1170,1655);
	
	public static final int STANDART_MARGIN_SIZE = 60;

	//Это хранимое поле
	private Identifier id = null;
	//Это хранимое поле	
	private String name = "";
	//Это хранимое поле	
	private String description = "";

	/**
	 * Время сохранения
	 */
	private long savedTime = 0L;
	/**
	 * Время последнего изменения
	 */
	private long modifiedTime = 0L;

	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private Dimension size = A4;

	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private int marginSize = ReportTemplate.STANDART_MARGIN_SIZE;
	
	//Это хранимое поле
	/**
	 * Тип шаблона (принадлежность шаблона к модулю)
	 */
	private String templateType = "";

	//Это хранимое поле	
	/**
	 * Список всех элементов шаблона
	 */
	private List<DataRenderingElement> dataRenderers = new ArrayList<DataRenderingElement>();
	//Это хранимое поле	
	/**
	 * Список фильтров использующихся в шаблоне
	 */
	private List objectResourceFilters = new ArrayList();
	//Это хранимое поле	
	/**
	 * Список надписей из шаблона
	 */
	private List<AttachedTextRenderingElement> labels = new ArrayList<AttachedTextRenderingElement>();
	//Это хранимое поле	
	/**
	 * Список картинок из шаблона
	 */
	private List<ImageRenderingElement> images = new ArrayList<ImageRenderingElement>();

	public boolean isModified()
	{
		return (this.savedTime != this.modifiedTime);
	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException
	{
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.description);
		out.writeObject(this.templateType);
		out.writeObject(this.size);
		out.writeInt(this.marginSize);		

		// Перекачиваем элементы отображения данных
		out.writeInt(this.dataRenderers.size());
		for (DataRenderingElement curRO : this.dataRenderers)
		{
			//TODO просто проверить.
			out.writeObject(curRO);
		}

		// Перекачиваем элементы отображения надписей
		out.writeInt(this.labels.size());
		for (AttachedTextRenderingElement curLabel : this.labels)
			curLabel.writeObject(out);

		// Перекачиваем элементы отображения изображений
		out.writeInt(this.images.size());
		for (ImageRenderingElement curImage : this.images)
			out.writeObject(curImage);

//		// Перекачиваем фильтры
//		int filtersCount = this.objectResourceFilters.size();		
//		out.writeInt(filtersCount);
//		for (int i = 0; i < filtersCount; i++)
//		{
//			ObjectResourceFilter curFilter =
//				(ObjectResourceFilter) this.objectResourceFilters.get(i);
//
//			out.writeObject(curFilter.resource_typ);
//			out.writeObject(curFilter.logicScheme);
//		}
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		this.id = (Identifier) in.readObject();
		this.name = (String)in.readObject();
		this.description = (String)in.readObject();
		this.templateType = (String)in.readObject();
		this.size = (Dimension)in.readObject();
		this.marginSize = in.readInt();

		// Перекачиваем объекты
		this.dataRenderers.clear();

		int orCount = in.readInt();
		for (int i = 0; i < orCount; i++)
		{
			DataRenderingElement curRO = (DataRenderingElement)in.readObject();
			this.dataRenderers.add(curRO);
		}

		// Перекачиваем надписи
		this.labels.clear();

		int labelCount = in.readInt();
		for (int i = 0; i < labelCount; i++)
		{
			AttachedTextRenderingElement curLabel = new AttachedTextRenderingElement();
			curLabel.readObject(in,this);
			this.labels.add(curLabel);
		}

		// Перекачиваем картинки
		this.images.clear();

		int imagesCount = in.readInt();
		for (int i = 0; i < imagesCount; i++)
		{
			ImageRenderingElement curImage = (ImageRenderingElement)in.readObject();
			this.images.add(curImage);
		}

//		// Перекачиваем фильтры
//		this.objectResourceFilters = new ArrayList();
//
//		int filtersCount = in.readInt();
//		for (int i = 0; i < filtersCount; i++)
//		{
//			String resource_typ = (String) in.readObject();
//			try
//			{
//				ObjectResourceFilter curFilter =
//					(ObjectResourceFilter) Class.forName(resource_typ).newInstance();
//
//				curFilter.logicScheme.readObject(in);
//
//				this.objectResourceFilters.add(curFilter);
//			}
//			catch (Exception exc)
//			{
//				System.out.println("Error recreating Filter object!!!");
//			}
//		}
	}

	/**
	 * @param rteName искомое имя
	 * @return элемент шаблона, отображающий искомый отчёт
	 */
	public DataRenderingElement findROforName (String rteName)
	{
		for (DataRenderingElement curRO : this.dataRenderers)
			if (curRO.getReportName().equals(rteName))
				return curRO;

		return null;
	}

	/**
	 * @param dre Объект отображения данных
	 * @return Список надписей, привязанных к данному объекту отображения
	 */
	public List<AttachedTextRenderingElement> getAttachedLabels (DataRenderingElement dre)
	{
		List<AttachedTextRenderingElement> result =
			new ArrayList<AttachedTextRenderingElement>();
		
		for (AttachedTextRenderingElement label : this.labels)
			if (	label.getVertAttacher().equals(dre)
				||	label.getHorizAttacher().equals(dre))
				result.add(label);

		return result;
	}
	
	/**
	 * @return Возвращает границы прямоугольника, в который
	 * вписывается схематичное изображение элемента шаблона и
	 * привязанные к нему надписи.
	 */
	public Rectangle getElementClasterBounds(DataRenderingElement rteRElem)
	{
		if (rteRElem == null)
			throw new AssertionError("The claster bounds can't be calculated for the null element!");
		
		int x1 = rteRElem.getLocation().x;
		int x2 = x1 + rteRElem.getSize().width;		
		int y1 = rteRElem.getLocation().y;
		int y2 = y1 + rteRElem.getSize().height;

		if (this.labels == null)
			return new Rectangle(x1,y1,x2 - x1,y2 - y1);
		
		for (AttachedTextRenderingElement curLabel : this.labels)
		{
			DataRenderingElement vertAttacher = curLabel.getVertAttacher();
			DataRenderingElement horizAttacher = curLabel.getHorizAttacher();			
			if (!		(	(vertAttacher != null)
						&&	vertAttacher.equals(rteRElem)
					||	(horizAttacher != null)
						&&	horizAttacher.equals(rteRElem)))
				continue;

			int labelX = curLabel.getLocation().x;
			int labelY = curLabel.getLocation().y;
			int labelWidth = curLabel.getSize().width;
			int labelHeight = curLabel.getSize().height;
			
			if (labelX < x1)
				x1 = labelX;

			if (labelX + labelWidth > x2)
				x2 = labelX + labelWidth;

			if (labelY < y1)
				y1 = labelY;

			if (labelY + labelHeight > y2)
				y2 = labelY + labelHeight;
		}

		return new Rectangle(x1,y1,x2 - x1,y2 - y1);
	}

	/**
	 * Проверяет: принадлежит ли кластеру (элементу шаблона с привязанными надписями)
	 * данная точка.
	 * @param x координата точки по x
	 * @param y координата точки по y
	 * @return true если точка, принадлежит кластеру
	 */
	public boolean clasterContainsPoint (DataRenderingElement rteRElem,int x, int y)
	{
		Rectangle bounds = this.getElementClasterBounds(rteRElem);
		return bounds.contains(x,y);
	}
	
	public DataRenderingElement getDataRElement(Identifier dreId)
	{
		for (DataRenderingElement ro : this.dataRenderers)
			if (ro.getId().equals (dreId))
				return ro;
		
		return null;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public List<DataRenderingElement> getDataRenderers() {
		return this.dataRenderers;
	}

	public Identifier getId() {
		return this.id;
	}

	public List<ImageRenderingElement> getImages() {
		return this.images;
	}

	public List<AttachedTextRenderingElement> getLabels() {
		return this.labels;
	}

	public String getName() {
		return this.name;
	}

	public List getObjectResourceFilters() {
		return this.objectResourceFilters;
	}

	public Dimension getSize() {
		return this.size;
	}

	public String getTemplateType() {
		return this.templateType;
	}
	
	public ReportTemplate(
			String templateType,
			Dimension size)
	{
		//TODO Здесь должен получаться идентификатор для шаблона отчёта. 
		this.id = new Identifier("");
		this.templateType = templateType;
		this.size = size;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMarginSize() {
		return this.marginSize;
	}

	public void setMarginSize(int marginSize) {
		this.marginSize = marginSize;
	}
}
