package com.syrus.AMFICOM.measurement;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.*;

public final class TestController implements ObjectResourceController
{
	public static final String KEY_START_TIME = "starttime";

	private static TestController instance;

	private List keys;

	private TestController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_START_TIME
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static TestController getInstance()
	{
		if (instance == null)
			instance = new TestController();
		return instance;
	}

	public List getKeys()
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(KEY_START_TIME))
			name = "Время начала";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof Test)
		{
			Test t = (Test)object;
			if (key.equals(KEY_START_TIME))
			{
				result = t.getStartTime();
			}
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
		if (key.equals(KEY_START_TIME))
		 return Date.class;
		return Object.class;
	}
}
