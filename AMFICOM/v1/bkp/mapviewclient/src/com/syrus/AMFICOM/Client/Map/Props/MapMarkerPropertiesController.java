/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapMarkerPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapMarkerPropertiesController instance;

	private List keys;

	private MapMarkerPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_DISTANCE,
				PROPERTY_LATITUDE,
				PROPERTY_LONGITUDE,
				PROPERTY_PATH_ID,
				PROPERTY_TYPE};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapMarkerPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapMarkerPropertiesController();
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
		MapMarker marker = (MapMarker )object;

		if (key.equals(PROPERTY_NAME))
		{
			result = marker.getName();
		}
		else
		if (key.equals(PROPERTY_TYPE))
		{
			result = LangModel.getString("node" + ((ObjectResource )marker).getTyp());
		}
		else
		if (key.equals(PROPERTY_LATITUDE))
		{
			result = String.valueOf(marker.getAnchor().x);
		}
		else
		if (key.equals(PROPERTY_LONGITUDE))
		{
			result = String.valueOf(marker.getAnchor().y);
		}
		else
		if (key.equals(PROPERTY_DISTANCE))
		{
			result = String.valueOf(marker.getFromStartLengthLf());
		}
		else
		if (key.equals(PROPERTY_PATH_ID))
		{
			result = marker.getMeasurementPath();
		}

		return result;
	}

	public boolean isEditable(final String key)
	{
		if (key.equals(PROPERTY_DISTANCE))
			return true;
		return false;
	}

	public void setValue(ObjectResource objectResource, final String key, final Object value)
	{
		MapMarker marker = (MapMarker )objectResource;

		if (key.equals(PROPERTY_DISTANCE))
		{
			try
			{
				double dist = Double.parseDouble((String )value);
				marker.moveToFromStartLf(dist);
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
