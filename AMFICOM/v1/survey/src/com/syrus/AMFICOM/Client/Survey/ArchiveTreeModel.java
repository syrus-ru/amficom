package com.syrus.AMFICOM.Client.Survey;

import java.text.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

/*
|- Архив измерений
	|+ Линия1
	|+ Линия2
	|- Линия3
		|- Шаблон исследования
			|+ Ша1
			|+ Ша2
			|- Ша3
				|- Результаты измерений
					|- 1 янв 2003 - 31 янв 2003
						|+ 1 янв 2003
						|+ 1 янв 2003
						|- 1 янв 2003
							|_ Результат1
							|_ Результат2
							|_ Результат3
					|+ 1 фев 2003 - 28 фев 2003
					|+ 1 мар 2003 - 31 мар 2003
					|+ 1 апр 2003 - 30 апр 2003
				|- Тест
					|_ Тест1
					|_ Тест2
					|_ Тест3
				|- Сигналы тревоги
					|- 1 янв 2003 - 31 янв 2003
						|_ Тревога1
						|_ Тревога2
						|_ Тревога3
					|+ 1 фев 2003 - 28 фев 2003
					|+ 1 мар 2003 - 31 мар 2003
					|+ 1 апр 2003 - 30 апр 2003
		|- Анализ
			|_ Ana1
			|_ Ana2
		|+ Модель
*/
public class ArchiveTreeModel extends ObjectResourceTreeModel
{
	static public long day_duration = 24L * 60L * 60L * 1000L;
	
