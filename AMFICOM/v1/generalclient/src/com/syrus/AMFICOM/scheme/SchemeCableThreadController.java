package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.corba.SchemeCableThread;
import com.syrus.AMFICOM.general.*;

public final class SchemeCableThreadController implements ObjectResourceController
{
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_THREAD_ID = "thread_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";
	public static final String COLUMN_SCHEME_CABLE_LINK_ID = "scheme_cable_link_id";
	public static final String COLUMN_CHARACTERISTICS = "characteristics";

	private static SchemeCableThreadController instance;

	private List keys;

	private SchemeCableThreadController()
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
				COLUMN_TYPE_ID,
				COLUMN_THREAD_ID,
				COLUMN_SOURCE_SCHEME_PORT_ID,
				COLUMN_TARGET_SCHEME_PORT_ID,
				COLUMN_SCHEME_CABLE_LINK_ID,
				COLUMN_CHARACTERISTICS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeCableThreadController getInstance()
	{
		if (instance == null)
			instance = new SchemeCableThreadController();
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
		if (object instanceof SchemeCableThread)
		{
			SchemeCableThread thread = (SchemeCableThread)object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				result = thread.id().toString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				result = Long.toString(thread.created());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				result = thread.creatorId().identifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				result = Long.toString(thread.modified());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				result = thread.modifierId().identifierString();
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				result = thread.description();
			else if (key.equals(COLUMN_NAME))
				result = thread.name();
			else if (key.equals(COLUMN_TYPE_ID))
				result = thread.cableThreadTypeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_THREAD_ID))
				result = thread.threadImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID))
				result = thread.sourceSchemePort().id().identifierString();
			else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID))
				result = thread.targetSchemePort().id().identifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_LINK_ID))
				result = thread.schemeCablelink().id().identifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(thread.characteristics().length);
				for (Iterator it = thread.characteristicsImpl().getValue().iterator(); it.hasNext(); ) {
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
