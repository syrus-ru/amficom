/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapSiteNodePropertiesController 
		implements MapElementPropertiesController 
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

	public Object getValue(final ObjectResource object, final String key)
	{
		Object result = null;
		MapSiteNodeElement site = (MapSiteNodeElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = site.getName();
		}
		else
		if (key.equals(PROPERTY_PROTO_ID))
		{
			result = Pool.get(MapNodeProtoElement.typ, site.getMapProtoId());
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = String.valueOf(MiscUtil.fourdigits(site.getAnchor().x));
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = String.valueOf(MiscUtil.fourdigits(site.getAnchor().y));
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

	public void setValue(ObjectResource objectResource, final String key, final Object value)
	{
		MapSiteNodeElement site = (MapSiteNodeElement )objectResource;

		if (key.equals(PROPERTY_NAME))
		{
			site.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_PROTO_ID))
		{
			site.setMapProtoId(((MapElement )value).getId());
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			try
			{
				Point2D.Double pt = site.getAnchor();
				pt.x = Double.parseDouble((String )value);
				site.setAnchor(pt);
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
				Point2D.Double pt = site.getAnchor();
				pt.y = Double.parseDouble((String )value);
				site.setAnchor(pt);
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
