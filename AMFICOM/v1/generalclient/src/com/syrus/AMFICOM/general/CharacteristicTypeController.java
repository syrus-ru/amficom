package com.syrus.AMFICOM.general;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class CharacteristicTypeController implements ObjectResourceController
{
	private static CharacteristicTypeController instance;
	public static final String COLUMN_DATA_TYPE = "type";
	public static final String COLUMN_SORT = "sort";

	private List keys;

	private CharacteristicTypeController()
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
				COLUMN_DATA_TYPE,
				COLUMN_SORT
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
		if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof CharacteristicType) {
			CharacteristicType type = (CharacteristicType)object;
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
			else if (key.equals(COLUMN_DATA_TYPE))
				result = Integer.toString(type.getDataType().value());
			else if (key.equals(COLUMN_SORT))
				result = Integer.toString(type.getSort().value());
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
