/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

public final class MapSiteNodePropertiesController 
		extends MapElementPropertiesController 
{

	private static MapSiteNodePropertiesController instance;

	private List keys;

	private MapSiteNodePropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_PROTO_ID,
				PROPERTY_LATITUDE,
				PROPERTY_LONGITUDE};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapSiteNodePropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapSiteNodePropertiesController();
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
		SiteNode site = (SiteNode)object;

		if (key.equals(PROPERTY_NAME))
		{
			result = site.getName();
		}
		else
		if (key.equals(PROPERTY_PROTO_ID))
		{
			// remove .getName()
			result = ((SiteNodeType )site.getType()).getName();
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(site.getLocation().getX());
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = MapPropertiesManager.getCoordinatesFormat().format(site.getLocation().getY());
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		if (key.equals(PROPERTY_NAME))
			return true;
		if (key.equals(PROPERTY_PROTO_ID))
			return true;
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
		SiteNode site = (SiteNode)object;

		if (key.equals(PROPERTY_NAME))
		{
			site.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_PROTO_ID))
		{
			site.setType((SiteNodeType)value);
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			try
			{
				DoublePoint pt = site.getLocation();
				pt.setLocation(Double.parseDouble((String )value), pt.getY());
				site.setLocation(pt);
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
				DoublePoint pt = site.getLocation();
				pt.setLocation(pt.getX(), Double.parseDouble((String )value));
				site.setLocation(pt);
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
