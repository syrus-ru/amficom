package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class EquipmentTypeController implements ObjectResourceController
{
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";

	private static EquipmentTypeController instance;

	private List keys;

	private EquipmentTypeController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_NAME,
				KEY_DESCRIPTION
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EquipmentTypeController getInstance()
	{
		if (instance == null)
			instance = new EquipmentTypeController();
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
		if (object instanceof EquipmentType)
		{
			EquipmentType type = (EquipmentType)object;
			if (key.equals(KEY_NAME))
				result = type.getName();
			if (key.equals(KEY_DESCRIPTION))
				result = type.getDescription();
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
