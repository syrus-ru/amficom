package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import java.awt.*;
import java.text.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ElementaryTestModel extends ObjectResourceModel {

	public ElementaryTest etest;
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public ElementaryTestModel(ElementaryTest etest)
	{
		this.etest = etest;
	}
/*
	public Test test = new Test("");
	public long et_time = 0;

	public ElementaryTest(Test test, long et_time) {
		this.test = test;
		this.et_time = et_time;
	}
*/
	public String getColumnValue(String col_id)
	{

		String s = "";
		try
		{
			if(col_id.equals("status"))
			{
				switch(etest.status.value())
				{
					case TestStatus._TEST_STATUS_PROCESSING:
						s = LangModelSchedule.getString("Running");
						break;
					case TestStatus._TEST_STATUS_COMPLETED:
						s = LangModelSchedule.getString("Done");
						break;
					case TestStatus._TEST_STATUS_SCHEDULED:
						s = LangModelSchedule.getString("ReadyToRun");
						break;
				}
			}
			/*if(col_id.equals("local_id"))
				s = test.local_id;
			if(col_id.equals("kis_id"))
				s = test.kis_id;*/
			if(col_id.equals("start_time"))
				s = sdf.format(new Date(etest.et_time));
			if(col_id.equals("kis_id"))
			{
				s = String.valueOf(etest.count);
			}
			/*if(col_id.equals("test_type_id"))
				s = test.test_type_id;
			if(col_id.equals("request_id"))
				s = this.test.request_id;*/
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - Test");
			s = "";
		}
		return s;
	}

	/*public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("status"))
			return false;
		if(col_id.equals("local_id"))
			return false;
		if(col_id.equals("kis_id"))
			return false;
		if(col_id.equals("start_time"))
			return false;
		if(col_id.equals("temporal_type"))
			return false;
		if(col_id.equals("test_type_id"))
			return false;
		if(col_id.equals("request_id"))
			return false;
		return false;
	}*/

	public Component getColumnRenderer(String col_id)
	{
		if(col_id.equals("status"))
			return new TextFieldEditor(getColumnValue("status"));
		if(col_id.equals("kis_id"))
			return new TextFieldEditor(etest.test.kis_id);
		if(col_id.equals("start_time"))
			return new TextFieldEditor(sdf.format(new Date(etest.et_time)));
		if(col_id.equals("temporal_type"))
			return new TextFieldEditor(getColumnValue("temporal_type"));
		if(col_id.equals("test_type_id"))
			return new ObjectResourceComboBox("testtype", etest.test.test_type_id);
		if(col_id.equals("request_id"))
			return new ObjectResourceComboBox("testrequest", etest.test.request_id);
		return null;
	}

	/*public String getColumnName (String col_id)
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
	}*/

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}
}

