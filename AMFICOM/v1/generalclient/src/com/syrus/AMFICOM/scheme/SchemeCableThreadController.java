package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemeCableThreadController implements ObjectResourceController
{
	public static final String COLUMN_THREAD_ID = "thread_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";
	public static final String COLUMN_SCHEME_CABLE_LINK_ID = "scheme_cable_link_id";

	private static SchemeCableThreadController instance;

	private List keys;

	private SchemeCableThreadController()
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
			name = "��������";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof SchemeCableThread)
		{
			SchemeCableThread thread = (SchemeCableThread)object;
			if (key.equals(COLUMN_ID))
				result = thread.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(thread.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = thread.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(thread.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = thread.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = thread.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = thread.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = thread.getCableThreadType().getId().getIdentifierString();
			else if (key.equals(COLUMN_THREAD_ID))
				result = thread.getLink().getId().getIdentifierString();
			else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID))
				result = thread.getSourceSchemePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID))
				result = thread.getTargetSchemePort().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_LINK_ID))
				result = thread.getParentSchemeCableLink().getId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(thread.getCharacteristics().size());
				for (Iterator it = thread.getCharacteristics().iterator(); it.hasNext(); ) {
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
