package com.syrus.AMFICOM.Client.Schedule.Sorter;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import java.util.*;

public class TestSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"kis_id", "string"},
		{"local_id", "string"},
		{"start_time", "long"},
		{"status", "string"},
		{"temporal_type", "string"},
		{"test_type_id", "string"},
		{"port_id", "string"}
		};

	public TestSorter()
	{
		super();
	}

	public TestSorter(List dataSet)
	{
		super(dataSet);
	}

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		Test test = (Test )or;
		if (column.equals("kis_id"))
		{
			return test.getModel().getColumnValue("kis_id");
		}
		if (column.equals("local_id"))
		{
			return test.getModel().getColumnValue("local_id");
		}
		if (column.equals("port_id"))
		{
			return test.getModel().getColumnValue("port_id");
		}
		if (column.equals("test_type_id"))
		{
			return test.getModel().getColumnValue("test_type_id");
		}
		if (column.equals("temporal_type"))
		{
			return test.getModel().getColumnValue("temporal_type");
		}
		if (column.equals("status"))
		{
			return test.getModel().getColumnValue("status");
		}
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		if (column.equals("start_time"))
			return ((Test )or).getStartTime();
		return 0;
	}

}

