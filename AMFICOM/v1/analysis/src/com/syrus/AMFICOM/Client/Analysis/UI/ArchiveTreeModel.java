package com.syrus.AMFICOM.Client.Analysis.UI;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/*
|- Архив
	|- Измерения
		|-"Маршрут1"
			|- По шаблонам
				|+ Ша1
				|+ Ша2
				|- Ша3
					|-03.07.2004 (test)
						|-03.07.2004 17:40
						|-03.07.2004 17:45
			|-По дате
				|- июль 2004
					|- 03.07.2004 (test)
						|-03.07.2004 17:40
						|-03.07.2004 17:45
				|+ август 2004
				|+ сентябрь 2004
			|-По сигналам тревоги
				|- июль 2004
					|- 03.07.2004 (test)
						|-03.07.2004 17:40
				|+ август 2004
	|-Модели
		|-Расчетные
			|-"Путь 1"
				|-Модель 1
		|-Прогнозируемые
			|-"Маршрут1"
				|-ожидание на 01.09.2004
*/
public class ArchiveTreeModel extends ObjectResourceTreeModel
{
	Domain domain;
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMM.yyyy");
	static Calendar calendar;
	static Date initialDate;
	static Date currentDate;

	static {
		calendar = Calendar.getInstance();
		calendar.set(2004, 07, 01, 00, 00, 00);
		initialDate = calendar.getTime();
		currentDate = new Date();
	}

