/*
 * $Id: ReportTemplate.java,v 1.8 2005/09/14 14:37:53 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.report;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * <p>Класс шаблона отчёта - включает в себя списки элементов, надписей и
 * фильтров, используемых в нём, а также средства их сериализации и подготовки
 * к передаче по CORBA. Также включает ряд служебных функций.</p>
 * 
 * <p>Тип шаблона характеризует из какого модуля по нему можно построить
 * отчёт </p>
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.8 $, $Date: 2005/09/14 14:37:53 $
 * @module generalclient_v1
 */
public class ReportTemplate extends StorableObject
{
	private static final long serialVersionUID = 6270406142449624592L;
	
	public static final IntDimension A0 = new IntDimension (3360, 4760);
	public static final IntDimension A1 = new IntDimension (3360, 2380);
	public static final IntDimension A2 = new IntDimension (1680, 2380);
	public static final IntDimension A3 = new IntDimension (1680, 1190);
	public static final IntDimension A4 = new IntDimension (840, 1190);

	public enum ORIENTATION {PORTRAIT,LANDSCAPE}
	
	public static final int STANDART_MARGIN_SIZE = 60;

	//Это хранимое поле	
	private String name = "";
	//Это хранимое поле	
	private String description = "";

	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private IntDimension size = A4;
	/**
	 * Размер шаблона (его ширина)
	 */
	private ORIENTATION orientation = ORIENTATION.PORTRAIT;
	//Это хранимое поле
	/**
	 * Размер шаблона (его ширина)
	 */
	private int marginSize = ReportTemplate.STANDART_MARGIN_SIZE;
	
	//Это хранимое поле
	/**
	 * Принадлежность шаблона к модулю
	 */
	private String destinationModule = DestinationModules.UNKNOWN_MODULE;

	//Приходится хранить элементы в разных списках, несмотря на то, что они
	//все наследуют StorableElement, поскольку при загрузке (импорте) важно,
	//чтобы сначала были подгружены элементы хранения данных, а уже потом -
	//надписи к ним привязанные.
	//Это хранимое поле	
	/**
	 * Список всех элементов шаблона
	 */
	private List<DataStorableElement> dataStorableElements = new ArrayList<DataStorableElement>();
//	//Это хранимое поле	
//	/**
//	 * Список фильтров использующихся в шаблоне
//	 */
//	private List objectResourceFilters = new ArrayList();
	//Это хранимое поле	
	/**
	 * Список надписей из шаблона
	 */
	private List<AttachedTextStorableElement> textStorableElements = new ArrayList<AttachedTextStorableElement>();
	//Это хранимое поле	
	/**
	 * Список картинок из шаблона
	 */
	private List<ImageStorableElement> imageStorableElements = new ArrayList<ImageStorableElement>();

	public boolean isModified()	{
		Date modifiedDate = this.getModified();
		if (modifiedDate == null)
			return true;
		
		long templateModified = this.getModified().getTime();
		
		for (StorableElement element : this.dataStorableElements)
			if (element.getModified() > templateModified)
				return true;
		for (StorableElement element : this.textStorableElements)
			if (element.getModified() > templateModified)
				return true;
		for (StorableElement element : this.imageStorableElements)
			if (element.getModified() > templateModified)
				return true;
		
		return false;
	}

