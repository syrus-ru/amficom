package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.Point;

import java.util.ArrayList;
import java.util.List;

public final class MapPhysicalLinkBinding 
{
	private MapPhysicalLinkElement link = null;
	
	private ObjectResource[][] bindingMap = null;
	
	private ArrayList bindObjects = new ArrayList();
	
	private Dimension dimension = null;
	
	public MapPhysicalLinkBinding(MapPhysicalLinkElement link, Dimension bindingDimension)
	{
		this.link = link;
		setDimension(bindingDimension);
	}
	
	public void add(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			bindObjects.add(or);
	}
	
	public void remove(ObjectResource or)
	{
		if(or != null)
		{
			unbind(or);
			bindObjects.remove(or);
		}
	}
	
	public List getBindObjects()
	{
		return (List )bindObjects.clone();
	}
	
	public Dimension getDimension()
	{
		return dimension;
	}
	
	public void setDimension(Dimension dimension)
	{
		this.dimension = dimension;
		ObjectResource[][] bindingMap2 = new ObjectResource[dimension.width][dimension.height];

		for (int i = 0; i < bindingMap2.length; i++) 
		{
			for (int j = 0; j < bindingMap2[i].length; j++) 
			{
				bindingMap2[i][j] = null;
			}
		}
		
		if(bindingMap != null)
		{
			int mini = Math.min(bindingMap.length, bindingMap2.length);

			for (int i = 0; i < mini; i++) 
			{
				int minj = Math.min(bindingMap[i].length, bindingMap2[i].length);
				for (int j = 0; j < minj; j++) 
				{
					bindingMap2[i][j] = bindingMap[i][j];
				}
			}
		}
		bindingMap = bindingMap2;
	}
	
	public void bind(ObjectResource or, int i, int j)
	{
		ObjectResource or1 = getBound(i, j);
		if(or1 != null)
		{
			unbind(or1);
		}
		unbind(or);
		bindingMap[i][j] = or;
	}
	
	public void unbind(ObjectResource or)
	{
		Point binding = getBinding(or);
		if(binding != null)
			bindingMap[binding.x][binding.y] = null;
	}
	
	public ObjectResource getBound(int i, int j)
	{
		return bindingMap[i][j];
	}
	
	public boolean isBound(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			return false;
		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				if(bindingMap[i][j] == or)
					return true;
			}
		}
		return false;
	}

	public Point getBinding(ObjectResource or)
	{
		int index = bindObjects.indexOf(or);
		if(index == -1)
			return null;
		for (int i = 0; i < bindingMap.length; i++) 
		{
			for (int j = 0; j < bindingMap[i].length; j++) 
			{
				if(bindingMap[i][j] == or)
					return new Point(i, j);
			}
		}
		return null;
	}
}