	public ArchiveTreeModel(Domain domain)
	{
		this.domain = domain;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", "Архив", true);
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
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if (node.getObject()instanceof String) {
			String s = (String)node.getObject();
			if (s.equals("root"))
				return String.class;
			else if (s.equals("measurements"))
				return MonitoredElement.class;
			else if (s.equals("measurementsetups"))
				return MeasurementSetup.class;
			else if (s.equals("dates"))
				return Date.class;
			else if (s.equals("alarms"))
				return Date.class;
		}
		else if (node.getObject()instanceof MonitoredElement) {
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();
			if (parent.getObject().equals("predicted"))
				return Result.class;
			if (parent.getObject().equals("measurements"))
				return String.class;
		}
		else if (node.getObject()instanceof MeasurementSetup)
			return Test.class;
		else if (node.getObject()instanceof Test)
			return Result.class;
		else if (node.getObject()instanceof Date) {
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();
			if (parent.getObject().equals("dates"))
				return Test.class;
			if (parent.getObject().equals("alarms"))
				return Result.class;
		}
		else if (node.getObject()instanceof SchemePath)
			return Result.class;
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
				vec.add(new ObjectResourceTreeNode("measurements", "Измерения", true));
				vec.add(new ObjectResourceTreeNode("models", "Модели", true));
			}
			else if(s.equals("measurements") || s.equals("predicted"))
			{
				StorableObjectCondition condition = new DomainCondition(domain, ObjectEntities.ME_ENTITY_CODE);
				try {
					List mes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mes.iterator(); it.hasNext(); ) {
						MonitoredElement me = (MonitoredElement)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(
								me,
								me.getName(),
								true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif").
															getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("models"))
			{
				vec.add(new ObjectResourceTreeNode("calculated", "Расчетные", true));
				vec.add(new ObjectResourceTreeNode("predicted", "Прогнозируемые", true));
			}
			else if(s.equals("calculated"))
			{
				List paths = Pool.getList(SchemePath.typ);
				if (paths != null)
				{
					ObjectResourceFilter filter = new ObjectResourceDomainFilter(domain.getId().getIdentifierString());
					paths = filter.filter(paths);
					ObjectResourceSorter sorter = SchemePath.getSorter();
					sorter.setDataSet(paths);
					paths = sorter.default_sort();
					for(Iterator it = paths.iterator(); it.hasNext();)
					{
						SchemePath path = (SchemePath)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(
								path,
								path.getName(),
								true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
						vec.add(n);
					}
				}
			}
			else if(s.equals("measurementsetups"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();
				MonitoredElement me = (MonitoredElement)parent.getObject();
				LinkedIdsCondition condition = LinkedIdsCondition.getInstance();
				condition.setDomain(domain);
				condition.setIdentifier(me.getId());
				condition.setEntityCode(ObjectEntities.MS_ENTITY_CODE);
				try {
					List mSetups = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mSetups.iterator(); it.hasNext(); ) {
						MeasurementSetup ms = (MeasurementSetup)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(
								ms,
								ms.getDescription(),
								true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testsetup.gif").
															getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("dates"))
			{
				calendar.setTime(initialDate);
				while (calendar.getTime().before(currentDate))
				{
					vec.add(new ObjectResourceTreeNode(calendar.getTime(), sdf.format(calendar.getTime()), true));
					calendar.add(Calendar.MONTH, 1);
				}
			}
			else if(s.equals("alarms"))
			{
				calendar.setTime(initialDate);
				while (calendar.getTime().before(currentDate))
				{
					vec.add(new ObjectResourceTreeNode(calendar.getTime(), sdf.format(calendar.getTime()), true));
					calendar.add(Calendar.MONTH, 1);
				}
			}
		}
		else if(node.getObject() instanceof MonitoredElement)
		{
			MonitoredElement me = (MonitoredElement)node.getObject();
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();
			String str = (String)parent.getObject();
			if (str.equals("measurements")) {
				vec.add(new ObjectResourceTreeNode("measurementsetups", "По шаблонам", true));
				vec.add(new ObjectResourceTreeNode("dates", "По дате", true));
				vec.add(new ObjectResourceTreeNode("alarms", "По сигналам тревоги", true));
			}
			else if (str.equals("predicted")) {
				/*ResultSortCondition condition = ResultSortCondition.getInstance();

//				LinkedIdsCondition condition = LinkedIdsCondition.getInstance();
condition.setDomain(domain);
				condition.setIdentifier(me.getId());
				condition.setEntityCode(ObjectEntities.MODELING_ENTITY_CODE);
				List models = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				for (Iterator it = models.iterator(); it.hasNext();)
				{
					Modeling model = (Modeling)it.next();
					if (model.getSort().equals(ModelingSort.MODELINGSORT_PREDICTION))
					{

					}
				}*/
//				images/model_mini.gif
			}
		}
		if (node.getObject() instanceof SchemePath) {
			/*StorableObjectCondition condition = new DomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE);
						 List models = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
							for (Iterator it = models.iterator(); it.next();)
							{
				 Modeling model = (Modeling)it.next();
				 if (model.getSort.equals("modeled"))
							}*/

//			"images/prognosis_mini.gif"
		}
		if (node.getObject() instanceof MeasurementSetup)
		{
			MeasurementSetup setup = (MeasurementSetup)node.getObject();
			LinkedIdsCondition condition = LinkedIdsCondition.getInstance();
			condition.setDomain(domain);
			condition.setIdentifier(setup.getId());
			condition.setEntityCode(ObjectEntities.TEST_ENTITY_CODE);
			try {
				List tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				for (Iterator it = tests.iterator(); it.hasNext(); ) {
					Test test = (Test)it.next();
					vec.add(new ObjectResourceTreeNode(test, test.getDescription(), true));
				}
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		if (node.getObject()instanceof Test) {
			Test test = (Test)node.getObject();
			LinkedIdsCondition condition = LinkedIdsCondition.getInstance();
			condition.setDomain(domain);
			condition.setIdentifier(test.getId());
			condition.setEntityCode(ObjectEntities.MEASUREMENT_ENTITY_CODE);
			try
			{
				List measurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				condition.setEntityCode(ObjectEntities.RESULT_ENTITY_CODE);
				List measurementIds = new LinkedList();
				for (Iterator it = measurements.iterator(); it.hasNext(); ) {
					Measurement measurement = (Measurement)it.next();
					measurementIds.add(measurement.getId());
				}
				condition.setLinkedIds(measurementIds);
				for (Iterator iter = MeasurementStorableObjectPool.getStorableObjectsByCondition(
						condition, true).iterator(); iter.hasNext(); ) {
					Result r = (Result)iter.next();
					if (r.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
						ImageIcon icon;
						if (r.getAlarmLevel().equals(AlarmLevel.ALARM_LEVEL_HARD)) {
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
									"images/alarm_bell_red.gif").
																	 getScaledInstance(15, 15, Image.SCALE_SMOOTH));
						}
						else if (r.getAlarmLevel().equals(AlarmLevel.ALARM_LEVEL_SOFT)) {
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
									"images/alarm_bell_yellow.gif").
																	 getScaledInstance(15, 15, Image.SCALE_SMOOTH));
						}
						else {
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").
																	 getScaledInstance(15, 15, Image.SCALE_SMOOTH));

						}
						vec.add(new ObjectResourceTreeNode(r, r.getMeasurement().getName(), true, icon, true));
					}
				}
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		if(node.getObject()instanceof Date)
		{
			Date startDate = (Date)node.getObject();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			Date endDate = calendar.getTime();

			ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();
			if (parent.getObject().equals("dates"))
			{
				StorableObjectCondition condition = new TemporalCondition(domain, startDate, endDate);
				try {
					List tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = tests.iterator(); it.hasNext(); ) {
						Test test = (Test)it.next();
						vec.add(new ObjectResourceTreeNode(test, test.getDescription(), true));
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (parent.getObject().equals("alarms"))
			{

			}
		}
		return vec;
	}
}
