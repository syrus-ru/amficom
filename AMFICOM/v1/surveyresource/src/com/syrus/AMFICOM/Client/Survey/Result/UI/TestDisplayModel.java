package com.syrus.AMFICOM.Client.Survey.Result.UI;

import java.util.LinkedList;
import java.util.List;
import com.syrus.AMFICOM.Client.General.UI.*;

public class TestDisplayModel extends StubDisplayModel
{
	public TestDisplayModel()
	{
		super();
	}

	List cols = new LinkedList();
	{
		cols.add("status");
		cols.add("local_id");
		cols.add("kis_id");
		cols.add("start_time");
		cols.add("temporal_type");
		cols.add("test_type_id");
//		cols.add("request_id");
	}
	
	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		String s = "";
		if(col_id.equals("status"))
			s = "Статус";
		if(col_id.equals("local_id"))
			s = "Волокно";
		if(col_id.equals("kis_id"))
			s = "КИС";
		if(col_id.equals("start_time"))
			s = "Время первого теста";
		if(col_id.equals("temporal_type"))
			s = "Временной тип";
		if(col_id.equals("test_type_id"))
			s = "Тип измерений";
		if(col_id.equals("request_id"))
			s = "Запрос";
		return s;
	}

}

