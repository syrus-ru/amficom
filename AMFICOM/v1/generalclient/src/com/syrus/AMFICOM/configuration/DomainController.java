package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.resource.*;

public final class DomainController implements ObjectResourceController
{

	private static DomainController instance;

	private List keys;

	private DomainController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static DomainController getInstance()
	{
		if (instance == null)
			instance = new DomainController();
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
		if (object instanceof Domain)
		{
			Domain domain = (Domain)object;
			if (key.equals(COLUMN_NAME))
				result = domain.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				result = domain.getDescription();
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
