package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SimpleMapElementController implements ObjectResourceController 
{

	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";

	private static SimpleMapElementController instance;

	private List keys;

	private Map statusMap;
	private Map values;

	private SimpleMapElementController() 
	{
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, KEY_TYPE};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SimpleMapElementController getInstance() 
	{
		if (instance == null)
			instance = new SimpleMapElementController();
		return instance;
	}

	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(KEY_NAME))
			name = LangModel.getString("Name");
		else
		if (key.equals(KEY_TYPE))
			name = LangModel.getString("Type");
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof MapElement) 
		{
			MapElement me = (MapElement )object;
			if (key.equals(KEY_NAME))
				result = ((ObjectResource )object).getName();
			else
			if (key.equals(KEY_TYPE))
			{
				if(me instanceof MapSiteNodeElement)
				{
					MapNodeProtoElement mnpe = (MapNodeProtoElement )Pool.get(
							MapNodeProtoElement.typ, 
							((MapSiteNodeElement )me).getMapProtoId());
					result = mnpe.getName();
				}
				else
				if(me instanceof MapPhysicalLinkElement)
				{
					MapLinkProtoElement mlpe = (MapLinkProtoElement )Pool.get(
							MapLinkProtoElement.typ, 
							((MapPhysicalLinkElement )me).getMapProtoId());
					result = mlpe.getName();
				}
				else
					result = 
						LangModel.getString("node" + ((ObjectResource )object).getTyp());
			}
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

	public String getKey(final int index) 
	{
		return (String )this.keys.get(index);
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