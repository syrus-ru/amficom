package com.syrus.AMFICOM.Client.Survey;

import java.text.*;
import java.awt.*;
import java.util.*;

import java.util.List;
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
					List dSet = Pool.getList(MonitoredElement.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(this.dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = StubResource.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						MonitoredElement me = (MonitoredElement )it.next();
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
				ObjectResourceTreeNode parent1 = (ObjectResourceTreeNode )parent.getParent();//get "Шаблон"
				ObjectResourceTreeNode parent2 = (ObjectResourceTreeNode )parent1.getParent();//get MonitoredElement
				MonitoredElement me = (MonitoredElement )parent2.getObject();

				if (Pool.getHash(ResultSet.TYPE) != null)
				{
					List dSet = Pool.getList(ResultSet.TYPE);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(this.dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = ResultSet.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						ResultSet rs = (ResultSet )it.next();
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

				List dSet = new ArrayList();

				for(int i = 0; i <ids.length; i++)
				{
					TestSetup ts = (TestSetup )Pool.get(TestSetup.TYPE, ids[i]);
					if(ts != null)
						dSet.add(ts);
				}

				ObjectResourceSorter sorter = StubResource.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					TestSetup ts = (TestSetup )it.next();
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

				if (Pool.getHash(Test.TYPE) != null)
				{
					List dSet = Pool.getList(Test.TYPE);
//					Enumeration enum = Pool.getHash(Test.typ).elements();

					ObjectResourceSorter sorter = Test.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						Test test = (Test )it.next();
						if(		test.getMonitoredElementId().equals(me.getId()) && 
								test.getTestSetupId().equals(tsetup.getId()))
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

				if (Pool.getHash(Analysis.TYPE) != null)
				{
					List dSet = Pool.getList(Analysis.TYPE);
					ObjectResourceSorter sorter = StubResource.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						Analysis anal = (Analysis )it.next();
						if(anal.getMonitoredElementId().equals(me.getId()) && anal.getUserId().length()>0)
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

				/**
				 * FIXME remove this fucking vector, which will be indicate existance at pool SchemePath
				 */
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

				if (Pool.getHash(Modeling.TYPE) != null)
				{
					List dSet = Pool.getList(Modeling.TYPE);
//					Enumeration enum = Pool.getHash(Modeling.typ).elements();

					ObjectResourceSorter sorter = StubResource.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						Modeling mod = (Modeling )it.next();
						if(schvec.contains(mod.getSchemePathId()))
						{
							ImageIcon ii = null;
							if(mod.getTypeId().equals("dadara"))
								ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/model_mini.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH));
							else
							if(mod.getTypeId().equals("optprognosis"))
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
					Date ts = new Date(rs.getStartTime());					
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
					for(long d = ts.getTime(); d <= rs.getEndTime(); d+= day_duration)
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

					List dSet = new ArrayList();

					for(int i = 0; i < r_ids.length; i++)
					{
						Alarm alarm = (Alarm )Pool.get(Alarm.typ, r_ids[i]);
						if(alarm != null)
							if(		alarm.generated >= rs.getStartTime() &&
									alarm.generated < rs.getEndTime())
							{
								Evaluation eval = alarm.getEvaluation();
								Test test = findTestForEvaluation(eval);
								if(test != null)
									if(test.getTestSetupId().equals(tsetup.getId()))
										dSet.add(alarm);
							}
					}

					ObjectResourceSorter sorter = Alarm.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						Alarm alarm = (Alarm )it.next();
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

				String[] r_ids = (String[] )rsetnode.getParameter();

				List dSet = new ArrayList();

				for(int i = 0; i < r_ids.length; i++)
				{
					Result res = (Result )Pool.get(Result.TYPE, r_ids[i]);
					if(res != null)
						if(		res.getElementaryStartTime() >= start_time &&
								res.getElementaryStartTime() < end_time)
						{
							Test test = (Test )Pool.get(Test.TYPE, res.getActionId());
							if(test != null)
								if(test.getTestSetupId().equals(tsetup.getId()))
									dSet.add(res);
						}
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					Result res = (Result )it.next();
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

				List dSet = new ArrayList();

				for(int i = 0; i < ts.getResultIds().length; i++)
				{
					Result res = (Result )Pool.get(Result.TYPE, ts.getResultIds()[i]);
					if(res != null)
						dSet.add(res);
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					Result res = (Result )it.next();
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
		Hashtable ht = Pool.getHash(Test.TYPE);
		if(ht == null)
			return null;
		for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
		{
			t = (Test )enum.nextElement();
			if(t.getEvaluationId().equals(eval.getId()))
				return t;
		}
		return null;
	}
}
