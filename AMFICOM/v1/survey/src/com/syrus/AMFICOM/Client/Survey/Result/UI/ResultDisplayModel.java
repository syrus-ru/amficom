package com.syrus.AMFICOM.Client.Survey.Result.UI;

import java.util.Vector;
import com.syrus.AMFICOM.Client.General.UI.*;

public class ResultDisplayModel extends StubDisplayModel
{
	public ResultDisplayModel()
	{
		super();
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("created");
		cols.add("result_type");
		cols.add("action_id");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		String s = "";
		if(col_id.equals("created"))
			s = "Время";
		if(col_id.equals("result_type"))
			s = "Тип результата";
		if(col_id.equals("action_id"))
			s = "Операция";
		return s;
	}

}

