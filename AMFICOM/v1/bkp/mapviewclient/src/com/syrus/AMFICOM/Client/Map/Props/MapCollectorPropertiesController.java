/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapCollectorPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapCollectorPropertiesController instance;

	private List keys;

	private MapCollectorPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_TOPOLOGICAL_LENGTH };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapCollectorPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapCollectorPropertiesController();
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
		MapPipePathElement path = (MapPipePathElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = path.getName();
		}
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
		{
			result = String.valueOf(path.getLengthLt());
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		if (key.equals(PROPERTY_NAME))
			return true;
		return false;
	}

	public void setValue(ObjectResource objectResource, final String key, final Object value)
	{
		MapPipePathElement path = (MapPipePathElement )objectResource;

		if (key.equals(PROPERTY_NAME))
		{
			path.setName((String )value);
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
