/**
 * $Id: MapPhysicalLinkBinding.java,v 1.5 2004/11/18 14:13:43 krupenn Exp $
 *
 * Syrus Systems
 * Ќаучно-технический центр
 * ѕроект: јћ‘» ќћ јвтоматизированный ћного‘ункциональный
 *         »нтеллектуальный  омплекс ќбъектного ћониторинга
 *
 * ѕлатформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ќбъект прив€зки кабелей к тоннелю. ѕринадлежит определенному тоннелю.
 * включает всеб€ список кабелей, которые проход€т по данному тоннелю,
 * и матрицу пролегани€ кабелей по трубам тоннел€
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/11/18 14:13:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalLinkBinding 
{
	/** карта прив€зки кабелей к трубам */
	private List[][] bindingMap = null;
	
	/** список кабелей, проложенных по данному тоннелю */
	private ArrayList bindObjects = new ArrayList();
	
	/** размерность тоннел€ (матрица труб) */
	private Dimension dimension = null;

	/**
	 * пор€док нумерации труб сверху вниз
	 */	
	protected boolean topToBottom = true;

	/**
	 * пор€док нумерации слева направо
	 */	
	protected boolean leftToRight = true;

	/**
	 * пор€док нумерации сначала по горизонтали, затем по вертикали
	 */	
	protected boolean horizontalVertical = true;


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
	 * удалить кабель из тоннел€
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
	 * удалить все кабели из тоннел€
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
	 * получить размерность матрицы прокладки кабелей по трубам тоннел€
	 */
	public Dimension getDimension()
	{
		return dimension;
	}
	
	/**
	 * установить размерность матрицы прокладки кабелей по трубам тоннел€
	 */
	public void setDimension(Dimension dimension)
	{
		this.dimension = dimension;
		
		// создаетс€ нова€ матрица прокладки
		List[][] bindingMap2 = new List[dimension.width][dimension.height];

		for (int i = 0; i < bindingMap2.length; i++) 
		{
			for (int j = 0; j < bindingMap2[i].length; j++) 
			{
				bindingMap2[i][j] = new LinkedList();
			}
		}
		
		// копируетс€ прив€зки из старой матрицы
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
	 * указать прокладку кабел€ по трубе
	 */
	public void bind(ObjectResource or, int i, int j)
	{
		unbind(or);
		bindingMap[i][j].add(or);
	}
	
	/**
	 * убрать прив€зку кабел€ к конкретной трубе в тоннеле
	 */
	public void unbind(ObjectResource or)
	{
		Point binding = getBinding(or);
		if(binding != null)
			bindingMap[binding.x][binding.y].remove(or);
	}
	
	/**
	 * получить список кабелей, проход€щих по трубе
	 */
	public List getBound(int i, int j)
	{
		return bindingMap[i][j];
	}
	
	/**
	 * проверить, определено ли место прохождени€ кабел€ в тоннеле
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
	 * @return null если место кабел€ не задано
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

	public void flipTopToBottom()
	{
		topToBottom = !topToBottom;
	}


	public boolean isTopToBottom()
	{
		return topToBottom;
	}


	public void flipLeftToRight()
	{
		leftToRight = !leftToRight;
	}


	public boolean isLeftToRight()
	{
		return leftToRight;
	}


	public void flipHorizontalVertical()
	{
		horizontalVertical = !horizontalVertical;
	}


	public boolean isHorizontalVertical()
	{
		return horizontalVertical;
	}
}
