package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Survey.ClientTest_Transferable;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.CORBA.Survey.*;

import com.syrus.AMFICOM.Client.General.UI.*;

public class TestDisplayModel extends StubDisplayModel
{

	public TestDisplayModel()
	{
		super();
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("temporal_type");
		cols.add("kis_id");
		cols.add("port_id");
		cols.add("local_id");
		cols.add("test_type_id");
		cols.add("start_time");
		cols.add("status");


		return cols;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("status"))
			s = LangModelScheduleOld.String("labelStatus");
		if(col_id.equals("local_id"))
			s = LangModelScheduleOld.String("ORPath1");
		if(col_id.equals("kis_id"))
			s = LangModelScheduleOld.String("ORKIS");
		if(col_id.equals("start_time"))
			s = LangModelScheduleOld.String("labelStartTime");
		if(col_id.equals("temporal_type"))
			s = LangModelScheduleOld.String("labelTimeType");
		if(col_id.equals("test_type_id"))
			s = LangModelScheduleOld.String("labelIzmerType");
		if(col_id.equals("request_id"))
			s = LangModelScheduleOld.String("labelZapros");
		if(col_id.equals("port_id"))
			s = LangModelScheduleOld.String("ORPort");
		return s;
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("status"))
			return 35;
		if(col_id.equals("local_id"))
			return 129;
		if(col_id.equals("kis_id"))
			return 129;
		if(col_id.equals("start_time"))
			return 70;
		if(col_id.equals("temporal_type"))
			return 50;
		if(col_id.equals("test_type_id"))
			return 129;
		if(col_id.equals("request_id"))
			return 129;
		if(col_id.equals("port_id"))
			return 129;
		return 100;
	}

	public boolean isColumnEditable(String col_id)
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
		if(col_id.equals("port_id"))
			return false;
		return false;
	}

	public boolean isColumnColored(String col_id)
	{
		return true;
	}

	public Color getColumnColor(ObjectResource or, String col_id)
	{
		Color color = Color.white;
		if(or instanceof Test)
		{
			if (col_id.equals("temporal_type"))
			{
				if (((Test)or).elementary_test_alarms.length!=0)
				{
					ElementaryTestAlarm[] eta = ((Test)or).elementary_test_alarms;
					int a = 0;
					while (a < eta.length)
					{
						Alarm alarm = (Alarm )Pool.get(Alarm.typ, eta[a].alarm_id);
						if(alarm != null)
						{
							if(alarm.type_id.equals("rtutestalarm"))
							{
								return Color.red;
							}
							else if(alarm.type_id.equals("rtutestwarning"))
								 color = Color.yellow;
						}
						a++;
					}
				}
			}
			else if (!col_id.equals("temporal_type"))
			{
				if(((Test)or).temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME)
				{
					color = Color.white;
				}
				else if (((Test)or).temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL)
				{
					color = Color.green;
				}
				else if (((Test)or).temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE)
				{
					color = Color.blue;
				}
			}
		}
		else if(or instanceof ElementaryTest)
		{
			ElementaryTestAlarm[] eta = ((ElementaryTest)or).test.elementary_test_alarms;
			int a = 0;
			while (a < eta.length)
			{
				if ( ((ElementaryTest)or).et_time == eta[a].elementary_start_time)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, eta[a].alarm_id);
					if (col_id.equals("temporal_type"))
					{
						if(alarm != null)
						{
							if(alarm.type_id.equals("rtutestalarm"))
								color = Color.red;
							else if(alarm.type_id.equals("rtutestwarning"))
								 color = Color.yellow;
						}
					}

					break;
				}
				a++;
			}
			if (a == eta.length)
				color = Color.white;
		}
		return color;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
//		Test test = (Test )or;
		ObjectResourceModel mod = or.getModel();
		String val = mod.getColumnValue(col_id);
		return new JLabelRenderer(val);
//		return (PropertyRenderer )mod.getColumnRenderer(col_id);
/*
		if(col_id.equals("status"))
			return new TextFieldEditor(mod.getColumnValue("status"));
		if(col_id.equals("local_id))
			return new TextFieldEditor(test.local_id);
		if(col_id.equals("kis_id"))
			return new TextFieldEditor(test.kis_id);
		if(col_id.equals("start_time"))
			return new TextFieldEditor(sdf.format(new Date(test.start_time)));
		if(col_id.equals("temporal_type"))
			return new TextFieldEditor(mod.getColumnValue("temporal_type"));
		if(col_id.equals("test_type_id"))
			return new ObjectResourceComboBox("testtype", test.test_type_id);
		if(col_id.equals("request_id"))
			return new ObjectResourceComboBox("testrequest", test.request_id);
		return null;
*/
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}