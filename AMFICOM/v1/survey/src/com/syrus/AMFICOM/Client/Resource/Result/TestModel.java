package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Component;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;

public class TestModel extends ObjectResourceModel
{
	public Test test;
  public TestModel(Test test)
  {
	this.test = test;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("status"))
			{
				switch(test.status.value())
				{
					case TestStatus._TEST_STATUS_PROCESSING:
						s = LangModelSurvey.String("labelDoing");
						break;
					case TestStatus._TEST_STATUS_COMPLETED:
						s = LangModelSurvey.String("labelDone");
						break;
					case TestStatus._TEST_STATUS_SCHEDULED:
						s = LangModelSurvey.String("labelReadyToDo");
						break;
				}
			}
			if(col_id.equals("local_id"))
			{
				TransmissionPath path;
				for(Enumeration e = Pool.getHash(TransmissionPath.typ).elements();
					e.hasMoreElements();)
				{
					path = (TransmissionPath )e.nextElement();
					if(path.monitored_element_id.equals(test.monitored_element_id))
					{
						s = path.getName();
						break;
					}
				}
//				s = test.local_id;
			}
			if(col_id.equals("port_id"))
			{
				TransmissionPath path;
				for(Enumeration e = Pool.getHash(TransmissionPath.typ).elements();
					e.hasMoreElements();)
				{
					path = (TransmissionPath )e.nextElement();
					if(path.monitored_element_id.equals(test.monitored_element_id))
					{
						s = Pool.getName("accessport", path.access_port_id);
						break;
					}
				}
//				s = test.local_id;
			}
			if(col_id.equals("kis_id"))
				s = Pool.getName("kis", test.kis_id);
			if(col_id.equals("start_time"))
				s = sdf.format(new Date(test.start_time));
			if(col_id.equals("temporal_type"))
			{
				switch(test.temporal_type.value())
				{
					case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
						s = LangModelSurvey.String("labelOnetime");
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
						s = LangModelSurvey.String("labelPeriod");
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
						s = LangModelSurvey.String("labelTimeTable");
						break;
				}
			}
			if(col_id.equals("test_type_id"))
				s = Pool.getName("testtype", test.test_type_id);
			if(col_id.equals("request_id"))
				s = Pool.getName("testrequest", test.request_id);
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Test");
			s = "";
		}
		return s;
	}

	public Component getColumnRenderer(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if(col_id.equals("status"))
			return new TextFieldEditor(getColumnValue("status"));
		if(col_id.equals("kis_id"))
			return new TextFieldEditor(test.kis_id);
		if(col_id.equals("start_time"))
			return new TextFieldEditor(sdf.format(new Date(test.start_time)));
		if(col_id.equals("temporal_type"))
			return new TextFieldEditor(getColumnValue("temporal_type"));
		if(col_id.equals("test_type_id"))
			return new ObjectResourceComboBox("testtype", test.test_type_id);
		if(col_id.equals("request_id"))
			return new ObjectResourceComboBox("testrequest", test.request_id);
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}
}