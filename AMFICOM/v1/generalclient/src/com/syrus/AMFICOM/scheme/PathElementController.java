package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.corba.PathElement;
import com.syrus.AMFICOM.general.*;

public final class PathElementController implements ObjectResourceController
{
	public static final String COLUMN_SEQUENTIAL_NUMBER = "number";
	public static final String COLUMN_SCHEME_ID = "scheme_id";
	public static final String COLUMN_SCHEME_ELEMENT_ID = "scheme_element_id";
	public static final String COLUMN_SCHEME_CABLE_THREAD_ID = "scheme_cable_thread_id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_START_ABSTRACT_PORT_ID = "start_abstract_port_id";
	public static final String COLUMN_END_ABSTRACT_PORT_ID = "end_abstract_port_id";

	private static PathElementController instance;

	private List keys;

	private PathElementController()
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
				COLUMN_SEQUENTIAL_NUMBER,
				COLUMN_SCHEME_ID,
				COLUMN_SCHEME_ELEMENT_ID,
				COLUMN_SCHEME_CABLE_THREAD_ID,
				COLUMN_TYPE,
				COLUMN_START_ABSTRACT_PORT_ID,
				COLUMN_END_ABSTRACT_PORT_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static PathElementController getInstance()
	{
		if (instance == null)
			instance = new PathElementController();
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
		if (key.equals(COLUMN_SEQUENTIAL_NUMBER))
			name = "№";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof PathElement)
		{
			PathElement pe = (PathElement)object;
			if (key.equals(COLUMN_ID))
				result = pe.id().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(pe.created());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = pe.creatorId().identifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(pe.modified());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = pe.modifierId().identifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = pe.description();
			else if (key.equals(COLUMN_NAME))
				result = pe.name();
			else if (key.equals(COLUMN_SEQUENTIAL_NUMBER))
				result = Integer.toString(pe.sequentialNumber());
			else if (key.equals(COLUMN_SCHEME_ID))
				result = pe.scheme().id().identifierString();
			else if (key.equals(COLUMN_SCHEME_ELEMENT_ID))
				result = pe.abstractSchemeElement().id().identifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_THREAD_ID))
				result = pe.schemeCableThread().id().identifierString();
			else if (key.equals(COLUMN_TYPE))
				result = Integer.toString(pe.type().value());
			else if (key.equals(COLUMN_START_ABSTRACT_PORT_ID))
				result = pe.startAbstractSchemePort().id().identifierString();
			else if (key.equals(COLUMN_END_ABSTRACT_PORT_ID))
				result = pe.endAbstractSchemePort().id().identifierString();

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


