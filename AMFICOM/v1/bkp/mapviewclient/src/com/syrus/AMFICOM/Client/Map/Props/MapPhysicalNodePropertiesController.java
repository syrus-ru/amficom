/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;

public final class MapPhysicalNodePropertiesController 
		implements MapElementPropertiesController 
{

	private static MapPhysicalNodePropertiesController instance;

	private List keys;

	private MapPhysicalNodePropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME, 
				PROPERTY_LATITUDE,
				PROPERTY_LONGITUDE,
				PROPERTY_PHYSICAL_LINK_ID};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapPhysicalNodePropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapPhysicalNodePropertiesController();
		return instance;
	}
	
	public String getKey(final int index) 
	{
		return (String )this.keys.get(index);
	}

	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = LangModelMap.getString(key);
		if(name == null)
			name = "";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		TopologicalNode node = (TopologicalNode)object;

		if (key.equals(PROPERTY_NAME))
		{
			result = node.getName();
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(node.getLocation().getX());
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(node.getLocation().getY());
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LINK_ID))
		{
			// remove .getName()
			result = node.getPhysicalLink().getName();
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		return true;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		TopologicalNode node = (TopologicalNode)object;

		if (key.equals(PROPERTY_NAME))
		{
			node.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			try
			{
				DoublePoint pt = node.getLocation();
				pt.setLocation(Double.parseDouble((String )value), pt.getY());
				node.setLocation(pt);
			}
			catch(NumberFormatException e)
			{
				return;
			}
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			try
			{
				DoublePoint pt = node.getLocation();
				pt.setLocation(pt.getX(), Double.parseDouble((String )value));
				node.setLocation(pt);
			}
			catch(NumberFormatException e)
			{
				return;
			}
		}
	}

	public Object getPropertyValue(final String key) 
	{
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) 
	{
	}

	public Class getPropertyClass(String key) 
	{
		Class clazz = String.class;
		return clazz;
	}

}
