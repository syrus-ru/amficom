/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapMeasurementPathController 
		implements ObjectResourcePropertiesController 
{

	private static MapMeasurementPathController instance;

	private List keys;

	private MapMeasurementPathController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_PHYSICAL_LINK_ID,
				PROPERTY_TOPOLOGICAL_LENGTH,
				PROPERTY_OPTICAL_LENGTH,
				PROPERTY_PHYSICAL_LENGTH,
				PROPERTY_START_NODE_ID,
				PROPERTY_END_NODE_ID,
				PROPERTY_COLOR,
				PROPERTY_STYLE,
				PROPERTY_THICKNESS};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapMeasurementPathController getInstance() 
	{
		if (instance == null)
			instance = new MapMeasurementPathController();
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
		MapMeasurementPathElement path = (MapMeasurementPathElement )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = path.getName();
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LINK_ID))
		{
			result = path.getSchemePath();
		}
		else
		if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
		{
			result = String.valueOf(path.getLengthLt());
		}
		else
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
		{
			result = String.valueOf(path.getLengthLo());
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
		{
			result = String.valueOf(path.getLengthLf());
		}
		else
		if (key.equals(PROPERTY_START_NODE_ID))
		{
			result = path.getStartNode();
		}
		else
		if (key.equals(PROPERTY_END_NODE_ID))
		{
			result = path.getEndNode();
		}
		else
		if (key.equals(PROPERTY_COLOR))
		{
			result = path.getColor();
		}
		else
		if (key.equals(PROPERTY_STYLE))
		{
			result = path.getStyle();
		}
		else
		if (key.equals(PROPERTY_THICKNESS))
		{
			result = String.valueOf(path.getLineSize());
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
		MapCablePathElement path = (MapCablePathElement )objectResource;

		if (key.equals(PROPERTY_NAME))
		{
			path.setName((String )value);
		}
		else
		if (key.equals(PROPERTY_OPTICAL_LENGTH))
		{
			try 
			{
				path.setLengthLo(Double.parseDouble((String )value));
			} 
			catch (NumberFormatException ex) 
			{
			} 
		}
		else
		if (key.equals(PROPERTY_PHYSICAL_LENGTH))
		{
			try
			{
				path.setLengthLf(Double.parseDouble((String )value));
			} 
			catch (NumberFormatException ex) 
			{
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
