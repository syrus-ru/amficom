/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.Object.User;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class MapViewController implements ObjectResourceController 
{
	public static final String KEY_NAME = "Name";
	public static final String KEY_DOMAIN = "Domain";
	public static final String KEY_USER = "User_id";
	public static final String KEY_CREATED = "Created";
	public static final String KEY_MODIFIED = "Modified";

	private static MapViewController instance;

	private List keys;

	  static SimpleDateFormat sdf =
		new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private MapViewController() 
	{
		// empty private constructor
		String[] keysArray = new String[] { 
				KEY_NAME, 
				KEY_DOMAIN, 
				KEY_USER, 
				KEY_CREATED, 
				KEY_MODIFIED };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MapViewController getInstance() 
	{
		if (instance == null)
			instance = new MapViewController();
		return instance;
	}
	
	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = LangModelMap.getString(key);
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		MapView view = (MapView )object;
		if (key.equals(KEY_NAME))
		{
			result = view.getName();
		}
		else
		if (key.equals(KEY_DOMAIN))
		{
			try
			{
				result = AdministrationStorableObjectPool.getStorableObject(view.getDomainId(), false);
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}
		}
		else
		if (key.equals(KEY_USER))
		{
			try
			{
				result = AdministrationStorableObjectPool.getStorableObject(view.getCreatorId(), false);
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}
		}
		else
		if (key.equals(KEY_CREATED))
		{
			result = sdf.format(view.getCreated());
		}
		else
		if (key.equals(KEY_MODIFIED))
		{
			result = sdf.format(view.getModified());
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
