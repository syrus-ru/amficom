package com.syrus.AMFICOM.Client.Test;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

/*
Архив измерений
|+ Линия1
|+ Линия2
|- Линия3
   |- Результаты измерений
	  |- 1 янв 2003 - 31 янв 2003
		 |_ Результат1
		 |_ Результат2
		 |_ Результат3
	  |+ 1 фев 2003 - 31 фев 2003
	  |+ 1 фев 2003 - 31 фев 2003
	  |+ 1 фев 2003 - 31 фев 2003
	|- Анализ
	  |_ Ana1
	  |_ Ana2
	|+ Модель
	|+ Тест
*/
public class ObserverTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public ObserverTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", "Архив измерений", true);
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
		if(node.expanded)
			return;
		node.expanded = true;
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				new ConfigDataSourceImage(dsi).LoadISM();
//				dsi.LoadISM();
			}
			else
			if(s.equals("result"))
			{
				new SurveyDataSourceImage(dsi).LoadResultSets();
			}
			else
			if(s.equals("alarm"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();
				String ids[] = new SurveyDataSourceImage(dsi).GetAlarmsForME(me.getId());
/*
				String ids[] = dsi.GetAlarmsForME(me.getId());
//				new SurveyDataSourceImage(dsi).GetAlarms(ids);
				dsi.GetAlarms(ids);
*/
				node.setParameter(ids);
			}
			else
			if(s.equals("useranalysis"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();
				new SurveyDataSourceImage(dsi).GetAnalysisForME(me.getId());
/*
				String ids[] = dsi.GetAnalysisForME(me.getId());
				for(int i = 0; i < ids.length; i++)
					new SurveyDataSourceImage(dsi).GetAnalysis(ids[i]);
*/
			}
			else
			if(s.equals("modeling"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if(me.element_type.equals("path"))
				{
					Vector schvec = new Vector();
					if(Pool.getHash(SchemePath.typ) != null)
					{
						Enumeration enum1 = Pool.getHash(SchemePath.typ).elements();
						for(; enum1.hasMoreElements();)
						{
							SchemePath sp = (SchemePath )enum1.nextElement();
							if(sp.path_id.equals(me.element_id))
							{
								new SurveyDataSourceImage(dsi).GetModelingsForSP(sp.getId());
/*
								String ids[] = dsi.GetModelingsForSP(sp.getId());
								for(int i = 0; i < ids.length; i++)
									new SurveyDataSourceImage(dsi).GetModeling(ids[i]);
*/
							}
						}
					}
				}
			}
			else
			if(s.equals("test"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				new SurveyDataSourceImage(dsi).GetTestsForME(me.getId());
/*
				String ids[] = dsi.GetTestsForME(me.getId());
				new SurveyDataSourceImage(dsi).GetTests(ids);
*/
			}
		}
		else
		{
			if(node.getObject() instanceof ResultSet)
			{
				ResultSet rs = (ResultSet )node.getObject();

				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				ObjectResourceTreeNode parent2 = (ObjectResourceTreeNode )parent.getParent();

				MonitoredElement me = (MonitoredElement )parent2.getObject();

				String[] r_ids = new SurveyDataSourceImage(dsi).LoadResultSetResultIds(rs.getId(), me.getId());
/*
				String[] r_ids = dsi.LoadResultSetResultIds(rs.getId(),me.getId());
				for(int i = 0; i < r_ids.length; i++)
					dsi.GetResult(r_ids[i]);
//					new SurveyDataSourceImage(dsi).GetResult(r_ids[i]);
*/
				node.setParameter(r_ids);
			}
			else
			if(node.getObject() instanceof Test)
			{
				Test ts = (Test )node.getObject();

				new SurveyDataSourceImage(dsi).GetTestResult(ts.getId());
/*
				for(int i = 0; i < ts.result_ids.length; i++)
					dsi.GetResult(ts.result_ids[i]);
//					new SurveyDataSourceImage(dsi).GetResult(ts.result_ids[i]);
*/
			}
		}
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		/*if(node.expanded)
			return;
		node.expanded = true;*/
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
				return MonitoredElement.class;
			else
			if(s.equals("result"))
				return ResultSet.class;
			else
			if(s.equals("alarm"))
				return Alarm.class;
			else
			if(s.equals("test"))
				return Test.class;
			else
			if(s.equals("useranalysis"))
				return Analysis.class;
			else
			if(s.equals("modeling"))
				return Modeling.class;
		}
		else
		{
			if(node.getObject() instanceof ResultSet)
			  return Result.class;
			if(node.getObject() instanceof Test)
			  return Result.class;
		}
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				if (Pool.getHash(MonitoredElement.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(MonitoredElement.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = MonitoredElement.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						MonitoredElement me = (MonitoredElement )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(
								me, 
								me.getName(), 
								true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
						vec.add(n);
					}
				}
			}
			else
			if(s.equals("result"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if (Pool.getHash(ResultSet.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(ResultSet.typ));
//					Enumeration enum = Pool.getHash(ResultSet.typ).elements();

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = ResultSet.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						ResultSet rs = (ResultSet )enum.nextElement();
						if(rs.getDomainId().equals(me.domain_id))
						{
							ObjectResourceTreeNode n = new ObjectResourceTreeNode(rs, rs.getName(), true);
							vec.add(n);
						}
					}
				}
			}
			else
			if(s.equals("alarm"))
			{
				String[] ids = (String[] )node.getParameter();

				DataSet dSet = new DataSet();

				for(int i = 0; i <ids.length; i++)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, ids[i]);
					if(alarm != null)
						dSet.add(alarm);
				}

				ObjectResourceSorter sorter = Alarm.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Alarm alarm = (Alarm )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							alarm, 
							alarm.getName(), 
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/alarm.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)),
							true);
					vec.add(n);
				}
			}
			else
			if(s.equals("test"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if (Pool.getHash(Test.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Test.typ));
//					Enumeration enum = Pool.getHash(Test.typ).elements();

					ObjectResourceSorter sorter = Test.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Test test = (Test )enum.nextElement();
						if(test.getMonitoredElementId().equals(me.getId()))
						{
							ObjectResourceTreeNode n = new ObjectResourceTreeNode(test, test.getName(), true);
							vec.add(n);
						}
					}
				}
			}
			else
			if(s.equals("useranalysis"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if (Pool.getHash(Analysis.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Analysis.typ));
//					Enumeration enum = Pool.getHash(Analysis.typ).elements();

					ObjectResourceSorter sorter = Analysis.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Analysis anal = (Analysis )enum.nextElement();
						if(anal.monitored_element_id.equals(me.getId()) && !anal.user_id.equals(""))
						{
							ObjectResourceTreeNode n = new ObjectResourceTreeNode(anal, anal.getName(), true, true);
							vec.add(n);
						}
					}
				}
			}
			else
			if(s.equals("modeling"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				Vector schvec = new Vector();
				if(me.element_type.equals("path"))
				{
					if(Pool.getHash(SchemePath.typ) != null)
					{
						Enumeration enum1 = Pool.getHash(SchemePath.typ).elements();
						for(; enum1.hasMoreElements();)
						{
							SchemePath sp = (SchemePath )enum1.nextElement();
							if(sp.path_id.equals(me.element_id))
								schvec.add(sp.getId());
						}
					}
				}

				if (Pool.getHash(Modeling.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Modeling.typ));
//					Enumeration enum = Pool.getHash(Modeling.typ).elements();

					ObjectResourceSorter sorter = Modeling.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Modeling mod = (Modeling )enum.nextElement();
						if(schvec.contains(mod.scheme_path_id))
						{
							ImageIcon ii = null;
							if(mod.type_id.equals("dadara"))
								ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/model_mini.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
							else
							if(mod.type_id.equals("optprognosis"))
								ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/prognosis_mini.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));

							ObjectResourceTreeNode n;
							if(ii == null)
								n = new ObjectResourceTreeNode(
									mod, 
									mod.getName(), 
									true,
									true);
							else
								n = new ObjectResourceTreeNode(
									mod, 
									mod.getName(), 
									true,
									ii,
									true);
							vec.add(n);
						}
					}
				}
			}
		}
		else
		{
			if(node.getObject() instanceof MonitoredElement)
			{
				vec.add(new ObjectResourceTreeNode("result", "Результаты", true));
				vec.add(new ObjectResourceTreeNode("test", "Тест", true));
				vec.add(new ObjectResourceTreeNode("alarm", "Сигнал тревоги", true));
				vec.add(new ObjectResourceTreeNode("useranalysis", "Анализ", true));
				vec.add(new ObjectResourceTreeNode("modeling", "Модель", true));
			}
			else
			if(node.getObject() instanceof ResultSet)
			{
				ResultSet rs = (ResultSet )node.getObject();

				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				ObjectResourceTreeNode parent2 = (ObjectResourceTreeNode )parent.getParent();

				MonitoredElement me = (MonitoredElement )parent2.getObject();

				String[] r_ids = (String[] )node.getParameter();

				DataSet dSet = new DataSet();

				for(int i = 0; i <r_ids.length; i++)
				{
					Result res = (Result )Pool.get(Result.typ, r_ids[i]);
					if(res != null)
						dSet.add(res);
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Result res = (Result )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							res, 
							res.getName(), 
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)),
							true);
					vec.add(n);
				}
			}
			else
			if(node.getObject() instanceof Test)
			{
				Test ts = (Test )node.getObject();

				DataSet dSet = new DataSet();

				for(int i = 0; i < ts.getResultIds().length; i++)
				{
					Result res = (Result )Pool.get(Result.typ, ts.getResultIds()[i]);
					if(res != null)
						dSet.add(res);
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Result res = (Result )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							res, 
							res.getName(), 
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)),
							true);
					vec.add(n);
				}
			}
		}
		return vec;
	}
}

