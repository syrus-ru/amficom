package com.syrus.AMFICOM.server;

import java.util.*;

import com.syrus.AMFICOM.filter.*;
import com.syrus.AMFICOM.server.event.*;
import com.syrus.AMFICOM.server.measurement.*;
import com.syrus.AMFICOM.server.catalog.*;

import com.syrus.AMFICOM.CORBA.General.*;

public class AlarmFilter implements Filter
{
	public AlarmFilter()
	{
	}

   	public boolean expression(FilterExpressionInterface expr, Object or)
    {
     	return expression((FilterExpressionBase )expr, or);
    }

	public boolean expression(FilterExpressionBase expr, Object or)
    {
    try
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
					if (a.getGenerated().getTime() == Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
				else if (((String)vec.elementAt(1)).equals(">"))
				{
					if (a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
				else if (((String)vec.elementAt(1)).equals("<"))
				{
					if (a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
			}
		}
		else if (type.equals("time"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(1)) &&  a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2)))
				{
					result = true;
				}
			}
		}
		else if (type.equals("range"))
		{
			if (expr.getId().equals("time"))
			{
				if ( a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(1)) &&  a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2)))
					result = true;
			}
		}
		else if (type.equals("string"))
		{
			String substring = (String)vec.elementAt(1);
			if (expr.getId().equals("source"))
			{
                Event ev = new Event(a.getEventId());
            	EventSource es = new EventSource(ev.getSourceId());
				result = SearchSubstring(es.getTransferable().object_source_name, substring);
			}
			else if (expr.getId().equals("monitoredelement"))
			{
                Event ev = new Event(a.getEventId());
                Result res = new Result(ev.getDescriptor());
                Evaluation eval = (Evaluation )res.getAction();
                Test test = Test.retrieveTestForEvaluation(eval.getId());
                MonitoredElement me = new MonitoredElement(test.getTransferable().monitored_element_id);

				result = SearchSubstring(me.getName(), substring);
			}
		}
		else if (type.equals("list"))
		{
			Hashtable tree = (Hashtable )vec.elementAt(1);
			if (expr.getId().equals("monitoredelement"))
			{
                Event ev = new Event(a.getEventId());
                Result res = new Result(ev.getDescriptor());
                Evaluation eval = (Evaluation )res.getAction();
                Test test = Test.retrieveTestForEvaluation(eval.getId());
                MonitoredElement me = new MonitoredElement(test.getTransferable().monitored_element_id);

				FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
				if (mmtn.state == 2)
				{
					result = true;
				}
				else
                if (mmtn.state == 1)
				{
					for(int i = 0; i < mmtn.children_ids.length; i++)
					{
						FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);

						for(int j = 0; j < down_mte.children_ids.length; j++)
						{
							FilterTreeNodeHolder down_mte2 = (FilterTreeNodeHolder )tree.get(down_mte.children_ids[j]);

							if (me.getId().equals(down_mte2.id) && (down_mte2.state == 2))
								result = true;
						}
					}
				}
			}
			if (expr.getId().equals("source"))
			{
                Event ev = new Event(a.getEventId());
				FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(int i = 0; i < mmtn.children_ids.length; i++)
					{
						FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
						if (ev.getSourceId().equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("type"))
			{
				FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(int i = 0; i < mmtn.children_ids.length; i++)
					{
						FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
						if (a.getTypeId().equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("status"))
			{
				FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(int i = 0; i < mmtn.children_ids.length; i++)
					{
						FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
						String stat = "";
						if (a.getStatus() == AlarmStatus._ALARM_STATUS_GENERATED)
							stat = "GENERATED";
						else if (a.getStatus() == AlarmStatus._ALARM_STATUS_ASSIGNED)
							stat = "ASSIGNED";
						else if (a.getStatus() == AlarmStatus._ALARM_STATUS_FIXED)
							stat = "FIXED";
						if (down_mte.id.equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
		}
		return result;
    }
    catch(Exception ex)
    {
    	return false;
    }
    }

   	public boolean SearchSubstring (String text, String substring)
	{
		boolean res = false;
		if(text.indexOf(substring) >= 0)
			return true;
        return res;
    }
}

