package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemePathController extends ObjectResourceController
{
	public static final String COLUMN_START_DEVICE_ID = "start_device_id";
	public static final String COLUMN_END_DEVICE_ID = "end_device_id";
	public static final String COLUMN_PATH_ID = "path_id";
//	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_SCHEME_ID = "scheme_id";
	public static final String COLUMN_LINK_IDS = "link_ids";

	private static SchemePathController instance;

	private List keys;

	private SchemePathController()
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
				COLUMN_START_DEVICE_ID,
				COLUMN_END_DEVICE_ID,
				COLUMN_PATH_ID,
				COLUMN_TYPE_ID,
				COLUMN_SCHEME_ID,
				COLUMN_LINK_IDS,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemePathController getInstance()
	{
		if (instance == null)
			instance = new SchemePathController();
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
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof SchemePath)
		{
			SchemePath path = (SchemePath)object;
			if (key.equals(COLUMN_ID))
				result = path.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(path.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = path.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(path.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = path.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = path.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = path.getName();
			else if (key.equals(COLUMN_PATH_ID))
				result = path.getTransmissionPath().getId().getIdentifierString();
			else if (key.equals(COLUMN_START_DEVICE_ID))
				result = path.getStartSchemeElement().getId().getIdentifierString();
			else if (key.equals(COLUMN_END_DEVICE_ID))
				result = path.getEndSchemeElement().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_ID))
				result = path.getScheme().getId().getIdentifierString();
			else if (key.equals(COLUMN_LINK_IDS)) {
				PathElement[] pes = path.getPathElementsAsArray();
				List res = new ArrayList(pes.length);
				for (int i = 0; i < pes.length; i++) {
					res.add(pes[i].getId().getIdentifierString());
				}
				result = res;
			}
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(path.getCharacteristics().size());
				for (Iterator it = path.getCharacteristics().iterator(); it.hasNext(); ) {
					Characteristic ch = (Characteristic)it.next();
					res.add(ch.getId().getIdentifierString());
				}
				result = res;
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
		Class clazz = String.class;
		return clazz;
	}
}
