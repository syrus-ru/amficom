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
import com.syrus.AMFICOM.map.NodeLink;

public final class MapNodeLinkPropertiesController 
		extends MapElementPropertiesController 
{

	private static MapNodeLinkPropertiesController instance;

	private List keys;

	private MapNodeLinkPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_TOPOLOGICAL_LENGTH,
				PROPERTY_START_NODE_ID,
				PROPERTY_END_NODE_ID,
				PROPERTY_PHYSICAL_LINK_ID};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapNodeLinkPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapNodeLinkPropertiesController();
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
		NodeLink nodeLink = (NodeLink)object;

		if (key.equals(PROPERTY_NAME))
		{
			result = nodeLink.getName();
		}
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
		{
			result = MapPropertiesManager.getDistanceFormat().format(nodeLink.getLengthLt());
		}
		else
		if (key.equals(PROPERTY_START_NODE_ID))
		{
			// remove .getName()
			result = nodeLink.getStartNode().getName();
		}
		else
		if (key.equals(PROPERTY_END_NODE_ID))
		{
			// remove .getName()
			result = nodeLink.getEndNode().getName();
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LINK_ID))
		{
			// remove .getName()
			result = nodeLink.getPhysicalLink().getName();
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
		NodeLink nodeLink = (NodeLink)object;

		if (key.equals(PROPERTY_NAME))
		{
			nodeLink.setName((String )value);
		}
	}

	public Object getPropertyValue(final String key) 
	{
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) 
	{//empty
	}

	public Class getPropertyClass(String key) 
	{
		Class clazz = String.class;
		return clazz;
	}

}
