package com.syrus.AMFICOM.Client.Test;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
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
				new ConfigDataSourceImage(this.dsi).LoadISM();
//				dsi.LoadISM();
			}
			else
			if(s.equals("result"))
			{
				new SurveyDataSourceImage(this.dsi).LoadResultSets();
			}
			else
			if(s.equals("alarm"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();
				String ids[] = new SurveyDataSourceImage(this.dsi).GetAlarmsForME(me.getId());
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
				new SurveyDataSourceImage(this.dsi).GetAnalysisForME(me.getId());
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
					if(Pool.getHash(SchemePath.typ) != null)
					{
            Map map = Pool.getMap(SchemePath.typ);
						for(Iterator it = map.values().iterator(); it.hasNext();)
						{
							SchemePath sp = (SchemePath )it.next();
							if(sp.path_id.equals(me.element_id))
							{
								new SurveyDataSourceImage(this.dsi).GetModelingsForSP(sp.getId());
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

				new SurveyDataSourceImage(this.dsi).GetTestsForME(me.getId());
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

				String[] r_ids = new SurveyDataSourceImage(this.dsi).LoadResultSetResultIds(rs.getId(), me.getId());
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

				new SurveyDataSourceImage(this.dsi).GetTestResult(ts.getId());
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

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
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
			if(s.equals("result"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if (Pool.getHash(ResultSet.TYPE) != null)
				{
					List dSet = Pool.getList(ResultSet.TYPE);
//					Enumeration enum = Pool.getHash(ResultSet.typ).elements();

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
			if(s.equals("alarm"))
			{
				String[] ids = (String[] )node.getParameter();

				List dSet = new ArrayList();

				for(int i = 0; i <ids.length; i++)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, ids[i]);
					if(alarm != null)
						dSet.add(alarm);
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
			else
			if(s.equals("test"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MonitoredElement me = (MonitoredElement )parent.getObject();

				if (Pool.getHash(Test.TYPE) != null)
				{
					List dSet = Pool.getList(Test.TYPE);
					
					ObjectResourceSorter sorter = Test.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					for(Iterator it = dSet.iterator(); it.hasNext();)
					{
						Test test = (Test )it.next();
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

				List schvec = new ArrayList();
				if(me.element_type.equals("path"))
				{
          Map map = Pool.getMap(SchemePath.typ);
					if(map != null)
					{
						for(Iterator it = map.values().iterator(); it.hasNext();)
						{
							SchemePath sp = (SchemePath )it.next();
							if(sp.path_id.equals(me.element_id))
								schvec.add(sp.getId());
						}
					}
				}

				if (Pool.getHash(Modeling.TYPE) != null)
				{
					List dSet = Pool.getList(Modeling.TYPE);

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

				List dSet = new ArrayList();

				for(int i = 0; i <r_ids.length; i++)
				{
					Result res = (Result )Pool.get(Result.TYPE, r_ids[i]);
					if(res != null)
						dSet.add(res);
				}

				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					Result res = (Result )it.next();
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

