package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class ResultModel extends ObjectResourceModel
{
	public Result r;

	public ResultModel(Result r)
	{
		this.r = r;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = r.id;
			if(col_id.equals("result_type"))
			{
				Hashtable actions = new Hashtable();
				actions.put("analysis", "Анализ");
				actions.put("modeling", "Моделирование");
				actions.put("evaluation", "Оценка");
				actions.put("test", "Тестирование");
				actions.put("testrequest", "Запрос на тестирование");
				s = (String)actions.get(r.result_type);
				if (s == null)
					s = "";
			}
			if(col_id.equals("created"))
				s = sdf.format(new Date(r.elementary_start_time));
			if(col_id.equals("user_id"))
				s = r.user_id;
			if(col_id.equals("action_id"))
			{
				s = r.action_id;
			}
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - Result");
			s = "";
		}
		return s;
	}

	public Component getColumnRenderer(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if(col_id.equals("id"))
			return new TextFieldEditor(r.getId());
		if(col_id.equals("result_type"))
			return new TextFieldEditor(r.result_type);
		if(col_id.equals("created"))
			return new TextFieldEditor(sdf.format(new Date(r.elementary_start_time)));
		if(col_id.equals("user_id"))
			return new TextFieldEditor(r.user_id);
		if(col_id.equals("action_id"))
			return new TextFieldEditor(r.action_id);
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}
}