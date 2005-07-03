package com.syrus.AMFICOM.Client.Survey.Alarm.UI;

import java.util.*;

import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class AlarmController implements ObjectResourceController
{
	public static final String KEY_SOURCE_NAME = "source_name";
	public static final String KEY_GENERATED = "generated";
	public static final String KEY_TYPE_NAME = "alarm_type_name";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ME_ID = "monitored_element_id";

	private static AlarmController instance;

	private List keys;

	private AlarmController()
	{
		// empty private constructor
		String[] keysArray = new String[] {
				KEY_SOURCE_NAME,
				KEY_GENERATED,
				KEY_TYPE_NAME,
				KEY_STATUS,
				KEY_ME_ID
		};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AlarmController getInstance()
	{
		if (instance == null)
			instance = new AlarmController();
		return instance;
	}

	public List getKeys()
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(KEY_ME_ID))
			name = "Исследуемый объект";
		if (key.equals(KEY_GENERATED))
			name = "Время появления";
		if (key.equals(KEY_SOURCE_NAME))
			name = "Источник";
		if (key.equals(KEY_STATUS))
			name = "Статус";
		if (key.equals(KEY_TYPE_NAME))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;
		if (object instanceof Alarm)
		{
			Alarm alarm = (Alarm)object;
			if (key.equals(KEY_ME_ID))
				result = alarm.getMonitoredElementId();
			else if (key.equals(KEY_GENERATED))
				result = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(alarm.generated));
			else if (key.equals(KEY_SOURCE_NAME))
			{
				EventSource source = (EventSource)Pool.get(EventSource.typ, alarm.source_id);
				result = source.object_source_name;
			}
			else if (key.equals(KEY_STATUS))
			{
				switch(alarm.status.value())
				{
					case AlarmStatus._ALARM_STATUS_ASSIGNED:
						result = "Назначен";
						break;
					case AlarmStatus._ALARM_STATUS_DELETED:
						result = "Удален";
						break;
					case AlarmStatus._ALARM_STATUS_FIXED:
						result = "Исправлен";
						break;
					case AlarmStatus._ALARM_STATUS_GENERATED:
						result = "Новый";
				}
			}
			else if (key.equals(KEY_TYPE_NAME))
			{
				AlarmType type = (AlarmType)Pool.get(AlarmType.typ, alarm.type_id);
				result = type.getName();
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

