package com.syrus.AMFICOM.Client.Analysis.UI;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

/*
|- �����
	|- ���������
		|-"�������1"
			|- �� ��������
				|+ ��1
				|+ ��2
				|- ��3
					|-03.07.2004 (test)
						|-03.07.2004 17:40
						|-03.07.2004 17:45
			|-�� ����
				|- ���� 2004
					|- 03.07.2004 (test)
						|-03.07.2004 17:40
						|-03.07.2004 17:45
				|+ ������ 2004
				|+ �������� 2004
			|-�� �������� �������
				|- ���� 2004
					|- 03.07.2004 (test)
						|-03.07.2004 17:40
				|+ ������ 2004
	|-������
		|-���������
			|-"���� 1"
				|-������ 1
		|-��������������
			|-"�������1"
				|-�������� �� 01.09.2004
*/
public class ArchiveTreeModel extends ObjectResourceTreeModel
{
	Domain domain;
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
		return new ObjectResourceTreeNode("root", "�����", true);
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

	public ObjectResourceController getNodeChildController(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String) object;
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
		} else if (object instanceof MonitoredElement) {
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.getParent();
			if (parent.getObject().equals("predicted"))
				return Result.class;
			if (parent.getObject().equals("measurements"))
				return String.class;
		} else if (object instanceof MeasurementSetup)
			return Test.class;
		else if (object instanceof Test)
			return Result.class;
		else if (object instanceof Date) {
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.getParent();
			if (parent.getObject().equals("dates"))
				return Test.class;
			if (parent.getObject().equals("alarms"))
				return Result.class;
		} else if (object instanceof SchemePath)
			return Result.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node) {
		Object nodeObject = node.getObject();
		List list = new ArrayList();
		if (nodeObject instanceof String) {
			String s = (String) nodeObject;
			if (s.equals("root")) {
				list.add(new ObjectResourceTreeNode("measurements", "���������", true));
				list.add(new ObjectResourceTreeNode("models", "������", true));
			} else if (s.equals("measurements") || s.equals("predicted")) {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.domain.getId(),
																		ObjectEntities.ME_ENTITY_CODE);
				try {
					Collection mes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mes.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(me, me.getName(), true,
																				new ImageIcon(Toolkit
																						.getDefaultToolkit().getImage(
																							"images/pathmode.gif")
																						.getScaledInstance(15, 15,
																							Image.SCALE_SMOOTH)));
						list.add(n);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals("models")) {
				list.add(new ObjectResourceTreeNode("calculated", "���������", true));
				list.add(new ObjectResourceTreeNode("predicted", "��������������", true));
			} else if (s.equals("calculated")) {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.domain.getId(),
																		ObjectEntities.SCHEME_PATH_ENTITY_CODE);
				try {
					List paths = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = paths.iterator(); it.hasNext();) {
						SchemePath path = (SchemePath) it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(path, path.getName(), true,
																				new ImageIcon(Toolkit
																						.getDefaultToolkit().getImage(
																							"images/pathmode.gif")
																						.getScaledInstance(15, 15,
																							Image.SCALE_SMOOTH)));
						list.add(n);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals("measurementsetups")) {
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.getParent();
				MonitoredElement me = (MonitoredElement) parent.getObject();
				LinkedIdsCondition condition = new LinkedIdsCondition(me.getId(), ObjectEntities.MS_ENTITY_CODE);
				try {
					Collection mSetups = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mSetups.iterator(); it.hasNext();) {
						MeasurementSetup ms = (MeasurementSetup) it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(ms, ms.getDescription(), true,
																				new ImageIcon(Toolkit
																						.getDefaultToolkit().getImage(
																							"images/testsetup.gif")
																						.getScaledInstance(15, 15,
																							Image.SCALE_SMOOTH)));
						list.add(n);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals("dates")) {
				calendar.setTime(initialDate);
				while (calendar.getTime().before(currentDate)) {
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					list.add(new ObjectResourceTreeNode(calendar.getTime(), sdf.format(calendar.getTime()), true));
					calendar.add(Calendar.MONTH, 1);
				}
			} else if (s.equals("alarms")) {
				calendar.setTime(initialDate);
				while (calendar.getTime().before(currentDate)) {
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					list.add(new ObjectResourceTreeNode(calendar.getTime(), sdf.format(calendar.getTime()), true));
					calendar.add(Calendar.MONTH, 1);
				}
			}
		} else if (nodeObject instanceof MonitoredElement) {
			MonitoredElement me = (MonitoredElement) nodeObject;
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.getParent();
			String str = (String) parent.getObject();
			if (str.equals("measurements")) {
				list.add(new ObjectResourceTreeNode("measurementsetups", "�� ��������", true));
				list.add(new ObjectResourceTreeNode("dates", "�� ����", true));
				list.add(new ObjectResourceTreeNode("alarms", "�� �������� �������", true));
			} else if (str.equals("predicted")) {
				/*
				 * ResultSortCondition condition =
				 * ResultSortCondition.getInstance();
				 *  // LinkedIdsCondition condition =
				 * LinkedIdsCondition.getInstance();
				 * condition.setDomain(domain);
				 * condition.setIdentifier(me.getId());
				 * condition.setEntityCode(ObjectEntities.MODELING_ENTITY_CODE);
				 * List models =
				 * MeasurementStorableObjectPool.getStorableObjectsByCondition(condition,
				 * true); for (Iterator it = models.iterator(); it.hasNext();) {
				 * Modeling model = (Modeling)it.next(); if
				 * (model.getSort().equals(ModelingSort.MODELINGSORT_PREDICTION)) {
				 *  } }
				 */
				// images/model_mini.gif
			}
		}
		else if (nodeObject instanceof SchemePath) {
			/*
			 * StorableObjectCondition condition = new DomainCondition(domain,
			 * ObjectEntities.MODELING_ENTITY_CODE); List models =
			 * MeasurementStorableObjectPool.getStorableObjectsByCondition(condition,
			 * true); for (Iterator it = models.iterator(); it.next();) {
			 * Modeling model = (Modeling)it.next(); if
			 * (model.getSort.equals("modeled")) }
			 */

			// "images/prognosis_mini.gif"
		}
		else if (nodeObject instanceof MeasurementSetup) {
			MeasurementSetup setup = (MeasurementSetup) nodeObject;
			LinkedIdsCondition condition = new LinkedIdsCondition(setup.getId(), ObjectEntities.TEST_ENTITY_CODE);
			try {
				condition.setEntityCode(ObjectEntities.TEST_ENTITY_CODE);
				Collection tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				for (Iterator it = tests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					list.add(new ObjectResourceTreeNode(test, test.getDescription(), true));
				}
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		else if (nodeObject instanceof Test) {
			Test test = (Test) nodeObject;
			LinkedIdsCondition condition = new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_ENTITY_CODE);
			try {
				Collection measurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				List measurementIds = new LinkedList();
				for (Iterator it = measurements.iterator(); it.hasNext();) {
					Measurement measurement = (Measurement) it.next();
					measurementIds.add(measurement.getId());
				}
				condition.setEntityCode(ObjectEntities.RESULT_ENTITY_CODE);
				condition.setLinkedIds(measurementIds);
				for (Iterator iter = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true)
						.iterator(); iter.hasNext();) {
					Result result = (Result) iter.next();
					if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
						ImageIcon icon;
						// FIXME: should be uncommented and fixed; hidden by saa
						// because of modified module measurement_v1
						// if
						// (r.getAlarmLevel().equals(AlarmLevel.ALARM_LEVEL_HARD))
						// {
						// icon = new
						// ImageIcon(Toolkit.getDefaultToolkit().getImage(
						// "images/alarm_bell_red.gif").getScaledInstance(15,
						// 15, Image.SCALE_SMOOTH));
						// }
						// else if
						// (r.getAlarmLevel().equals(AlarmLevel.ALARM_LEVEL_SOFT))
						// {
						// icon = new
						// ImageIcon(Toolkit.getDefaultToolkit().getImage(
						// "images/alarm_bell_yellow.gif").getScaledInstance(15,
						// 15, Image.SCALE_SMOOTH));
						// }
						// else
						{
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif")
									.getScaledInstance(15, 15, Image.SCALE_SMOOTH));

						}
						list.add(new ObjectResourceTreeNode(result, ((Measurement) result.getAction()).getName(), true, icon,
															true));
					}
				}
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		else if (nodeObject instanceof Date) {
			Date startDate = (Date) nodeObject;
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			Date endDate = calendar.getTime();

			ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.getParent();
			if (parent.getObject().equals("dates")) {
				TypicalCondition condition1 = new TypicalCondition(startDate, endDate,
																	OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_END_TIME);
				TypicalCondition condition2 = new TypicalCondition(startDate, endDate,
																	OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_START_TIME);
				StorableObjectCondition condition;
				try {
					condition = new CompoundCondition(condition1, CompoundConditionSort.OR, condition2);
				} catch (CreateObjectException e) {
					// it's cannot be occur
					throw new UnsupportedOperationException();
				}
				try {
					Collection tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = tests.iterator(); it.hasNext();) {
						Test test = (Test) it.next();
						list.add(new ObjectResourceTreeNode(test, test.getDescription(), true));
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (parent.getObject().equals("alarms")) {

			}
		}
		return list;
	}
}
