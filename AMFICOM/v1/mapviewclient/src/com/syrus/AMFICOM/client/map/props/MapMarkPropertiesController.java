/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class MapMarkPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapMarkPropertiesController instance;

	private List keys;

	private MapMarkPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME, 
				PROPERTY_LATITUDE,
				PROPERTY_LONGITUDE,
				PROPERTY_PHYSICAL_LINK_ID};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapMarkPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapMarkPropertiesController();
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
		MapMarkElement mark = (MapMarkElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = mark.getName();
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = String.valueOf(MiscUtil.fourdigits(mark.getAnchor().x));
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = String.valueOf(MiscUtil.fourdigits(mark.getAnchor().y));
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LINK_ID))
		{
			result = mark.getLink();
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		if (key.equals(PROPERTY_NAME))
			return true;
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		MapMarkElement node = (MapMarkElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			node.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			try
			{
				Point2D.Double pt = node.getAnchor();
				pt.x = Double.parseDouble((String )value);
				node.setAnchor(pt);
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
				Point2D.Double pt = node.getAnchor();
				pt.y = Double.parseDouble((String )value);
				node.setAnchor(pt);
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
