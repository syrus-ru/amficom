package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.Filter.*;
import com.syrus.AMFICOM.CORBA.General.*;
import java.util.*;

public class AlarmFilter extends ObjectResourceFilter
{

	public AlarmFilter()
	{
		super();
	}

	public Vector getFilterColumns()
	{
		Vector vec = new Vector();
		vec.add("alarm_Source");
		vec.add("alarm_Monitoredelement");
		vec.add("alarm_Type");
		vec.add("alarm_Status");
		vec.add("alarm_Time");

		vec.add("alarm_Assigned");
		vec.add("alarm_Fixed_when");
		vec.add("alarm_Assigned_to");
		vec.add("alarm_Fixed_by");

/*		vec.add("source");
		vec.add("time");
		vec.add("type");
		vec.add("status");
		vec.add("monitoredelement");*/

		return vec;
	}

	public String getFilterColumnName(String col_id)
	{
		if(col_id == null)
			return "";

		return LangModelSurvey.String(col_id);
/*		if(col_id.equals("source"))
			return LangModelSurvey.String("labelsource");
		if(col_id.equals("type"))
			return LangModelSurvey.String("labelAlarmType");
		if(col_id.equals("status"))
			return LangModelSurvey.String("labelStatus");
		if(col_id.equals("time"))
			return LangModelSurvey.String("labelalarmtime");
		if(col_id.equals("monitoredelement"))
			return LangModelSurvey.String("labelmonitoredelement");
		return "";*/
	}

	public String[] getColumnFilterTypes(String col_id)
	{
		if(col_id == null)
			return new String[] {};
		if(col_id.equals("alarm_Source"))
			return new String[] {"string", "list"};
		if(col_id.equals("alarm_Type"))
			return new String[] {"list"};
		if(col_id.equals("alarm_Status"))
			return new String[] {"list"};
		if(col_id.equals("alarm_Time"))
			return new String[] {"time"};
		if(col_id.equals("alarm_Monitoredelement"))
			return new String[] {"string", "list"};

		if(col_id.equals("alarm_Assigned"))
			return new String[] {"time"};
		if(col_id.equals("alarm_Fixed_when"))
			return new String[] {"time"};
		if(col_id.equals("alarm_Assigned_to"))
			return new String[] {"string", "list"};
		if(col_id.equals("alarm_Fixed_by"))
			return new String[] {"string", "list"};

		return new String[] {};
	}

	public FilterPanel getColumnFilterPanel(String col_id, String type)
	{
		if(col_id == null)
			return null;
			if(type.equals("numeric"))
				return new GeneralEquationFilterPanel();
			if(type.equals("time"))
				return new GeneralTimeFilterPanel();
			if(type.equals("string"))
				return new GeneralStringFilterPanel();
			if(type.equals("range"))
				return new GeneralRangeFilterPanel();
			if(type.equals("list"))
			{	if(col_id.equals("alarm_Source"))
					return new GeneralTreeFilterPanel(new AlarmSourceTree());
				if(col_id.equals("alarm_Monitoredelement"))
					return new GeneralTreeFilterPanel(new METree());
				if(col_id.equals("alarm_Type"))
					return new GeneralTreeFilterPanel(new AlarmTypeTree());
				if(col_id.equals("alarm_Status"))
					return new GeneralTreeFilterPanel(new AlarmStatusTree());
			}
		return null;
	}

	public boolean expression(FilterExpression expr, ObjectResource or)
	{
		boolean result = false;
		Alarm a = (Alarm )or;
		Vector vec = expr.getVec();
		String type = (String) vec.elementAt(0);
		if (type.equals("numeric"))
		{
			if (expr.getId().equals("time"))
			{
				if (((String)vec.elementAt(1)).equals("="))
				{
					if (a.generated == Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
				else if (((String)vec.elementAt(1)).equals(">"))
				{
					if (a.generated > Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
				else if (((String)vec.elementAt(1)).equals("<"))
				{
					if (a.generated < Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
			}
		}
		else if (type.equals("time"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.generated > Long.parseLong((String)vec.elementAt(1)) &&  a.generated < Long.parseLong((String)vec.elementAt(2)))
				{
					result = true;
				}
			}
		}
		else if (type.equals("range"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.generated > Long.parseLong((String)vec.elementAt(1)) &&  a.generated < Long.parseLong((String)vec.elementAt(2)))
					result = true;
			}
		}
		else if (type.equals("string"))
		{
			String substring = (String)vec.elementAt(1);
			if (expr.getId().equals("alarm_Source"))
			{
				String name = Pool.getName(EventSource.typ, a.getSourceId());
				result = SearchSubstring(name, substring);
			}
			else if (expr.getId().equals("alarm_Monitoredelement"))
			{
				MonitoredElement me;
				for(Enumeration e = Pool.getHash(MonitoredElement.typ).elements();
					e.hasMoreElements();)
				{
					me = (MonitoredElement )e.nextElement();
					if(me.getId().equals(a.getMonitoredElementId()))
					{
						result = SearchSubstring(me.getName(), substring);
					}
				}
			}
		}
		else if (type.equals("list"))
		{
			TreeModelClone tree = (TreeModelClone)vec.elementAt(1);
			if (expr.getId().equals("alarm_Monitoredelement"))
			{
				FilterTreeNode mmtn = (FilterTreeNode )tree.getRoot();
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
					{
						FilterTreeNode down_mte = (FilterTreeNode )enum.nextElement();

						for(Enumeration enu = down_mte.children(); enu.hasMoreElements();)
						{
							FilterTreeNode down_mte2 = (FilterTreeNode )enu.nextElement();

							if (a.getMonitoredElementId().equals(down_mte2.id) && (down_mte2.state == 2))
								result = true;
						}
					}
				}
			}
			if (expr.getId().equals("alarm_Source"))
			{
				FilterTreeNode mmtn = (FilterTreeNode )tree.getRoot();
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
					{
						FilterTreeNode down_mte = (FilterTreeNode )enum.nextElement();
						if (a.getSourceId().equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("alarm_Type"))
			{
				FilterTreeNode mmtn = (FilterTreeNode )tree.getRoot();
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
					{
						FilterTreeNode down_mte = (FilterTreeNode )enum.nextElement();
						if (a.type_id.equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("alarm_Status"))
			{
				FilterTreeNode mmtn = (FilterTreeNode )tree.getRoot();
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
					{
						FilterTreeNode down_mte = (FilterTreeNode )enum.nextElement();
						String stat = "";
						if (a.status.equals(AlarmStatus.ALARM_STATUS_GENERATED))
							stat = "GENERATED";
						else if (a.status.equals(AlarmStatus.ALARM_STATUS_ASSIGNED))
							stat = "ASSIGNED";
						else if (a.status.equals(AlarmStatus.ALARM_STATUS_FIXED))
							stat = "FIXED";
						if (down_mte.id.equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
		}
		return result;
	}

	public Object clone()
	{
		AlarmFilter af = new AlarmFilter();
		af.clearCriteria();
		for(int i = 0; i < getCriteria().size(); i++)
		{
			FilterExpression fe = (FilterExpression )getCriteria().get(i);
			af.addCriterium((FilterExpression )fe.clone());
		}
		af.logicScheme = (LogicScheme )logicScheme.clone(af);
		return af;
	}
}
