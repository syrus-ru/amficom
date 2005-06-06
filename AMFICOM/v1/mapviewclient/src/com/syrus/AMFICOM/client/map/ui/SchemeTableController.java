/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.client.map.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.Scheme;

public final class SchemeTableController extends StorableObjectWrapper 
{
	public static final String KEY_NAME = "Name";
	public static final String KEY_DOMAIN = "Domain";
	public static final String KEY_USER = "User_id";
	public static final String KEY_CREATED = "Created";
	public static final String KEY_MODIFIED = "Modified";

	private static SchemeTableController instance;

	private List keys;
	private String[] keysArray;

	private SchemeTableController() 
	{
		// empty private constructor
		this.keysArray = new String[] { 
				KEY_NAME, 
				KEY_DOMAIN, 
				KEY_USER, 
				KEY_CREATED, 
				KEY_MODIFIED };
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(this.keysArray)));
	}

	public static SchemeTableController getInstance() 
	{
		if (instance == null)
			instance = new SchemeTableController();
		return instance;
	}
	
	public List getKeys() 
	{
		return this.keys;
	}

	public String[] getKeysArray() 
	{
		return this.keysArray;
	}

	public String getName(final String key)
	{
		String name = LangModelMap.getString(key);
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		Scheme sc = (Scheme )object;
		if (key.equals(KEY_NAME))
		{
			result = sc.getName();
		}
		else
		if (key.equals(KEY_DOMAIN))
		{
			try
			{
				result = StorableObjectPool
						.getStorableObject(sc
								.getDomainId(),
								true);
			}
			catch (final ApplicationException ae)
			{
				ae.printStackTrace();
				result = null;
			}
		}
		else
		if (key.equals(KEY_USER))
		{
			try
			{
				Identifier id = sc.getCreatorId();
				result = (User )StorableObjectPool.getStorableObject(id, false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		if (key.equals(KEY_CREATED))
		{
			result = MapPropertiesManager.getDateFormat().format(sc.getCreated());
		}
		else
		if (key.equals(KEY_MODIFIED))
		{
			result = MapPropertiesManager.getDateFormat().format(sc.getModified());
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{//empty
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
	{//empty
	}

	public Class getPropertyClass(String key) 
	{
		Class clazz = String.class;
		return clazz;
	}

}
