package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class MonitoredElementController implements ObjectResourceController
{
	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	public static final String COLUMN_SORT = "sort";
	public static final String COLUMN_LOCAL_ADDRESS = "local_address";

	private static MonitoredElementController instance;

	private List keys;

	private MonitoredElementController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				COLUMN_ID,
				COLUMN_CREATED,
				COLUMN_CREATOR_ID,
				COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID,
				COLUMN_NAME,
				COLUMN_MEASUREMENT_PORT_ID,
				COLUMN_SORT,
				COLUMN_LOCAL_ADDRESS
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MonitoredElementController getInstance()
	{
		if (instance == null)
			instance = new MonitoredElementController();
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
		if (object instanceof MonitoredElement)
		{
			MonitoredElement me = (MonitoredElement)object;
			if (key.equals(COLUMN_ID))
				result = me.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = me.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = me.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = me.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = me.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				result = me.getName();
			else if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				result = me.getMeasurementPortId().getIdentifierString();
			else if (key.equals(COLUMN_SORT))
				result = Integer.toString(me.getSort().value());
			else if (key.equals(COLUMN_LOCAL_ADDRESS))
				result = me.getLocalAddress();
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
