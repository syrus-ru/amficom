/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Map.mapview.VoidElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapVoidElementPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapVoidElementPropertiesController instance;

	private List keys;

	private MapVoidElementPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapVoidElementPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapVoidElementPropertiesController();
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

		VoidElement ve = (VoidElement)object;
		MapView mapView = ve.getMapView();

		if (key.equals(PROPERTY_NAME))
		{
			result = mapView.getName();
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
		VoidElement ve = (VoidElement)object;
		MapView mapView = ve.getMapView();

		if (key.equals(PROPERTY_NAME))
		{
			mapView.setName((String )value);
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
