package com.syrus.AMFICOM.Client.Survey.Result.UI;

import java.util.LinkedList;
import java.util.List;
import com.syrus.AMFICOM.Client.General.UI.*;

public class ResultSetDisplayModel extends StubDisplayModel
{
	public ResultSetDisplayModel()
	{
		super();
	}

	List cols = new LinkedList();
	{
		cols.add("domain_id");
		cols.add("start_time");
		cols.add("end_time");
		cols.add("active");
	}
	
	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		String s = "";
		if(col_id.equals("start_time"))
			s = "Начало диапазона";
		if(col_id.equals("end_time"))
			s = "Конец диапазона";
		if(col_id.equals("domain_id"))
			s = "Домен";
		if(col_id.equals("active"))
			s = "Активный";
		return s;
	}

}

