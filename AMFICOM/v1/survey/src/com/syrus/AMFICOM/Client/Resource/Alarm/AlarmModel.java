package com.syrus.AMFICOM.Client.Resource.Alarm;

import java.util.*;
import java.text.*;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;

public class AlarmModel extends ObjectResourceModel
{
	Alarm alarm;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

	public AlarmModel(Alarm alarm)
	{
		this.alarm = alarm;
	}

	public PropertiesPanel getPropertyPane()
	{
		return new GeneralPanel();
	}

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = alarm.id;
			if(col_id.equals("source_name"))
				s = Pool.getName(EventSource.typ, alarm.source_id);
			if(col_id.equals("status"))
			{
				if(alarm.status.equals(AlarmStatus.ALARM_STATUS_GENERATED))
					s = LangModelSurvey.getString("New");
				else
				if(alarm.status.equals(AlarmStatus.ALARM_STATUS_ASSIGNED))
					s = LangModelSurvey.getString("Assigned");
				else
				if(alarm.status.equals(AlarmStatus.ALARM_STATUS_FIXED))
					s = LangModelSurvey.getString("Fixed");
//				s = LangModelSurvey.String("label" + String.valueOf(alarm.status));
			}
			if(col_id.equals("generated"))
				s = sdf.format(new Date(alarm.generated));
			if(col_id.equals("alarm_type_name"))
				s = Pool.getName(AlarmType.typ, alarm.type_id);
			if(col_id.equals("monitored_element_id"))
			{
				SystemEvent event = alarm.getEvent();
				EventSource source = event.getSource();
				s = Pool.getName(MonitoredElement.typ, alarm.getMonitoredElementId());
			}
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - Alarm");
			s = "";
		}
		if(s == null)
			s = "";

		return s;
	}
}

