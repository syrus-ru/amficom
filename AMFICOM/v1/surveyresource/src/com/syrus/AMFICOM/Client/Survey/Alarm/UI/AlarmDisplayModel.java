package com.syrus.AMFICOM.Client.Survey.Alarm.UI;

import java.awt.Color;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class AlarmDisplayModel extends StubDisplayModel
{
	public AlarmDisplayModel()
	{
		super();
	}

	private List cols = new LinkedList();
	{
//		cols.add("id");
		cols.add("source_name");
		cols.add("generated");
		cols.add("alarm_type_name");
		cols.add("status");
		cols.add("monitored_element_id");
	}
	
	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("source_name"))
			return "Источник";
		if(col_id.equals("status"))
			return "Статус";
		if(col_id.equals("generated"))
			return "Время появления";
		if(col_id.equals("alarm_type_name"))
			return "Имя";
		if(col_id.equals("monitored_element_id"))
			return "Исследуемый объект";
		return "leer";
	}

	public boolean isColumnColored(String col_id)
	{
		return true;
	}

	public Color getColumnColor(ObjectResource or, String col_id)
	{
		Alarm alarm = (Alarm )or;
		if(alarm.status == AlarmStatus.ALARM_STATUS_GENERATED)
			return new Color(255, 100, 100);
		else
		if(alarm.status == AlarmStatus.ALARM_STATUS_ASSIGNED)
			return new Color(200, 255, 200);
		return super.getColumnColor(or, col_id);
	}
}

