/**
 * $Id: PhysicalLinkBinding.java,v 1.2 2004/12/20 18:39:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ������ �������� ������� � �������. ����������� ������������� �������.
 * �������� ����� ������ �������, ������� �������� �� ������� �������,
 * � ������� ���������� ������� �� ������ �������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/12/20 18:39:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class PhysicalLinkBinding 
{
	/** ����� �������� ������� � ������ */
	private List[][] bindingMap = null;
	
	/** ������ �������, ����������� �� ������� ������� */
	private ArrayList bindObjects = new ArrayList();
	
	/** ����������� ������� (������� ����) */
	private IntDimension dimension = null;

	/**
	 * ������� ��������� ���� ������ ����
	 */	
	protected boolean topToBottom = true;

	/**
	 * ������� ��������� ����� �������
	 */	
	protected boolean leftToRight = true;

	/**
	 * ������� ��������� ������� �� �����������, ����� �� ���������
	 */	
	protected boolean horizontalVertical = true;


	public PhysicalLinkBinding(IntDimension bindingDimension)
	{
		setDimension(bindingDimension);
	}
	
	/**
	 * �������� ������ � �������
	 */
	public void add(Object or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			bindObjects.add(or);
	}
	
	/**
	 * ������� ������ �� �������
	 */
	public void remove(Object or)
	{
		if(or != null)
		{
			unbind(or);
			bindObjects.remove(or);
		}
	}
	
	/**
	 * ������� ��� ������ �� �������
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
	 * �������� ������ �������
	 */
	public List getBindObjects()
	{
		return (List )bindObjects.clone();
	}
	
	/**
	 * �������� ����������� ������� ��������� ������� �� ������ �������
	 */
	public IntDimension getDimension()
	{
		return dimension;
	}
	
	/**
	 * ���������� ����������� ������� ��������� ������� �� ������ �������
	 */
	public void setDimension(IntDimension dimension)
	{
		this.dimension = dimension;
		
		// ��������� ����� ������� ���������
		List[][] bindingMap2 = new List[dimension.getWidth()][dimension.getHeight()];

		for (int i = 0; i < bindingMap2.length; i++) 
		{
			for (int j = 0; j < bindingMap2[i].length; j++) 
			{
				bindingMap2[i][j] = new LinkedList();
			}
		}
		
		// ���������� �������� �� ������ �������
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
	
	public int getSequenceNumber(int ii, int jj)
	{
		int sequenceNumber = -1;
		int m = getDimension().getWidth();
		int n = getDimension().getHeight();
		int counter = 1;
		int limit = n * m;

		int istart = isLeftToRight() ? 0 : m - 1;
		int jstart = isTopToBottom() ? 0 : n - 1;

		int iend = m - 1 - istart;
		int jend = n - 1 - jstart;

		int iincrement = isLeftToRight() ? 1 : -1;
		int jincrement = isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while(true)
		{
			if(i == ii && j == jj)
				break;
		
			if(counter > limit)
				break;

			if(isHorizontalVertical())
			{
				if(i == iend)
				{
					i = istart;
					j += jincrement;
				}
				else
					i += iincrement;
			}
			else
			{
				if(j == jend)
				{
					j = jstart;
					i += iincrement;
				}
				else
					j += jincrement;
			}
		}
		return sequenceNumber;
	}
	
	/**
	 * ������� ��������� ������ �� �����
	 */
	public void bind(Object or, int i, int j)
	{
		unbind(or);
		bindingMap[i][j].add(or);
	}
	
	/**
	 * ������ �������� ������ � ���������� ����� � �������
	 */
	public void unbind(Object or)
	{
		IntPoint binding = getBinding(or);
		if(binding != null)
			bindingMap[binding.x][binding.y].remove(or);
	}
	
	/**
	 * �������� ������ �������, ���������� �� �����
	 */
	public List getBound(int i, int j)
	{
		return bindingMap[i][j];
	}
	
	/**
	 * ���������, ���������� �� ����� ����������� ������ � �������
	 */
	public boolean isBound(Object or)
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
	 * �������� ���������� �����, �� ������� �������� ������
	 * @return null ���� ����� ������ �� ������
	 */
	public IntPoint getBinding(Object or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			return null;
		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				if(bindingMap[i][j].contains(or))
					return new IntPoint(i, j);
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
