/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		MapPhysicalNodeElement node = (MapPhysicalNodeElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = node.getName();
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(node.getLocation().x);
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(node.getLocation().y);
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LINK_ID))
		{
			// remove .getName()
			result = node.getMap().getPhysicalLink(node.getPhysicalLinkId()).getName();
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		return true;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		MapPhysicalNodeElement node = (MapPhysicalNodeElement )object;

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
				pt.x = Double.parseDouble((String )value);
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
				pt.y = Double.parseDouble((String )value);
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