	DataSourceInterface dsi;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	public ArchiveTreeModel(DataSourceInterface dsi)
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
			if(s.equals("testsetup"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();
				String ids[] = new SurveyDataSourceImage(dsi).getTestSetupByME(me.getId());

				node.setParameter(ids);
			}
			else
			if(s.equals("alarm"))
			{
				new SurveyDataSourceImage(dsi).LoadResultSets();

				ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )node.getParent();// get TestSetup
				TestSetup tsetup = (TestSetup )tsetupnode.getObject();

				ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
				ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
				MonitoredElement me = (MonitoredElement )menode.getObject();

				String ids[] = new SurveyDataSourceImage(dsi).GetAlarmsForME(me.getId());

				node.setParameter(ids);
			}
			else
			if(s.equals("useranalysis"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();
				new SurveyDataSourceImage(dsi).GetAnalysisForME(me.getId());
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
							}
						}
					}
				}
			}
			else
			if(s.equals("test"))
			{
				ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )node.getParent();// get TestSetup
				TestSetup tsetup = (TestSetup )tsetupnode.getObject();

				ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
				ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
				MonitoredElement me = (MonitoredElement )menode.getObject();

				new SurveyDataSourceImage(dsi).GetTestsForME(me.getId());
			}
		}
		else
		{
			if(node.getObject() instanceof ResultSet)
			{
				ResultSet rs = (ResultSet )node.getObject();

				ObjectResourceTreeNode parent1 = (ObjectResourceTreeNode )node.getParent();// get "Результат"
				if(parent1.getObject().equals("result"))
				{
					ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )parent1.getParent();// get TestSetup
					TestSetup tsetup = (TestSetup )tsetupnode.getObject();

					ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
					ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
					MonitoredElement me = (MonitoredElement )menode.getObject();

					String[] r_ids = new SurveyDataSourceImage(dsi).LoadResultSetResultIds(rs.getId(), me.getId());
					node.setParameter(r_ids);
				}
			}
			else
			if(node.getObject() instanceof Test)
			{
				Test ts = (Test )node.getObject();

				new SurveyDataSourceImage(dsi).GetTestResult(ts.getId());
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
				return ResultSet.class;
			else
			if(s.equals("test"))
				return Test.class;
			else
			if(s.equals("testsetup"))
				return TestSetup.class;
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
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();//get TestSetup
				if(parent.getObject().equals("alarm"))
					return Alarm.class;
			}
			if(node.getObject() instanceof Test)
			  return Result.class;
			if(node.getObject() instanceof Date)
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
			if(s.equals("result") || s.equals("alarm"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();//get TestSetup
				TestSetup ts = (TestSetup )parent.getObject();

				ObjectResourceTreeNode parent1 = (ObjectResourceTreeNode )parent.getParent();//get "Шаблон"
				ObjectResourceTreeNode parent2 = (ObjectResourceTreeNode )parent1.getParent();//get MonitoredElement
				MonitoredElement me = (MonitoredElement )parent2.getObject();

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
			if(s.equals("testsetup"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				String[] ids = (String[] )node.getParameter();

				DataSet dSet = new DataSet();

				for(int i = 0; i <ids.length; i++)
				{
					TestSetup ts = (TestSetup )Pool.get(TestSetup.typ, ids[i]);
					if(ts != null)
						dSet.add(ts);
				}

				ObjectResourceSorter sorter = TestSetup.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					TestSetup ts = (TestSetup )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							ts, 
							ts.getName(), 
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testsetup.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
					vec.add(n);
				}
			}
			else
			if(s.equals("test"))
			{
				ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )node.getParent();
				TestSetup tsetup = (TestSetup )tsetupnode.getObject();

				ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
				ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
				MonitoredElement me = (MonitoredElement )menode.getObject();

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
						if(		test.monitored_element_id.equals(me.getId()) && 
								test.test_setup_id.equals(tsetup.getId()))
						{
							ObjectResourceTreeNode n = new ObjectResourceTreeNode(
									test, 
									test.getName(), 
									true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testir1.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
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
				vec.add(new ObjectResourceTreeNode("testsetup", "Шаблон", true));
				vec.add(new ObjectResourceTreeNode("useranalysis", "Анализ", true));
				vec.add(new ObjectResourceTreeNode("modeling", "Модель", true));
			}
			else
			if(node.getObject() instanceof TestSetup)
			{
				vec.add(new ObjectResourceTreeNode("result", "Результат", true));
				vec.add(new ObjectResourceTreeNode("test", "Тест", true));
				vec.add(new ObjectResourceTreeNode("alarm", "Сигнал тревоги", true));
			}
			else
			if(node.getObject() instanceof ResultSet)
			{
				ResultSet rs = (ResultSet )node.getObject();

				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();// get ResultSet

				if(parent.getObject().equals("result"))
				{
					Date ts = new Date(rs.start_time);
					ts.setHours(0);
					ts.setMinutes(0);
					ts.setSeconds(0);

/*
					GregorianCalendar calendar = new GregorianCalendar(
						ts.getYear(),
						ts.getMonth(),
						ts.getDate());
					int start_date = ts.getDate();
					int end_date = calendar.getActualMaximum(Calendar.DATE);
					for(int i = start_date; i <= end_date; i++)
					{
						Date rset_ts = new Date(
								ts.getYear(),
								ts.getMonth(),
								i,
								0,
								0,
								0);
*/
					GregorianCalendar calendar = new GregorianCalendar(
						ts.getYear(),
						ts.getMonth(),
						ts.getDate());
					int start_date = ts.getDate();
					int end_date = calendar.getActualMaximum(Calendar.DATE);
					for(long d = ts.getTime(); d <= rs.end_time; d+= day_duration)
//					for(long d = rs.start_time; d <= rs.end_time; d+= day_duration)
					{
						Date rset_ts = new Date(d);
//						rset_ts.setHours(0);

						ObjectResourceTreeNode n = new ObjectResourceTreeNode(
								rset_ts, 
								sdf.format(rset_ts), 
								true);
						vec.add(n);
					}
				}
				else
				if(parent.getObject().equals("alarm"))
				{
					ObjectResourceTreeNode parent1 = (ObjectResourceTreeNode )node.getParent();// get "Сигнал тревоги"
					ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )parent1.getParent();// get TestSetup
					TestSetup tsetup = (TestSetup )tsetupnode.getObject();

					ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
					ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
					MonitoredElement me = (MonitoredElement )menode.getObject();

					String[] r_ids = (String[] )parent1.getParameter();

					DataSet dSet = new DataSet();

					for(int i = 0; i < r_ids.length; i++)
					{
						Alarm alarm = (Alarm )Pool.get(Alarm.typ, r_ids[i]);
						if(alarm != null)
							if(		alarm.generated >= rs.start_time &&
									alarm.generated < rs.end_time)
							{
								Evaluation eval = alarm.getEvaluation();
								Test test = findTestForEvaluation(eval);
								if(test != null)
									if(test.test_setup_id.equals(tsetup.getId()))
										dSet.add(alarm);
							}
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
			}
			else
			if(node.getObject() instanceof Date)
			{
				Date ts = (Date )node.getObject();
				long start_time = ts.getTime();
				long end_time = start_time + day_duration;

				ObjectResourceTreeNode rsetnode = (ObjectResourceTreeNode )node.getParent();// get ResultSet
				ResultSet rs = (ResultSet )rsetnode.getObject();

				ObjectResourceTreeNode parent1 = (ObjectResourceTreeNode )rsetnode.getParent();// get "Результат"
				ObjectResourceTreeNode tsetupnode = (ObjectResourceTreeNode )parent1.getParent();// get TestSetup
				TestSetup tsetup = (TestSetup )tsetupnode.getObject();

				ObjectResourceTreeNode parent3 = (ObjectResourceTreeNode )tsetupnode.getParent();// get "Шаблон"
				ObjectResourceTreeNode menode = (ObjectResourceTreeNode )parent3.getParent();// get MonitoredElement
				MonitoredElement me = (MonitoredElement )menode.getObject();

				String[] r_ids = (String[] )rsetnode.getParameter();

				DataSet dSet = new DataSet();

				for(int i = 0; i < r_ids.length; i++)
				{
					Result res = (Result )Pool.get(Result.typ, r_ids[i]);
					if(res != null)
						if(		res.elementary_start_time >= start_time &&
								res.elementary_start_time < end_time)
						{
							Test test = (Test )Pool.get(Test.typ, res.action_id);
							if(test != null)
								if(test.test_setup_id.equals(tsetup.getId()))
									dSet.add(res);
						}
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Result res = (Result )enum.nextElement();
					Alarm a = Alarm.getAlarmByTestResult(res.getId());
					ImageIcon ii;
					if(a == null)
						ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					else
						ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/alarm.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							res, 
							res.getName(), 
							true,
							ii,
							true);
					vec.add(n);
				}
			}
			else
			if(node.getObject() instanceof Test)
			{
				Test ts = (Test )node.getObject();

				DataSet dSet = new DataSet();

				for(int i = 0; i < ts.result_ids.length; i++)
				{
					Result res = (Result )Pool.get(Result.typ, ts.result_ids[i]);
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
					Alarm a = Alarm.getAlarmByTestResult(res.getId());
					ImageIcon ii;
					if(a == null)
						ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					else
						ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/alarm.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(
							res, 
							res.getName(), 
							true,
							ii,
							true);
					vec.add(n);
				}
			}
		}
		return vec;
	}

	Test findTestForEvaluation(Evaluation eval)
	{
		Test t;
		Hashtable ht = Pool.getHash(Test.typ);
		if(ht == null)
			return null;
		for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
		{
			t = (Test )enum.nextElement();
			if(t.evaluation_id.equals(eval.getId()))
				return t;
		}
		return null;
	}
}
