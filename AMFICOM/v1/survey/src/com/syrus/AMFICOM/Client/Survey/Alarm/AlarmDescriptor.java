package com.syrus.AMFICOM.Client.Survey.Alarm;

import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Resource.Alarm.*;

public class AlarmDescriptor
{
	protected Alarm alarm;
	protected SystemEvent event;
	
	private Vector descriptorEvents = new Vector();

//----------------------------------
	public AlarmDescriptor(Alarm alarm)
	{
		this.alarm = alarm;

		event = alarm.getEvent();
	}

	public String getAlarmType()
	{
		return alarm.type_id;
	}

	public Alarm getAlarm()
	{
		return alarm;
	}

	public long getAlarmTime()
	{
		return event.created;
	}

	protected void add(AlarmDescriptorEvent e)
	{
		descriptorEvents.add(e);
	}
	
	public int getCount()
	{
		return descriptorEvents.size();
	}

	public AlarmDescriptorEvent get(int i)
	{
		return (AlarmDescriptorEvent )descriptorEvents.get(i);
	}
}

