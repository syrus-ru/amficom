package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import java.util.*;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

public class TestFilter extends ObjectResourceFilter
{
	private Vector filterColumns;

	public Vector getFilterColumns()
	{
		if (this.filterColumns==null){
			this.filterColumns = new Vector();
			this.filterColumns.add("kis");
			this.filterColumns.add("mone");
			this.filterColumns.add("test_type");
			this.filterColumns.add("temp_type");
			this.filterColumns.add("status");
			this.filterColumns.add("time");
			this.filterColumns.add("alarm");
		}

		return this.filterColumns;
	}

	public String getFilterColumnName(String col_id)
	{
		if(col_id == null)
			return "";
		if(col_id.equals("kis"))
			return LangModelSchedule.getString("RTU");
		if(col_id.equals("mone"))
			return LangModelSchedule.getString("TestObject");
		if(col_id.equals("test_type"))
			return LangModelSchedule.getString("TestType");
		if(col_id.equals("temp_type"))
			return LangModelSchedule.getString("TemporalType");
		if(col_id.equals("status"))
			return LangModelSchedule.getString("Status");
		if(col_id.equals("time"))
			return LangModelSchedule.getString("Test_time");
		if(col_id.equals("alarm"))
			return LangModelSchedule.getString("Alarm");
		return "";
	}

	public String[] getColumnFilterTypes(String col_id)
	{
		if(col_id == null)
			return new String[] {};
		if(col_id.equals("kis"))
			return new String[] {"string", "list"};
		if(col_id.equals("mone"))
			return new String[] {"string", "list"};
		if(col_id.equals("test_type"))
			return new String[] {"list"};
		if(col_id.equals("temp_type"))
			return new String[] {"list"};
		if(col_id.equals("status"))
			return new String[] {"list"};
		if(col_id.equals("time"))
			return new String[] {"time"};
		if(col_id.equals("alarm"))
			return new String[] {"list"};
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
			if(type.equals("list") && col_id.equals("kis"))
				return new GeneralTreeFilterPanel(new KISTree());
			if(type.equals("list") && col_id.equals("mone"))
				return new GeneralTreeFilterPanel(new MoneTree());
			if(type.equals("list") && col_id.equals("test_type"))
				return new GeneralTreeFilterPanel(new TestTypeTree());
			if(type.equals("list") && col_id.equals("temp_type"))
				return new GeneralTreeFilterPanel(new TempTypeTree());
			if(type.equals("list") && col_id.equals("status"))
				return new GeneralTreeFilterPanel(new StatusTree());
			if(type.equals("list") && col_id.equals("alarm"))
				return new GeneralTreeFilterPanel(new AlarmTree());
		return null;
	}

