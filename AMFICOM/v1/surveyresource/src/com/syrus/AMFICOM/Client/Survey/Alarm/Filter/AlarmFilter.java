package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSource;

import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.CORBA.General.AlarmStatus;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;

public class AlarmFilter extends ObjectResourceFilter
{
	private List filterColumns;

	public AlarmFilter()
	{
		super();
		this.resource_typ = this.getClass().getName();
	}

	public AlarmFilter(LogicScheme ls)
	{
		super(ls);
		this.resource_typ = this.getClass().getName();
	}


	public List getFilterColumns()
	{
		if (this.filterColumns==null)
		{
			this.filterColumns = new ArrayList();
			this.filterColumns.add("Event_source");
			this.filterColumns.add("Monitored_element");
			this.filterColumns.add("Event_type");
			this.filterColumns.add("Status");
			this.filterColumns.add("time");

			this.filterColumns.add("Execution_start");
			this.filterColumns.add("Execution_finish");
			this.filterColumns.add("Chief_executor");
			this.filterColumns.add("Executor");
		}

//		vec.add("source");
//		vec.add("time");
//		vec.add("type");
//		vec.add("status");
//		vec.add("monitoredelement");

		return this.filterColumns;
	}

	public String getFilterColumnName(String colId)
	{
		String result = null;
		if(colId == null)
			result = "";
		else result = LangModelSurvey.getString(colId);
		return result;
//		if(colId.equals("source"))
//			return LangModelSurvey.String("labelsource");
//		if(colId.equals("type"))
//			return LangModelSurvey.String("labelAlarmType");
//		if(colId.equals("status"))
//			return LangModelSurvey.String("labelStatus");
//		if(colId.equals("time"))
//			return LangModelSurvey.String("labelalarmtime");
//		if(colId.equals("monitoredelement"))
//			return LangModelSurvey.String("labelmonitoredelement");
//		return "";
	}

	public String[] getColumnFilterTypes(String colId)
	{
		if(colId == null)
			return new String[] {};
		if(colId.equals("Event_source"))
			return new String[] {"string", "list"};
		if(colId.equals("Event_type"))
			return new String[] {"list"};
		if(colId.equals("Status"))
			return new String[] {"list"};
		if(colId.equals("time"))
			return new String[] {"time"};
		if(colId.equals("Monitored_element"))
			return new String[] {"string", "list"};

		if(colId.equals("Execution_start"))
			return new String[] {"time"};
		if(colId.equals("Execution_finish"))
			return new String[] {"time"};
		if(colId.equals("Chief_executor"))
			return new String[] {"string"};//, "list"
		if(colId.equals("Executor"))
			return new String[] {"string"};//, "list"

		return new String[] {};
	}

	public FilterPanel getColumnFilterPanel(String colId, String type)
	{
		if(colId == null)
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
			{	if(colId.equals("Event_source"))
					return new GeneralTreeFilterPanel(new AlarmSourceTree());
				if(colId.equals("Monitored_element"))
					return new GeneralTreeFilterPanel(new METree());
				if(colId.equals("Event_type"))
					return new GeneralTreeFilterPanel(new AlarmTypeTree());
				if(colId.equals("Status"))
					return new GeneralTreeFilterPanel(new AlarmStatusTree());
			}
		return null;
	}

	public boolean expression(FilterExpressionInterface expr, ObjectResource or)
	{
		boolean result = false;
		Alarm a = (Alarm )or;
		List vec = expr.getVec();
		String type = (String) vec.get(0);
		if (type.equals("numeric"))
		{
			if (expr.getId().equals("time"))
			{
				if (((String)vec.get(1)).equals("="))
				{
					if (a.generated == Long.parseLong((String)vec.get(2)))
						result = true;
				}
				else if (((String)vec.get(1)).equals(">"))
				{
					if (a.generated > Long.parseLong((String)vec.get(2)))
						result = true;
				}
				else if (((String)vec.get(1)).equals("<"))
				{
					if (a.generated < Long.parseLong((String)vec.get(2)))
						result = true;
				}
			}
		}
		else if (type.equals("time"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.generated > Long.parseLong((String)vec.get(1)) &&  a.generated < Long.parseLong((String)vec.get(2)))
				{
					result = true;
				}
			}
		}
		else if (type.equals("range"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.generated > Long.parseLong((String)vec.get(1)) &&  a.generated < Long.parseLong((String)vec.get(2)))
					result = true;
			}
		}
		else if (type.equals("string"))
		{
			String substring = (String)vec.get(1);
			if (expr.getId().equals("Event_source"))
			{
				String name = Pool.getName(EventSource.typ, a.getSourceId());
				result = SearchSubstring(name, substring);
			}
			else if (expr.getId().equals("Monitored_element"))
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
			TreeModelClone tree = (TreeModelClone)vec.get(1);
			if (expr.getId().equals("Monitored_element"))
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
			if (expr.getId().equals("Event_source"))
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
			if (expr.getId().equals("Event_type"))
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
			if (expr.getId().equals("Status"))
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
			FilterExpressionInterface fe = (FilterExpressionInterface) getCriteria().get(i);
			af.addCriterium((FilterExpressionInterface)(fe.clone()));
		}
		af.logicScheme = (LogicScheme )this.logicScheme.clone(af);
		return af;
	}
}
