package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class CableThreadController implements ObjectResourceController
{
	private static CableThreadController instance;

	private List keys;

	private CableThreadController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_TYPE_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableThreadController getInstance()
	{
		if (instance == null)
			instance = new CableThreadController();
		return instance;
	}

	public List getKeys()
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "Название";
		if (key.equals(COLUMN_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof CableThread)
		{
			CableThread thread = (CableThread)object;
			if (key.equals(COLUMN_ID))
				result = thread.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = thread.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = thread.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = thread.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = thread.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = thread.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = thread.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = thread.getType().getId().getIdentifierString();
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