	public boolean expression(FilterExpressionInterface expr, ObjectResource or)
	{
		boolean result = false;
		Test test = (Test) or;
		Vector vec = expr.getVec();
		String type = (String) vec.elementAt(0);
		if (type.equals("numeric"))
		{
			if (expr.getId().equals("time"))
			{
				if (((String)vec.elementAt(1)).equals("="))
				{
					if (test.getStartTime() == Long.parseLong((String)vec.elementAt(2)))
					{
						result = true;
					}
				}
				else if (((String)vec.elementAt(1)).equals(">"))
				{
					if (test.getStartTime() > Long.parseLong((String)vec.elementAt(2)))
					{
						result = true;
					}
				}
				else if (((String)vec.elementAt(1)).equals("<"))
				{
					if (test.getStartTime() < Long.parseLong((String)vec.elementAt(2)))
					{
						result = true;
					}
				}
			}
		}
		else if (type.equals("time"))
		{
			if (expr.getId().equals("time"))
			{
				if ( test.getStartTime() > Long.parseLong((String)vec.elementAt(1)) &&  test.getStartTime() < Long.parseLong((String)vec.elementAt(2)))
				{
					result = true;
				}
			}
		}
		else if (type.equals("range"))
		{
			if (expr.getId().equals("time"))
			{
				if ( test.getStartTime() > Long.parseLong((String)vec.elementAt(1)) &&  test.getStartTime() < Long.parseLong((String)vec.elementAt(2)))
				{
					result = true;
				}
			}
		}
		else if (type.equals("string"))
		{
			String substring = (String)vec.elementAt(1);
			if (expr.getId().equals("kis"))
			{
				String name = Pool.getName(KIS.typ, test.getKisId());
				result = SearchSubstring(name, substring);
			}
			else if (expr.getId().equals("mone"))
			{
				MonitoredElement me;
				for(Enumeration e = Pool.getHash(MonitoredElement.typ).elements();
					e.hasMoreElements();)
				{
					me = (MonitoredElement )e.nextElement();
					if(me.getId().equals(test.getMonitoredElementId()))
					{
						result = SearchSubstring(me.getName(), substring);
					}
				}
			}
		}
		else if (type.equals("list"))
		{
			TreeModelClone tree = (TreeModelClone)vec.elementAt(1);
			if (expr.getId().equals("kis"))
			{
				FilterTreeNode mmtn = (FilterTreeNode )tree.getRoot();
				String portid = "";
				String meid = "";
				String testtypeid = "";
				MonitoredElement me;
				boolean flag1 = false;
				boolean flag2 = false;
				for(Enumeration e = Pool.getHash(MonitoredElement.typ).elements();
					e.hasMoreElements();)
				{
					me = (MonitoredElement )e.nextElement();
					if(me.getId().equals(test.getMonitoredElementId()))
					{
						portid = me.access_port_id;
						meid = me.getId();
						break;
					}
				}
				AccessPort ap = (AccessPort )Pool.get("accessport",portid);
				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
				{
					TestType tt = (TestType )enum.nextElement();
					if(apt.test_type_ids.contains(tt.getId()))
					{
						testtypeid = tt.getId();
					}
				}

				if (mmtn.state == 2)
				{
					result = true;
				}
				else if (mmtn.state == 1)
				{
					for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
					{
						FilterTreeNode down_mte1 = (FilterTreeNode )enum.nextElement();
						if (test.getKisId().equals(down_mte1.id) && (down_mte1.state != 0))
						{
							for(Enumeration enu = down_mte1.children(); enu.hasMoreElements();)
							{
								FilterTreeNode down_mte2 = (FilterTreeNode )enu.nextElement();
								if (portid.equals(down_mte2.id) && (down_mte2.state != 0))
								{
									for(Enumeration en = down_mte2.children(); en.hasMoreElements();)
									{
										FilterTreeNode down_mte3 = (FilterTreeNode )en.nextElement();
										if ( down_mte3.getId().equals("mone") )
										{
											for(Enumeration e = down_mte3.children(); e.hasMoreElements();)
											{
												FilterTreeNode down_mte4_1 = (FilterTreeNode )e.nextElement();
												if (meid.equals(down_mte4_1.id) && (down_mte4_1.state == 2))
												{
													flag1 = true;
													break;
												}
											}
										}
										if ( down_mte3.getId().equals("testtypes") )
										{
											for(Enumeration e = down_mte3.children(); e.hasMoreElements();)
											{
												FilterTreeNode down_mte4_2 = (FilterTreeNode )e.nextElement();
												if (testtypeid.equals(down_mte4_2.id) && (down_mte4_2.state == 2))
												{
													flag2 = true;
													break;
												}
											}
										}
										if (flag1 == true && flag2 == true)
										{
											result = true;
											break;
										}
									}
								}
								if (flag1 == true && flag2 == true)
								{
									break;
								}
							}
						}
						if (flag1 == true && flag2 == true)
						{
							break;
						}
					}
				}
			}
			if (expr.getId().equals("mone"))
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
						MonitoredElement me = null;
						for(Enumeration e = Pool.getHash(MonitoredElement.typ).elements();
							e.hasMoreElements();)
						{
							me = (MonitoredElement )e.nextElement();
							if(me.getId().equals(test.getMonitoredElementId()))
							{
								break;
							}
						}

						if (me.id.equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("test_type"))
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
						if (test.getTestTypeId().equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("temp_type"))
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
						String stat = test.getTemporalType().toString();
						if (down_mte.getId().equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("alarm"))
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
						if (test.getElementaryTestAlarms().length != 0)
							stat = "alarm";
						else stat = "noalarm";
						if (down_mte.getId().equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expr.getId().equals("status"))
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
						String stat = test.getStatus().toString();
						if (down_mte.getId().equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
		}
		return result;
	}

	public Object clone()
	{
		TestFilter af = new TestFilter();
		af.clearCriteria();
		for(int i = 0; i < getCriteria().size(); i++)
		{
			FilterExpression fe = (FilterExpression )getCriteria().get(i);
			af.addCriterium((FilterExpression )fe.clone());
		}
		return af;
	}
}
