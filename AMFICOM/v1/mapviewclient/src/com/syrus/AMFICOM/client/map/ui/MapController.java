package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.Map;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MapController implements ObjectResourceController 
{
	public static final String KEY_NAME = "Name";
	public static final String KEY_DOMAIN = "Domain";
	public static final String KEY_USER = "User_id";
	public static final String KEY_CREATED = "Created";
	public static final String KEY_MODIFIED = "Modified";

	private static MapController instance;

	private List keys;

	  static SimpleDateFormat sdf =
		new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapPanel";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	private MapController() 
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

	public static MapController getInstance() 
	{
		if (instance == null)
			instance = new MapController();
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
		Map map = (Map)object;
		if (key.equals(KEY_NAME))
		{
			result = map.getName();
		}
		else
		if (key.equals(KEY_DOMAIN))
		{
			try
			{
				Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(map.getDomainId(), false);
				result = domain.getName();
			}
			catch (CommunicationException e)
			{
				result = "";
			}
			catch (DatabaseException e)
			{
				result = "";
			}
		}
		else
		if (key.equals(KEY_USER))
		{
			try
			{
				User user = (User )AdministrationStorableObjectPool.getStorableObject(map.getCreatorId(), false);
				result = user.getName();
			}
			catch (CommunicationException e)
			{
				result = "";
			}
			catch (DatabaseException e)
			{
				result = "";
			}
		}
		else
		if (key.equals(KEY_CREATED))
		{
			result = sdf.format(map.getCreated());
		}
		else
		if (key.equals(KEY_MODIFIED))
		{
			result = sdf.format(map.getModified());
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
