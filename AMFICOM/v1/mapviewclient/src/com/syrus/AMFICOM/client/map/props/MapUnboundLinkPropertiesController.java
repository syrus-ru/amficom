/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.CablePath;

public final class MapUnboundLinkPropertiesController 
		extends MapElementPropertiesController 
{

	private static MapUnboundLinkPropertiesController instance;

	private List keys;

	private MapUnboundLinkPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_SCHEME_CABLE_ID,
				PROPERTY_TOPOLOGICAL_LENGTH,
				PROPERTY_START_NODE_ID,
				PROPERTY_END_NODE_ID };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapUnboundLinkPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapUnboundLinkPropertiesController();
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
		UnboundLink link = (UnboundLink)object;

		if (key.equals(PROPERTY_NAME))
		{
			result = link.getName();
		}
		else
		if (key.equals(PROPERTY_SCHEME_CABLE_ID))
		{
			// remove .getName()
			result = link.getCablePath().getName();
		}
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
		{
			result = MapPropertiesManager.getDistanceFormat().format(link.getLengthLt());
		}
		else
		if (key.equals(PROPERTY_START_NODE_ID))
		{
			// remove .getName()
			result = link.getStartNode().getName();
		}
		else
		if (key.equals(PROPERTY_END_NODE_ID))
		{
			// remove .getName()
			result = link.getEndNode().getName();
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
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
