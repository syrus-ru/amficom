/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.syrus.AMFICOM.map.AbstractNode;

public final class MapPhysicalLinkPropertiesController 
		extends MapElementPropertiesController 
{

	private static MapPhysicalLinkPropertiesController instance;

	private List keys;

	private MapPhysicalLinkPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_PROTO_ID,
				PROPERTY_TOPOLOGICAL_LENGTH,
				PROPERTY_START_NODE_ID,
				PROPERTY_END_NODE_ID,
				PROPERTY_COLOR,
				PROPERTY_STYLE,
				PROPERTY_THICKNESS};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapPhysicalLinkPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapPhysicalLinkPropertiesController();
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
		PhysicalLink link = (PhysicalLink)object;

		PhysicalLinkController plc = (PhysicalLinkController)com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController.getInstance();

		if (key.equals(PROPERTY_NAME))
		{
			result = link.getName();
		}
		else
		if (key.equals(PROPERTY_PROTO_ID))
		{
			// remove .getName()
			result = ((PhysicalLinkType )link.getType()).getName();
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
		else
		if (key.equals(PROPERTY_COLOR))
		{
			result = plc.getColor(link);
		}
		else
		if (key.equals(PROPERTY_STYLE))
		{
			result = plc.getStyle(link);
		}
		else
		if (key.equals(PROPERTY_THICKNESS))
		{
			result = String.valueOf(plc.getLineSize(link));
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
		PhysicalLink link = (PhysicalLink)object;

		if (key.equals(PROPERTY_NAME))
		{
			link.setName((String )value);
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
