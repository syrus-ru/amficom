/*
 * $Id: ReportTemplate.java,v 1.3 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
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
 * <p>����� ������� ������ - �������� � ���� ������ ���������, �������� �
 * ��������, ������������ � ��, � ����� �������� �� ������������ � ����������
 * � �������� �� CORBA. ����� �������� ��� ��������� �������.</p>
 * 
 * <p>��� ������� ������������� �� ������ ������ �� ���� ����� ���������
 * ����� </p>
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2005/08/31 10:32:55 $
 * @module generalclient_v1
 */
public class ReportTemplate implements Serializable
{
	private static final long serialVersionUID = 6270406142449624592L;
	
	public static final Dimension A4 = new Dimension(827,1170);
	public static final Dimension A3 = new Dimension(1170,1655);
	
	public static final int STANDART_MARGIN_SIZE = 60;

	//��� �������� ����
	private Identifier id = null;
	//��� �������� ����	
	private String name = "";
	//��� �������� ����	
	private String description = "";

	/**
	 * ����� ����������
	 */
	private long savedTime = 0L;
	/**
	 * ����� ���������� ���������
	 */
	private long modifiedTime = 0L;

	//��� �������� ����
	/**
	 * ������ ������� (��� ������)
	 */
	private Dimension size = A4;

	//��� �������� ����
	/**
	 * ������ ������� (��� ������)
	 */
	private int marginSize = ReportTemplate.STANDART_MARGIN_SIZE;
	
	//��� �������� ����
	/**
	 * �������������� ������� � ������
	 */
	private String destinationModule = DestinationModules.UNKNOWN_MODULE;

	//��� �������� ����	
	/**
	 * ������ ���� ��������� �������
	 */
	private List<DataStorableElement> dataStorableElements = new ArrayList<DataStorableElement>();
//	//��� �������� ����	
//	/**
//	 * ������ �������� �������������� � �������
//	 */
//	private List objectResourceFilters = new ArrayList();
	//��� �������� ����	
	/**
	 * ������ �������� �� �������
	 */
	private List<AttachedTextStorableElement> textStorableElements = new ArrayList<AttachedTextStorableElement>();
	//��� �������� ����	
	/**
	 * ������ �������� �� �������
	 */
	private List<ImageStorableElement> imageStorableElements = new ArrayList<ImageStorableElement>();

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
		out.writeObject(this.destinationModule);
		out.writeObject(this.size);
		out.writeInt(this.marginSize);		

		// ������������ �������� ����������� ������
		out.writeInt(this.dataStorableElements.size());
		for (DataStorableElement curRO : this.dataStorableElements)
		{
			//TODO ������ ���������.
			out.writeObject(curRO);
		}

		// ������������ �������� ����������� ��������
		out.writeInt(this.textStorableElements.size());
		for (AttachedTextStorableElement curLabel : this.textStorableElements)
			curLabel.writeObject(out);

		// ������������ �������� ����������� �����������
		out.writeInt(this.imageStorableElements.size());
		for (ImageStorableElement curImage : this.imageStorableElements)
			out.writeObject(curImage);

//		// ������������ �������
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
		this.size = (Dimension)in.readObject();
		this.marginSize = in.readInt();

		// ������������ �������
		this.dataStorableElements.clear();

		int orCount = in.readInt();
		for (int i = 0; i < orCount; i++)
		{
			DataStorableElement curRO = (DataStorableElement)in.readObject();
			this.dataStorableElements.add(curRO);
		}

		// ������������ �������
		this.textStorableElements.clear();

		int labelCount = in.readInt();
		for (int i = 0; i < labelCount; i++)
		{
			AttachedTextStorableElement curLabel = new AttachedTextStorableElement();
			curLabel.readObject(in,this);
			this.textStorableElements.add(curLabel);
		}

		// ������������ ��������
		this.imageStorableElements.clear();

		int imagesCount = in.readInt();
		for (int i = 0; i < imagesCount; i++)
		{
			ImageStorableElement curImage = (ImageStorableElement)in.readObject();
			this.imageStorableElements.add(curImage);
		}

//		// ������������ �������
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
	 * @param rteName ������� ���
	 * @return ������� �������, ������������ ������� �����
	 */
	public DataStorableElement findStorableElementForName (String rteName)
	{
		for (DataStorableElement storableElement : this.dataStorableElements)
			if (storableElement.getReportName().equals(rteName))
				return storableElement;

		return null;
	}

	/**
	 * @param dataStorableElement ������ ����������� ������
	 * @return ������ ��������, ����������� � ������� ������� �����������
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
	 * @return ���������� ������� ��������������, � �������
	 * ����������� ����������� ����������� �������� ������� �
	 * ����������� � ���� �������.
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
	 * ���������: ����������� �� �������� (�������� ������� � ������������ ���������)
	 * ������ �����.
	 * @param x ���������� ����� �� x
	 * @param y ���������� ����� �� y
	 * @return true ���� �����, ����������� ��������
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

	public long getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public List<DataStorableElement> getDataStorableElements() {
		return this.dataStorableElements;
	}

	public Identifier getId() {
		return this.id;
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

	public Dimension getSize() {
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

	public void setSize(Dimension size) {
		this.size = size;
	}

	public void setId(Identifier id) {
		this.id = id;
	}
}