	public void refreshModified() {
		Date modifiedDate = this.getModified();
		if (modifiedDate == null)
			return;
		
		long templateModified = modifiedDate.getTime();		
		for (StorableElement element : this.dataStorableElements)
			element.setModified(templateModified);
		for (StorableElement element : this.textStorableElements)
			element.setModified(templateModified);
		for (StorableElement element : this.imageStorableElements)
			element.setModified(templateModified);		
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException
	{
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.description);
		out.writeObject(this.destinationModule);
		out.writeObject(this.size);
		out.writeObject(this.orientation);		
		out.writeInt(this.marginSize);		

		// Перекачиваем элементы отображения данных
		out.writeInt(this.dataStorableElements.size());
		for (DataStorableElement curRO : this.dataStorableElements)
		{
			//TODO просто проверить.
			out.writeObject(curRO);
		}

		// Перекачиваем элементы отображения надписей
		out.writeInt(this.textStorableElements.size());
		for (AttachedTextStorableElement curLabel : this.textStorableElements)
			curLabel.writeObject(out);

		// Перекачиваем элементы отображения изображений
		out.writeInt(this.imageStorableElements.size());
		for (ImageStorableElement curImage : this.imageStorableElements)
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
		this.destinationModule = (String)in.readObject();
		this.size = (IntDimension)in.readObject();
		this.orientation = (ORIENTATION)in.readObject();		
		this.marginSize = in.readInt();

		// Перекачиваем объекты
		this.dataStorableElements.clear();

		int orCount = in.readInt();
		for (int i = 0; i < orCount; i++)
		{
			DataStorableElement curRO = (DataStorableElement)in.readObject();
			this.dataStorableElements.add(curRO);
		}

		// Перекачиваем надписи
		this.textStorableElements.clear();

		int labelCount = in.readInt();
		for (int i = 0; i < labelCount; i++)
		{
			AttachedTextStorableElement curLabel = new AttachedTextStorableElement();
			curLabel.readObject(in,this);
			this.textStorableElements.add(curLabel);
		}

		// Перекачиваем картинки
		this.imageStorableElements.clear();

		int imagesCount = in.readInt();
		for (int i = 0; i < imagesCount; i++)
		{
			ImageStorableElement curImage = (ImageStorableElement)in.readObject();
			this.imageStorableElements.add(curImage);
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
	public DataStorableElement findStorableElementForName (String rteName)
	{
		for (DataStorableElement storableElement : this.dataStorableElements)
			if (storableElement.getReportName().equals(rteName))
				return storableElement;

		return null;
	}

	/**
	 * @param dataStorableElement Объект отображения данных
	 * @return Список надписей, привязанных к данному объекту отображения
	 */
	public List<AttachedTextStorableElement> getAttachedTextStorableElements (DataStorableElement dataStorableElement)
	{
		List<AttachedTextStorableElement> result =
			new ArrayList<AttachedTextStorableElement>();
		
		for (AttachedTextStorableElement storableElement : this.textStorableElements)
			if (	storableElement.getVerticalAttacher().equals(dataStorableElement)
				||	storableElement.getHorizontalAttacher().equals(dataStorableElement))
				result.add(storableElement);

		return result;
	}
	
	/**
	 * @return Возвращает границы прямоугольника, в который
	 * вписывается схематичное изображение элемента шаблона и
	 * привязанные к нему надписи.
	 */
	public Rectangle getElementClasterBounds(DataStorableElement dataStorableElement)
	{
		if (dataStorableElement == null)
			throw new AssertionError("The claster bounds can't be calculated for the null element!");
		
		int x1 = dataStorableElement.getLocation().x;
		int x2 = x1 + dataStorableElement.getWidth();		
		int y1 = dataStorableElement.getLocation().y;
		int y2 = y1 + dataStorableElement.getHeight();

		if (this.textStorableElements == null)
			return new Rectangle(x1,y1,x2 - x1,y2 - y1);
		
		for (AttachedTextStorableElement textStorableElement : this.textStorableElements)
		{
			DataStorableElement vertAttacher = textStorableElement.getVerticalAttacher();
			DataStorableElement horizAttacher = textStorableElement.getHorizontalAttacher();			
			if (!		(	(vertAttacher != null)
						&&	vertAttacher.equals(dataStorableElement)
					||	(horizAttacher != null)
						&&	horizAttacher.equals(dataStorableElement)))
				continue;

			int labelX = textStorableElement.getLocation().x;
			int labelY = textStorableElement.getLocation().y;
			int labelWidth = textStorableElement.getWidth();
			int labelHeight = textStorableElement.getHeight();
			
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
	public boolean clasterContainsPoint (DataStorableElement dataStorableElement,int x, int y)
	{
		Rectangle bounds = this.getElementClasterBounds(dataStorableElement);
		return bounds.contains(x,y);
	}
	
	public DataStorableElement getDataRElement(Identifier dreId)
	{
		for (DataStorableElement dataStorableElement : this.dataStorableElements)
			if (dataStorableElement.getId().equals (dreId))
				return dataStorableElement;
		
		return null;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DataStorableElement> getDataStorableElements() {
		return this.dataStorableElements;
	}

	public List<ImageStorableElement> getImageStorableElements() {
		return this.imageStorableElements;
	}

	public List<AttachedTextStorableElement> getTextStorableElements() {
		return this.textStorableElements;
	}

	public String getName() {
		return this.name;
	}

//	public List getObjectResourceFilters() {
//		return this.objectResourceFilters;
//	}

	public IntDimension getSize() {
		return this.size;
	}

	public String getDestinationModule() {
		return this.destinationModule;
	}
	
	public ReportTemplate() {
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
	
	public boolean doObjectsIntersect(){
		return false;
	}

	public void setDestinationModule(String destinationModule) {
		this.destinationModule = destinationModule;
	}

	public void setSize(IntDimension size) {
		this.size = size;
	}

	public ORIENTATION getOrientation() {
		return this.orientation;
	}

	public void setOrientation(ORIENTATION orientation) {
		this.orientation = orientation;
	}
	
	public void addElement(StorableElement element) {
		if (element instanceof DataStorableElement)
			this.dataStorableElements.add((DataStorableElement)element);
		else if (element instanceof ImageStorableElement)
			this.imageStorableElements.add((ImageStorableElement)element);
		else if (element instanceof AttachedTextStorableElement)
			this.textStorableElements.add((AttachedTextStorableElement)element);
	}

	public void removeElement(StorableElement element) {
		if (element instanceof DataStorableElement)
			this.dataStorableElements.remove(element);
		else if (element instanceof ImageStorableElement)
			this.imageStorableElements.remove(element);
		else if (element instanceof AttachedTextStorableElement)
			this.textStorableElements.remove(element);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		// TODO Auto-generated method stub
		return null;
	}
}
