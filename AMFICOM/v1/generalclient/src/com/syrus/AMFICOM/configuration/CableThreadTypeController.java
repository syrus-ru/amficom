package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class CableThreadTypeController implements ObjectResourceController
{
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";

	private static CableThreadTypeController instance;

	private List keys;

	private CableThreadTypeController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				StorableObjectDatabase.COLUMN_ID,
				StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID,
				StorableObjectType.COLUMN_CODENAME,
				StorableObjectType.COLUMN_DESCRIPTION,
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
		if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof CableThreadType)
		{
			CableThreadType type = (CableThreadType)object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				result = type.getId().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				result = type.getCreated().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				result = type.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				result = type.getModified().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				result = type.getModifierId().getIdentifierString();
			else if (key.equals(StorableObjectType.COLUMN_CODENAME))
				result = type.getCodename();
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
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
