package com.syrus.AMFICOM.Client.Survey.Result.UI;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import com.syrus.AMFICOM.Client.General.UI.*;

public class TestRequestDisplayModel extends StubDisplayModel
{
	public TestRequestDisplayModel()
	{
		super();
	}
	
	List cols = new LinkedList();
	{
		cols.add("id");
		cols.add("time");
		cols.add("tests");
		cols.add("start_time");
		cols.add("end_time");
		cols.add("status");
	}
	
	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("time"))
			return "Запрос";
		if(col_id.equals("tests"))
			return "Число тестов";
		if(col_id.equals("start_time"))
			return "Начало";
		if(col_id.equals("end_time"))
			return "Окончание";
		if(col_id.equals("status"))
			return "Статус";
		return "leer";
	}
}

