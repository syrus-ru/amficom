package com.syrus.AMFICOM.Client.Resource.Alarm;

import java.util.*;

import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;

public class AlarmModel extends ObjectResourceModel {

	private Alarm	alarm;

	public AlarmModel(Alarm alarm) {
		this.alarm = alarm;
	}

	public PropertiesPanel getPropertyPane() {
		return new GeneralPanel();
	}

	public String getColumnValue(String colId) {
		String s = null;
		try {
			if (colId.equals("id"))
				s = alarm.id;
			if (colId.equals(ObjectResourceModel.COLUMN_SOURCE_NAME))
				s = Pool.getName(EventSource.typ, alarm.source_id);
			if (colId.equals(ObjectResourceModel.COLUMN_STATUS)) {
				if (alarm.status.equals(AlarmStatus.ALARM_STATUS_GENERATED))
					s = LangModelSurvey.getString("New");
				else if (alarm.status.equals(AlarmStatus.ALARM_STATUS_ASSIGNED))
					s = LangModelSurvey.getString("Assigned");
				else if (alarm.status.equals(AlarmStatus.ALARM_STATUS_FIXED))
					s = LangModelSurvey.getString("Fixed");
				//				s = LangModelSurvey.String("label" +
				// String.valueOf(alarm.status));
			}
			if (colId.equals(ObjectResourceModel.COLUMN_GENERATED))
				s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(alarm.generated));
			if (colId.equals(ObjectResourceModel.COLUMN_ALARM_TYPE_NAME))
				s = Pool.getName(AlarmType.typ, alarm.type_id);
			if (colId.equals(ObjectResourceModel.COLUMN_ME_ID)) {
				//SystemEvent event = alarm.getEvent();
				//EventSource source = event.getSource();
				s = Pool.getName(MonitoredElement.typ, alarm.getMonitoredElementId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (s == null) ? "" : s;
	}
}

