/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MeasurementPathController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapMeasurementPathPropertiesController 
		implements MapElementPropertiesController 
{

	private static MapMeasurementPathPropertiesController instance;

	private List keys;

	private MapMeasurementPathPropertiesController() 
	{
		String[] keysArray = new String[] { 
				PROPERTY_NAME,
				PROPERTY_SCHEME_PATH_ID,
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

	public static MapMeasurementPathPropertiesController getInstance() 
	{
		if (instance == null)
			instance = new MapMeasurementPathPropertiesController();
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
		try
		{
			MapMeasurementPathElement path = (MapMeasurementPathElement )object;
			
			MeasurementPathController mpc = (MeasurementPathController )MeasurementPathController.getInstance();
	
			if (key.equals(PROPERTY_NAME))
			{
				result = path.getName();
			}
			else
			if (key.equals(PROPERTY_SCHEME_PATH_ID))
			{
				// remove .getName()
				result = path.getSchemePath().getName();
			}
			else
			if (key.equals(PROPERTY_TOPOLOGICAL_LENGTH))
			{
				result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLt());
			}
			else
			if (key.equals(PROPERTY_OPTICAL_LENGTH))
			{
				result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLo());
			}
			else
			if (key.equals(PROPERTY_PHYSICAL_LENGTH))
			{
				result = MapPropertiesManager.getDistanceFormat().format(path.getLengthLf());
			}
			else
			if (key.equals(PROPERTY_START_NODE_ID))
			{
				//remove .getName()
				result = path.getStartNode().getName();
			}
			else
			if (key.equals(PROPERTY_END_NODE_ID))
			{
				// remove .getName()
				result = path.getEndNode().getName();
			}
			else
			if (key.equals(PROPERTY_COLOR))
			{
				result = mpc.getColor(path);
			}
			else
			if (key.equals(PROPERTY_STYLE))
			{
				result = mpc.getStyle(path);
			}
			else
			if (key.equals(PROPERTY_THICKNESS))
			{
				result = String.valueOf(mpc.getLineSize(path));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result = "";
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
		MapCablePathElement path = (MapCablePathElement )object;

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
