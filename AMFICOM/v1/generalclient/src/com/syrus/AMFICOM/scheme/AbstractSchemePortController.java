package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePort;

public final class AbstractSchemePortController implements ObjectResourceController
{
	public static final String KEY_NAME = "name";

	private static AbstractSchemePortController instance;

	private List keys;

	private AbstractSchemePortController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_NAME
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AbstractSchemePortController getInstance()
	{
		if (instance == null)
			instance = new AbstractSchemePortController();
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
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof AbstractSchemePort)
		{
			AbstractSchemePort port = (AbstractSchemePort)object;
			if (key.equals(KEY_NAME))
				result = port.getName();
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
