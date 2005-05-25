/*-
 * $Id: ArchiveChildrenFactory.java,v 1.9 2005/05/25 15:15:09 stas Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.UI;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.PopulatableItem;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.9 $, $Date: 2005/05/25 15:15:09 $
 * @author $Author: stas $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public class ArchiveChildrenFactory implements ChildrenFactory {

	private static ArchiveChildrenFactory	instance;

	private Identifier						domainId;

	private Calendar						calendar;

	private Date							initialDate;

	public static final String				ROOT				= "root";
	private static final String				ALARMS				= "alarms";
	private static final String				CALCULATED			= "calculated";
	private static final String				DATES				= "dates";
	private static final String				MEASUREMENTS		= "measurements";
	private static final String				MEASUREMENTSETUPS	= "measurementsetups";
	private static final String				MODELS				= "models";
	private static final String				PREDICTED			= "predicted";

	private SimpleDateFormat				dateFormat;

	private ArchiveChildrenFactory() {
		// singleton
		this.calendar = Calendar.getInstance();
		/* TODO maybe more resonable relative date ? */
		this.calendar.add(Calendar.YEAR, -1);
		this.calendar.set(Calendar.DAY_OF_MONTH, 1);
		this.calendar.set(Calendar.HOUR_OF_DAY, 0);
		this.calendar.set(Calendar.MINUTE, 0);
		this.calendar.set(Calendar.SECOND, 0);
		this.initialDate = this.calendar.getTime();
		/* TODO maybe putout here */
		this.dateFormat = new SimpleDateFormat("MMMMMMM yyyy");
	}

	public static ArchiveChildrenFactory getInstance() {
		if (instance == null)
			instance = new ArchiveChildrenFactory();
		return instance;
	}

	public void populate(Item item) {
		Object nodeObject = item.getObject();
		if (nodeObject instanceof String) {
			String s = (String) nodeObject;
			if (s.equals(ROOT)) {
				PopulatableItem item2 = new PopulatableItem();
				item2.setObject(MEASUREMENTS);
				item2.setName("Измерения");
				item2.setChildrenFactory(this);
				item.addChild(item2);

				PopulatableItem item3 = new PopulatableItem();
				item3.setObject(MODELS);
				item3.setName("Модели");
				item3.setChildrenFactory(this);
				item.addChild(item3);

			} else if (s.equals(MEASUREMENTS) || s.equals(PREDICTED)) {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.domainId, ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
				try {
					Set meSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					List list = new LinkedList(meSet);

					Collections.sort(list, new WrapperComparator(MonitoredElementWrapper.getInstance(),
																	StorableObjectWrapper.COLUMN_NAME));
					for (Iterator it = list.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						IconPopulatableItem item2 = new IconPopulatableItem();
						item2.setObject(me);
						item2.setName(me.getName());
						item2.setChildrenFactory(this);

						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));

						item.addChild(item2);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals(MODELS)) {
				PopulatableItem item2 = new PopulatableItem();
				item2.setObject(CALCULATED);
				item2.setName("Расчетные");
				item2.setChildrenFactory(this);
				item.addChild(item2);

				PopulatableItem item3 = new PopulatableItem();
				item3.setObject(PREDICTED);
				item3.setName("Прогнозируемые");
				item3.setChildrenFactory(this);
				item.addChild(item3);
			} else if (s.equals(CALCULATED)) {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.domainId,
																		ObjectEntities.SCHEME_PATH_ENTITY_CODE);
				try {
					Set paths = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = paths.iterator(); it.hasNext();) {
						SchemePath path = (SchemePath) it.next();
						IconPopulatableItem item2 = new IconPopulatableItem();
						item2.setObject(path);
						item2.setName(path.getName());
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));
						item2.setChildrenFactory(this);
						item.addChild(item2);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals(MEASUREMENTSETUPS)) {
				Item parent = item.getParent();
				MonitoredElement me = (MonitoredElement) parent.getObject();
				LinkedIdsCondition condition = new LinkedIdsCondition(me.getId(), ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
				try {
					Collection mSetups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mSetups.iterator(); it.hasNext();) {
						MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
						IconPopulatableItem item2 = new IconPopulatableItem();
						item2.setObject(measurementSetup);
						item2.setName(measurementSetup.getDescription());
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_MEASUREMENT_SETUP));
						item2.setChildrenFactory(this);
						item.addChild(item2);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (s.equals(DATES)) {
				calendar.setTime(initialDate);
				Date date = new Date(System.currentTimeMillis());
				while (calendar.getTime().before(date)) {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					Date time = calendar.getTime();
					PopulatableItem item2 = new PopulatableItem();
					item2.setObject(time);
					item2.setName(this.dateFormat.format(time));
					item2.setChildrenFactory(this);
					item.addChild(item2);
					calendar.add(Calendar.MONTH, 1);
				}
			} else if (s.equals(ALARMS)) {
				calendar.setTime(initialDate);
				Date date = new Date(System.currentTimeMillis());
				while (calendar.getTime().before(date)) {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					Date time = calendar.getTime();
					PopulatableItem item2 = new PopulatableItem();
					item2.setObject(time);
					item2.setName(this.dateFormat.format(time));
					item2.setChildrenFactory(this);
					item.addChild(item2);
					calendar.add(Calendar.MONTH, 1);
				}
			}
		} else if (nodeObject instanceof MonitoredElement) {
			MonitoredElement me = (MonitoredElement) nodeObject;
			Item parent = item.getParent();
			String str = (String) parent.getObject();
			if (str.equals(MEASUREMENTS)) {
				PopulatableItem item2 = new PopulatableItem();
				item2.setObject(MEASUREMENTSETUPS);
				item2.setName("По шаблонам");
				item2.setChildrenFactory(this);
				item.addChild(item2);

				PopulatableItem item3 = new PopulatableItem();
				item3.setObject(DATES);
				item3.setName("По дате");
				item3.setChildrenFactory(this);
				item.addChild(item3);

				PopulatableItem item4 = new PopulatableItem();
				item4.setObject(ALARMS);
				item4.setName("По сигналам тревоги");
				item4.setChildrenFactory(this);
				item.addChild(item4);
			} else if (str.equals(PREDICTED)) {
				/*
				 * ResultSortCondition condition =
				 * ResultSortCondition.getInstance(); // LinkedIdsCondition
				 * condition = LinkedIdsCondition.getInstance();
				 * condition.setDomain(domain);
				 * condition.setIdentifier(me.getId());
				 * condition.setEntityCode(ObjectEntities.MODELING_ENTITY_CODE);
				 * List models =
				 * MeasurementStorableObjectPool.getStorableObjectsByCondition(condition,
				 * true); for (Iterator it = models.iterator(); it.hasNext();) {
				 * Modeling model = (Modeling)it.next(); if
				 * (model.getSort().equals(ModelingSort.MODELINGSORT_PREDICTION)) { } }
				 */
				// images/model_mini.gif
			}
		} else if (nodeObject instanceof SchemePath) {
			/*
			 * StorableObjectCondition condition = new DomainCondition(domain,
			 * ObjectEntities.MODELING_ENTITY_CODE); List models =
			 * MeasurementStorableObjectPool.getStorableObjectsByCondition(condition,
			 * true); for (Iterator it = models.iterator(); it.next();) {
			 * Modeling model = (Modeling)it.next(); if
			 * (model.getSort.equals("modeled")) }
			 */

			// "images/prognosis_mini.gif"
		} else if (nodeObject instanceof MeasurementSetup) {
			MeasurementSetup setup = (MeasurementSetup) nodeObject;
			LinkedIdsCondition condition = new LinkedIdsCondition(setup.getId(), ObjectEntities.TEST_ENTITY_CODE);
			try {
				condition.setEntityCode(ObjectEntities.TEST_ENTITY_CODE);
				Collection tests = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				for (Iterator it = tests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					PopulatableItem item2 = new PopulatableItem();
					item2.setObject(test);
					item2.setName(test.getDescription());
					item2.setChildrenFactory(this);
					item.addChild(item2);
				}
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		} else if (nodeObject instanceof Test) {
			Test test = (Test) nodeObject;
			LinkedIdsCondition condition = new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_ENTITY_CODE);
			try {
				Collection measurements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				Set measurementIds = new HashSet();
				for (Iterator it = measurements.iterator(); it.hasNext();) {
					Measurement measurement = (Measurement) it.next();
					measurementIds.add(measurement.getId());
				}
				if (!measurementIds.isEmpty()) {
					condition.setEntityCode(ObjectEntities.RESULT_ENTITY_CODE);
					condition.setLinkedIds(measurementIds);
					for (Iterator iter = StorableObjectPool.getStorableObjectsByCondition(condition, true)
							.iterator(); iter.hasNext();) {
						Result result = (Result) iter.next();
						if (result.getSort().value() == ResultSort.RESULT_SORT_MEASUREMENT.value()) {
							// FIXME: should be uncommented and fixed; hidden by
							// saa
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

							IconPopulatableItem item2 = new IconPopulatableItem();
							item2.setObject(result);
							item2.setCanHaveChildren(false);
							item2.setName(((Measurement) result.getAction()).getName());
							item2.setChildrenFactory(this);
							item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_RESULT));
							item.addChild(item2);
						}
					}
				}
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		} else if (nodeObject instanceof Date) {
			Date startDate = (Date) nodeObject;
			Log.debugMessage("ArchiveChildrenFactory.populate | startDate is " + startDate, Log.FINEST);
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.SECOND, -1);
			Date endDate = calendar.getTime();
			Log.debugMessage("ArchiveChildrenFactory.populate | endDate is " + endDate, Log.FINEST);
			Item parent = item.getParent();
			Object parentObject = parent.getObject();
			if (parentObject.equals(DATES)) {
				StorableObjectCondition condition;
				TypicalCondition condition1 = new TypicalCondition(startDate, endDate,
																	OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_START_TIME);
				// */
				condition = condition1;
				/*
				 * / TypicalCondition condition2 = new
				 * TypicalCondition(startDate, endDate,
				 * OperationSort.OPERATION_IN_RANGE,
				 * ObjectEntities.TEST_ENTITY_CODE,
				 * TestWrapper.COLUMN_START_TIME); try { condition = new
				 * CompoundCondition(condition1, CompoundConditionSort.AND,
				 * condition2); } catch (CreateObjectException e) { // it's
				 * cannot be occur throw new UnsupportedOperationException(); } //
				 */
				try {
					Set tests = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					List list = new LinkedList(tests);

					Collections.sort(list, new WrapperComparator(TestWrapper.getInstance(),
																	TestWrapper.COLUMN_START_TIME));
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					for (Iterator it = list.iterator(); it.hasNext();) {
						Test test = (Test) it.next();
						PopulatableItem item2 = new PopulatableItem();
						item2.setObject(test);
						item2.setName(sdf.format(test.getStartTime()));
						item2.setChildrenFactory(this);
						item.addChild(item2);
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} else if (parentObject.equals(ALARMS)) {

			}
		}

	}

	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
	}

}
