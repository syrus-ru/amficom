/**
 * $Id: MapPhysicalLinkBinding.java,v 1.4 2004/10/18 12:43:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Объект привязки кабелей к тоннелю. Принадлежит определенному тоннелю.
 * включает всебя список кабелей, которые проходят по данному тоннелю,
 * и матрицу пролегания кабелей по трубам тоннеля
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/18 12:43:13 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalLinkBinding 
{
	/** карта привязки кабелей к трубам */
	private List[][] bindingMap = null;
	
	/** список кабелей, проложенных по данному тоннелю */
	private ArrayList bindObjects = new ArrayList();
	
	/** размерность тоннеля (матрица труб) */
	private Dimension dimension = null;
	
	public MapPhysicalLinkBinding(Dimension bindingDimension)
	{
		setDimension(bindingDimension);
	}
	
	/**
	 * добавить кабель в тоннель
	 */
	public void add(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			bindObjects.add(or);
	}
	
	/**
	 * удалить кабель из тоннеля
	 */
	public void remove(ObjectResource or)
	{
		if(or != null)
		{
			unbind(or);
			bindObjects.remove(or);
		}
	}
	
	/**
	 * удалить все кабели из тоннеля
	 */
	public void clear()
	{
		bindObjects.clear();

		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				bindingMap[i][j] = new LinkedList();
			}
		}
	}
	
	/**
	 * получить список кабелей
	 */
	public List getBindObjects()
	{
		return (List )bindObjects.clone();
	}
	
	/**
	 * получить размерность матрицы прокладки кабелей по трубам тоннеля
	 */
	public Dimension getDimension()
	{
		return dimension;
	}
	
	/**
	 * установить размерность матрицы прокладки кабелей по трубам тоннеля
	 */
	public void setDimension(Dimension dimension)
	{
		this.dimension = dimension;
		
		// создается новая матрица прокладки
		List[][] bindingMap2 = new List[dimension.width][dimension.height];

		for (int i = 0; i < bindingMap2.length; i++) 
		{
			for (int j = 0; j < bindingMap2[i].length; j++) 
			{
				bindingMap2[i][j] = new LinkedList();
			}
		}
		
		// копируется привязки из старой матрицы
		if(bindingMap != null)
		{
			int mini = Math.min(bindingMap.length, bindingMap2.length);

			for (int i = 0; i < mini; i++) 
			{
				int minj = Math.min(bindingMap[i].length, bindingMap2[i].length);
				for (int j = 0; j < minj; j++) 
				{
					bindingMap2[i][j].addAll(bindingMap[i][j]);
				}
			}
		}
		bindingMap = bindingMap2;
	}
	
	/**
	 * указать прокладку кабеля по трубе
	 */
	public void bind(ObjectResource or, int i, int j)
	{
		unbind(or);
		bindingMap[i][j].add(or);
	}
	
	/**
	 * убрать привязку кабеля к конкретной трубе в тоннеле
	 */
	public void unbind(ObjectResource or)
	{
		Point binding = getBinding(or);
		if(binding != null)
			bindingMap[binding.x][binding.y].remove(or);
	}
	
	/**
	 * получить список кабелей, проходящих по трубе
	 */
	public List getBound(int i, int j)
	{
		return bindingMap[i][j];
	}
	
	/**
	 * проверить, определено ли место прохождения кабеля в тоннеле
	 */
	public boolean isBound(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			return false;
		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				if(bindingMap[i][j].contains(or))
					return true;
			}
		}
		return false;
	}

	/**
	 * получить координаты трубы, по которой проходит кабель
	 * @return null если место кабеля не задано
	 */
	public Point getBinding(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			return null;
		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				if(bindingMap[i][j].contains(or))
					return new Point(i, j);
			}
		}
		return null;
	}
}
