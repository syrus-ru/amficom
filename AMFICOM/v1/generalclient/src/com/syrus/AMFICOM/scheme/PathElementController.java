package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.corba.PathElement;

public final class PathElementController implements ObjectResourceController
{
	public static final String KEY_NAME = "name";
	public static final String KEY_SEQUENTIAL_NUMBER = "number";

	private static PathElementController instance;

	private List keys;

	private PathElementController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_NAME,
				KEY_SEQUENTIAL_NUMBER
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static PathElementController getInstance()
	{
		if (instance == null)
			instance = new PathElementController();
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
		if (key.equals(KEY_SEQUENTIAL_NUMBER))
			name = "№";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof PathElement)
		{
			PathElement pe = (PathElement)object;
			if (key.equals(KEY_NAME))
				/**
				 * @todo insert pe.getName();
				 */
				result = "";
			if (key.equals(KEY_SEQUENTIAL_NUMBER))
				result = new Integer(pe.getSequentialNumber());
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


