package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class MeasurementPortController implements ObjectResourceController
{
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";

	private static MeasurementPortController instance;

	private List keys;

	private MeasurementPortController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_NAME,
				KEY_DESCRIPTION
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementPortController getInstance()
	{
		if (instance == null)
			instance = new MeasurementPortController();
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
		if (key.equals(KEY_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof MeasurementPort)
		{
			MeasurementPort port = (MeasurementPort)object;
			if (key.equals(KEY_NAME))
				result = port.getName();
			if (key.equals(KEY_DESCRIPTION))
				result = port.getDescription();
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
