/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.Object.User;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class SchemeController implements ObjectResourceController 
{
	public static final String KEY_NAME = "Name";
	public static final String KEY_DOMAIN = "Domain";
	public static final String KEY_USER = "User_id";
	public static final String KEY_CREATED = "Created";
	public static final String KEY_MODIFIED = "Modified";

	private static SchemeController instance;

	private List keys;

	  static SimpleDateFormat sdf =
		new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private SchemeController() 
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

	public static SchemeController getInstance() 
	{
		if (instance == null)
			instance = new SchemeController();
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
		Scheme sc = (Scheme )object;
		if (key.equals(KEY_NAME))
		{
			result = sc.getName();
		}
		else
		if (key.equals(KEY_DOMAIN))
		{
			result = Pool.getName(Domain.typ, sc.getDomainId());
		}
		else
		if (key.equals(KEY_USER))
		{
			result = Pool.getName(User.typ, sc.createdBy);
		}
		else
		if (key.equals(KEY_CREATED))
		{
			result = sdf.format(new Date(sc.created));
		}
		else
		if (key.equals(KEY_MODIFIED))
		{
			result = sdf.format(new Date(sc.getModified()));
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
