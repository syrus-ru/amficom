package com.syrus.AMFICOM.Client.Survey.Alarm.Sorter;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import java.util.*;

public class AlarmSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"monitored_element_id", "string"},
		{"generated", "long"},
		{"status", "string"},
		{"alarm_type_name", "string"},
		{"source_name", "string"}
		};

	public AlarmSorter()
	{
		super();
	}

	public AlarmSorter(List dataSet)
	{
		super(dataSet);
	}

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		Alarm alarm = (Alarm )or;
		if (column.equals("monitored_element_id"))
		{
			return alarm.getModel().getColumnValue("monitored_element_id");
		}
		if (column.equals("alarm_type_name"))
		{
			return alarm.getModel().getColumnValue("alarm_type_name");
		}
		if (column.equals("source_name"))
		{
			return alarm.getModel().getColumnValue("source_name");
		}
		if (column.equals("status"))
		{
			return alarm.getModel().getColumnValue("status");
		}
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		if (column.equals("generated"))
			return ((Alarm )or).generated;
		return 0;
	}

}

