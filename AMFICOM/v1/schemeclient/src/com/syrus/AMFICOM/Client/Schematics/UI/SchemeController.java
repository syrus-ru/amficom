package com.syrus.AMFICOM.Client.Schematics.UI;

import java.text.SimpleDateFormat;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.Scheme;

public final class SchemeController extends ObjectResourceController
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
		String name = LangModelSchematics.getString(key);
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		Scheme sc = (Scheme)object;
		if (key.equals(KEY_NAME)) {
			result = sc.getName();
		}
		else if (key.equals(KEY_DOMAIN)) {
			try
			{
				result = ((Domain) (AdministrationStorableObjectPool
						.getStorableObject(sc
								.getDomainId(),
								true)))
						.getName();
			}
			catch (final ApplicationException ae)
			{
				ae.printStackTrace();
				result = null;
			}
		}
		else if (key.equals(KEY_USER)) {
			try {
				User user = (User)ConfigurationStorableObjectPool.getStorableObject(
						sc.getCreatorId(), true);
				result = user.getName();
			}
			catch (Exception ex) {
				result = "";
			}
		}
		else if (key.equals(KEY_CREATED)) {
			result = sdf.format(sc.getCreated());
		}
		else if (key.equals(KEY_MODIFIED)) {
			result = sdf.format(sc.getModified());
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
