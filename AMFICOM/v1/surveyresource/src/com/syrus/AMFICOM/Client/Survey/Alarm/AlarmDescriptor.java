package com.syrus.AMFICOM.Client.Survey.Alarm;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.Alarm.*;

public class AlarmDescriptor
{
	protected Alarm alarm;
	protected SystemEvent event;
	
	private List descriptorEvents = new ArrayList();

//----------------------------------
	public AlarmDescriptor(Alarm alarm)
	{
		this.alarm = alarm;

		this.event = alarm.getEvent();
	}

	public String getAlarmType()
	{
		return this.alarm.type_id;
	}

	public Alarm getAlarm()
	{
		return this.alarm;
	}

	public long getAlarmTime()
	{
		return this.event.created;
	}

	protected void add(AlarmDescriptorEvent event)
	{
		this.descriptorEvents.add(event);
	}
	
	public int getCount()
	{
		return this.descriptorEvents.size();
	}

	public AlarmDescriptorEvent get(int index)
	{
		return (AlarmDescriptorEvent )this.descriptorEvents.get(index);
	}
}

