package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.*;
import com.syrus.AMFICOM.general.CharacteristicType;

public final class CharacteristicTypeController implements ObjectResourceController
{
	public static final String KEY_NAME = "name";

	private static CharacteristicTypeController instance;

	private List keys;

	private CharacteristicTypeController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_NAME
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CharacteristicTypeController getInstance()
	{
		if (instance == null)
			instance = new CharacteristicTypeController();
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
		if (object instanceof CharacteristicType)
		{
			CharacteristicType type = (CharacteristicType)object;
			if (key.equals(KEY_NAME))
			{
				result = type.getDescription();
			}
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value){
		// TODO empty method !!!
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
//		 TODO empty method !!!
	}

	public Class getPropertyClass(String key)
	{
		Class clazz = String.class;
		return clazz;
	}
}
