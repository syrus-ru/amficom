package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class CableThreadTypeController implements ObjectResourceController
{
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";

	private static CableThreadTypeController instance;

	private List keys;

	private CableThreadTypeController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_COLOR,
				COLUMN_LINK_TYPE_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableThreadTypeController getInstance()
	{
		if (instance == null)
			instance = new CableThreadTypeController();
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
		if (object instanceof CableThreadType)
		{
			CableThreadType type = (CableThreadType)object;
			if (key.equals(COLUMN_ID))
				result = type.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = type.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = type.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = type.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = type.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_CODENAME))
				result = type.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = type.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = type.getName();
			else if (key.equals(COLUMN_COLOR))
				result = Integer.toString(type.getColor());
			else if (key.equals(COLUMN_LINK_TYPE_ID))
				result = type.getLinkType().getId().getIdentifierString();
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
