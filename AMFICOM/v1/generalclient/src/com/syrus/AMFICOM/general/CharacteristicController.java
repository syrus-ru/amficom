package com.syrus.AMFICOM.general;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class CharacteristicController implements ObjectResourceController
{
	private static CharacteristicController instance;
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TYPE_ID = "type";
	public static final String COLUMN_SORT = "sort";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_CHARACTERIZED_ID = "characterizedId";
	public static final String COLUMN_EDITABLE = "editable";
	public static final String COLUMN_VISIBLE = "visible";

	private List keys;

	private CharacteristicController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				StorableObjectDatabase.COLUMN_ID,
				StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID,
				COLUMN_NAME,
				COLUMN_TYPE_ID,
				COLUMN_SORT,
				COLUMN_VALUE,
				COLUMN_CHARACTERIZED_ID,
				COLUMN_EDITABLE,
				COLUMN_VISIBLE
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CharacteristicController getInstance()
	{
		if (instance == null)
			instance = new CharacteristicController();
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
		if (object instanceof Characteristic)
		{
			Characteristic ch = (Characteristic)object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				result = ch.getId().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				result = ch.getCreated().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				result = ch.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				result = ch.getModified().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				result = ch.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				result = ch.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = ch.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SORT))
				result = Integer.toString(ch.getSort().value());
			else if (key.equals(COLUMN_VALUE))
				result = ch.getValue();
			else if (key.equals(COLUMN_CHARACTERIZED_ID))
				result = ch.getCharacterizedId().getIdentifierString();
			else if (key.equals(COLUMN_EDITABLE))
				result = Boolean.toString(ch.isEditable());
			else if (key.equals(COLUMN_VISIBLE))
				result = Boolean.toString(ch.isVisible());
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
