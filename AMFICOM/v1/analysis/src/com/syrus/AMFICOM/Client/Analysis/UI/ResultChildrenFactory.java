/*-
 * $Id: ResultChildrenFactory.java,v 1.22 2006/02/16 13:10:37 saa Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.UI;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.filterclient.MeasurementConditionWrapper;
import com.syrus.AMFICOM.filterclient.MeasurementSetupConditionWrapper;
import com.syrus.AMFICOM.filterclient.MonitoredElementConditionWrapper;
import com.syrus.AMFICOM.filterclient.TestConditionWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.MonitoredElementWrapper;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: saa $
 * @version $Revision: 1.22 $, $Date: 2006/02/16 13:10:37 $
 * @module analysis
 */

public class ResultChildrenFactory extends AbstractChildrenFactory {

	private static final String	ROOT	= "result.root";
	private static final String	MONITOREDELEMENTS	= "monitoredelements";
	private static final String	MEASUREMENTSETUPS	= "measurementsetups";
	private static final String	DATES	= "dates";
//	private static final String	DATES_MEASURE	= "dates_measure";
//	private static final String	TODAY	= "today";
//	private static final String	YESTERDAY	= "yesterday";
//	private static final String	LASTWEEK	= "lastweek";
//	private static final String	LASTMONTH	= "lastmonth";
//	private static final String	ARBITRARYDATE	= "arbitrarydate";
	
	static SimpleDateFormat shortDate = new SimpleDateFormat("dd.MM"); 
	
	private Calendar	calendar;

	private PopulatableIconedNode root;
	
	public ResultChildrenFactory() {
		this.calendar = Calendar.getInstance();
	}
	
	public static String getRootObject() {
		return ROOT;
	}
	
	public PopulatableIconedNode getRoot() {
		if (root == null) {
			root = new PopulatableIconedNode(this, ROOT, LangModelAnalyse.getString(ROOT), true);
		}
		return root;
	}
	
	private FiltrableIconedNode createDateItem(Date startDate, Date endDate, String title, StorableObjectCondition addCondition, short entityCode) throws CreateObjectException{
		TypicalCondition timeCondition = new TypicalCondition(startDate,
				endDate, OperationSort.OPERATION_IN_RANGE,
				entityCode, TestWrapper.COLUMN_START_TIME);
		FiltrableIconedNode item2 = new FiltrableIconedNode();
		item2.setObject(startDate);
		item2.setName(title);
		item2.setChildrenFactory(this);
		item2.setDefaultCondition(new CompoundCondition(addCondition, CompoundConditionSort.AND, timeCondition));
		SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		Log.debugMessage(title + " (from " + sdf.format(startDate) + " to " + sdf.format(endDate) + ")", Level.FINEST);
		return item2;
	}
	
	public void populate(Item item) {
		Object nodeObject = item.getObject();
		Collection<Item> items = item.getChildren();
		Collection<Object> objects = super.getChildObjects(item);
		
		if (nodeObject instanceof String) {
			String s = (String) nodeObject;
			if (s.equals(ROOT)) {
				if (item.getChildren().isEmpty()) { // add only if no children as they are constant
					FiltrableIconedNode item2 = new FiltrableIconedNode();
					item2.setObject(MONITOREDELEMENTS);
//					item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
					item2.setName(LangModelAnalyse.getString("monitoredElements"));
					item2.setChildrenFactory(this);
					item2.setDefaultCondition(new LinkedIdsCondition(LoginManager.getDomainId(), ObjectEntities.MONITOREDELEMENT_CODE));
					item2.setFilter(new Filter(new MonitoredElementConditionWrapper()));
					item.addChild(item2);
				}
			} else if (s.equals(MONITOREDELEMENTS)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					Set<StorableObject> meSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
										
					List<Item> toRemove = super.getItemsToRemove(meSet, items);
					List<StorableObject> toAdd = super.getObjectsToAdd(meSet, objects);
					for (Item child : toRemove) {
						child.setParent(null);
					}

					Collections.sort(toAdd, new WrapperComparator(MonitoredElementWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
					
					int i = 0;
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						PopulatableIconedNode item2 = new PopulatableIconedNode();
						item2.setObject(me);
						item2.setName(me.getDisplayedName());
						item2.setChildrenFactory(this);
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));
						//XXX add possibility to insert item in arbitrary location
//							item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				} catch (IllegalObjectEntityException ex) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), ex.getMessage());
					Log.errorMessage(ex);
				} catch (ApplicationException ex) {
					Log.errorMessage(ex);
				}
			} else if (s.equals(DATES)) {
				List<Item> children = new LinkedList<Item>(item.getChildren());
				for (Item child : children) {
					child.setParent(null);
				}

				Date startDate;
				Date endDate;
				this.calendar.setTimeInMillis(System.currentTimeMillis());
								
				try {
					// today
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					startDate = this.calendar.getTime();
					this.calendar.setTime(startDate);
					this.calendar.set(Calendar.HOUR_OF_DAY, 0);
					this.calendar.set(Calendar.MINUTE, 0);
					this.calendar.set(Calendar.SECOND, 0);
					startDate = this.calendar.getTime();
					this.calendar.add(Calendar.DAY_OF_MONTH, 1);
					this.calendar.add(Calendar.SECOND, -1);
					endDate = this.calendar.getTime();					
					Item item2 = createDateItem(startDate, endDate, 
							LangModelAnalyse.getString("today") + " (" + shortDate.format(startDate) + ")", 
							condition, condition.getEntityCode().shortValue());
					item.addChild(item2);
					
					//yesterday
					this.calendar.add(Calendar.DAY_OF_MONTH, -1);
					endDate = this.calendar.getTime();
					this.calendar.add(Calendar.DAY_OF_MONTH, -1);
					this.calendar.add(Calendar.SECOND, 1);
					startDate = this.calendar.getTime();					
					Item item3 = createDateItem(startDate, endDate, 
							LangModelAnalyse.getString("yesterday") + " (" + shortDate.format(startDate) + ")", 
							condition, condition.getEntityCode().shortValue());
					item.addChild(item3);
					
					//last week
					this.calendar.add(Calendar.DAY_OF_MONTH, 2);
					this.calendar.add(Calendar.SECOND, -1);
					endDate = this.calendar.getTime();
					this.calendar.add(Calendar.WEEK_OF_MONTH, -1);
					this.calendar.add(Calendar.DAY_OF_MONTH, 1);
					this.calendar.add(Calendar.SECOND, 1);
					startDate = this.calendar.getTime();
					Item item4 = createDateItem(startDate, endDate, 
							LangModelAnalyse.getString("last_week") + " (" + shortDate.format(startDate) + "-" + shortDate.format(endDate) + ")", 
							condition, condition.getEntityCode().shortValue());
					item.addChild(item4);
					
					//last month
					this.calendar.add(Calendar.WEEK_OF_MONTH, 1);
					this.calendar.add(Calendar.MONTH, -1);
					startDate = this.calendar.getTime();
					Item item5 = createDateItem(startDate, endDate, 
							LangModelAnalyse.getString("last_month") + " (" + shortDate.format(startDate) + "-" + shortDate.format(endDate) + ")", 
							condition, condition.getEntityCode().shortValue());
					item.addChild(item5);
					
					endDate = new Date(System.currentTimeMillis());
					//arbitrary (default last half an hour)
					this.calendar.setTime(endDate);
					this.calendar.add(Calendar.MINUTE, -30);
					startDate = this.calendar.getTime();

					TypicalCondition timeCondition = new TypicalCondition(startDate,
							endDate, OperationSort.OPERATION_IN_RANGE,
							condition.getEntityCode().shortValue(), TestWrapper.COLUMN_START_TIME);
					FiltrableIconedNode item6 = new FiltrableIconedNode();
					item6.setObject(startDate);
					item6.setName(LangModelAnalyse.getString("by_filter"));
					item6.setChildrenFactory(this);
					item6.setDefaultCondition(condition);
					
//					item6.setDefaultOperation(CompoundConditionSort.OR);
					if (condition.getEntityCode().shortValue() == ObjectEntities.TEST_CODE) {
						Filter f = new Filter(new TestConditionWrapper());
						f.addCondition(timeCondition, TestConditionWrapper.START_TIME_CONDITION_KEY);
						item6.setFilter(f);
					} else if (condition.getEntityCode().shortValue() == ObjectEntities.MEASUREMENT_CODE) {
						Filter f = new Filter(new MeasurementConditionWrapper());
						f.addCondition(timeCondition, MeasurementConditionWrapper.START_TIME_CONDITION_KEY);
						item6.setFilter(f);
					}
					item.addChild(item6);
				} catch (CreateObjectException e) {
					Log.errorMessage(e);
				}
			/*} else if (s.equals(DATES_MEASURE)) {
				List<Item> children = new LinkedList<Item>(item.getChildren());
				for (Item child : children) {
					child.setParent(null);
				}

				Date startDate;
				Date endDate;
				this.calendar.setTimeInMillis(System.currentTimeMillis());
								
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					// last hour
					startDate = this.calendar.getTime();
					this.calendar.setTime(startDate);
					this.calendar.set(Calendar.SECOND, 0);
					this.calendar.add(Calendar.HOUR_OF_DAY, -1);
					startDate = this.calendar.getTime();
					endDate = new Date(System.currentTimeMillis());
					
					// today
					startDate = this.calendar.getTime();
					this.calendar.setTime(startDate);
					this.calendar.set(Calendar.HOUR_OF_DAY, 0);
					this.calendar.set(Calendar.MINUTE, 0);
					this.calendar.set(Calendar.SECOND, 0);
					startDate = this.calendar.getTime();
					this.calendar.add(Calendar.DAY_OF_MONTH, 1);
					this.calendar.add(Calendar.SECOND, -1);
					endDate = new Date(System.currentTimeMillis());					
					Item item2 = createDateItem(startDate, endDate, LangModelAnalyse.getString("today") + " (" + shortDate.format(startDate) + ")", condition);
					item.addChild(item2);
					
					//yesterday
					this.calendar.add(Calendar.DAY_OF_MONTH, -1);
					endDate = this.calendar.getTime();
					this.calendar.add(Calendar.DAY_OF_MONTH, -1);
					this.calendar.add(Calendar.SECOND, 1);
					startDate = this.calendar.getTime();					
					Item item3 = createDateItem(startDate, endDate, LangModelAnalyse.getString("yesterday") + " (" + shortDate.format(startDate) + ")", condition);
					item.addChild(item3);
					
					//last week
					this.calendar.add(Calendar.DAY_OF_MONTH, 2);
					this.calendar.add(Calendar.SECOND, -1);
					endDate = this.calendar.getTime();
					this.calendar.add(Calendar.WEEK_OF_MONTH, -1);
					this.calendar.add(Calendar.DAY_OF_MONTH, 1);
					this.calendar.add(Calendar.SECOND, 1);
					startDate = this.calendar.getTime();
					Item item4 = createDateItem(startDate, endDate, LangModelAnalyse.getString("last_week") + " (" + shortDate.format(startDate) + "-" + shortDate.format(endDate) + ")", condition);
					item.addChild(item4);
					
					//last month
					this.calendar.add(Calendar.WEEK_OF_MONTH, 1);
					this.calendar.add(Calendar.MONTH, -1);
					startDate = this.calendar.getTime();
					Item item5 = createDateItem(startDate, endDate, LangModelAnalyse.getString("last_month") + " (" + shortDate.format(startDate) + "-" + shortDate.format(endDate) + ")", condition);
					item.addChild(item5);
					
					endDate = new Date(System.currentTimeMillis());
					//arbitrary (default last half an hour)
					this.calendar.setTime(endDate);
					this.calendar.add(Calendar.MINUTE, -30);
					startDate = this.calendar.getTime();

					TypicalCondition timeCondition = new TypicalCondition(startDate,
							endDate, OperationSort.OPERATION_IN_RANGE,
							ObjectEntities.TEST_CODE, TestWrapper.COLUMN_START_TIME);
					FiltrableIconedNode item6 = new FiltrableIconedNode();
					item6.setObject(startDate);
					item6.setName(LangModelAnalyse.getString("by_filter"));
					item6.setChildrenFactory(this);
					item6.setDefaultCondition(condition);
					
//					item6.setDefaultOperation(CompoundConditionSort.OR);
					Filter f = new Filter(new TestConditionWrapper());
					f.addCondition(timeCondition, TestConditionWrapper.START_TIME_CONDITION_KEY);
					item6.setFilter(f);
					item.addChild(item6);
				} catch (CreateObjectException e) {
					Log.errorMessage(e);
				}*/
			} else if (s.equals(MEASUREMENTSETUPS)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					Set<StorableObject> meSetups = StorableObjectPool.getStorableObjectsByCondition(condition, true);

					List<Item> toRemove = super.getItemsToRemove(meSetups, items);
					List<StorableObject> toAdd = super.getObjectsToAdd(meSetups, objects);
					for (Item child : toRemove) {
						child.setParent(null);
					}

					Collections.sort(toAdd, new WrapperComparator(MeasurementSetupWrapper.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION));

					int i = 0;
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MeasurementSetup ms = (MeasurementSetup) it.next();

						FiltrableIconedNode item2 = new FiltrableIconedNode();
						item2.setObject(ms);
						item2.setName(ms.getDescription());
						item2.setChildrenFactory(this);
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_MEASUREMENT_SETUP));
						item2.setDefaultCondition(new LinkedIdsCondition(ms.getId(), ObjectEntities.TEST_CODE));
						item2.setFilter(new Filter(new TestConditionWrapper()));
							// XXX add possibility to insert item in arbitrary location
							//		item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		} else if (nodeObject instanceof MonitoredElement) {
			if (item.getChildren().isEmpty()) { // add only if no children as they are constant
				MonitoredElement me = (MonitoredElement) nodeObject;
				
				FiltrableIconedNode item1 = new FiltrableIconedNode();
				item1.setObject(DATES);
//				item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				item1.setName(LangModelAnalyse.getString("on_date"));
				item1.setChildrenFactory(this);
				item1.setDefaultCondition(new LinkedIdsCondition(me.getId(), ObjectEntities.TEST_CODE));
				item.addChild(item1);
				
				final StorableObjectCondition measurementStatusCondition = new TypicalCondition(MeasurementStatus._MEASUREMENT_STATUS_COMPLETED,
						0,
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.MEASUREMENT_CODE,
						MeasurementWrapper.COLUMN_STATUS);
				
				FiltrableIconedNode item2 = new FiltrableIconedNode();
				item2.setObject(DATES);
//				item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				item2.setName(LangModelAnalyse.getString("on_date_measurement"));
				item2.setChildrenFactory(this);
				item2.setDefaultCondition(new CompoundCondition(measurementStatusCondition, 
						CompoundConditionSort.AND, 
						new LinkedIdsCondition(me.getId(), ObjectEntities.MEASUREMENT_CODE)));
				item.addChild(item2);
				
				FiltrableIconedNode item3 = new FiltrableIconedNode();
				item3.setObject(MEASUREMENTSETUPS);
//				item3.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				item3.setName(LangModelAnalyse.getString("on_setup"));
				item3.setChildrenFactory(this);
				item3.setDefaultCondition(new LinkedIdsCondition(me.getId(), ObjectEntities.MEASUREMENTSETUP_CODE));
				item3.setFilter(new Filter(new MeasurementSetupConditionWrapper()));
				item.addChild(item3);
				
			}
		} else if (nodeObject instanceof Date || 
				nodeObject instanceof MeasurementSetup) {
			try {
				if (nodeObject instanceof MeasurementSetup) {
					StorableObjectPool.refresh(Collections.singleton(((MeasurementSetup)nodeObject).getId()));
				}
				
				StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
				Set<StorableObject> testSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
									
				List<Item> toRemove = super.getItemsToRemove(testSet, items);
				List<StorableObject> toAdd = super.getObjectsToAdd(testSet, objects);
				for (Item child : toRemove) {
					child.setParent(null);
				}
				
				if (condition.getEntityCode().shortValue() == ObjectEntities.TEST_CODE) {
					Collections.sort(toAdd, new WrapperComparator(TestWrapper.getInstance(), TestWrapper.COLUMN_START_TIME));
					int i = 0;
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					final StorableObjectCondition measurementStatusCondition = new TypicalCondition(MeasurementStatus._MEASUREMENT_STATUS_COMPLETED,
							0,
							OperationSort.OPERATION_EQUALS,
							ObjectEntities.MEASUREMENT_CODE,
							MeasurementWrapper.COLUMN_STATUS);
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						Test test = (Test) it.next();
						FiltrableIconedNode item2 = new FiltrableIconedNode();
						item2.setObject(test);
						item2.setName(sdf.format(test.getStartTime()));
						item2.setChildrenFactory(this);
						item2.setDefaultCondition(new CompoundCondition(measurementStatusCondition, 
								CompoundConditionSort.AND,
								new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_CODE)));
						item2.setFilter(new Filter(new MeasurementConditionWrapper()));
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_TESTING));
						//XXX add possibility to insert item in arbitrary location
//						item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				} else if (condition.getEntityCode().shortValue() == ObjectEntities.MEASUREMENT_CODE) {
					Collections.sort(toAdd, new WrapperComparator(MeasurementWrapper.getInstance(), MeasurementWrapper.COLUMN_START_TIME));
					int i = 0;
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						Measurement measurement = (Measurement) it.next();
						PopulatableIconedNode item2 = new PopulatableIconedNode();
						item2.setObject(measurement);
						item2.setName(measurement.getName());
						item2.setCanHaveChildren(false);
						item2.setChildrenFactory(this);
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_RESULT));
						// XXX add possibility to insert item in arbitrary location
						// item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				}
			} catch (ApplicationException ex) {
				Log.errorMessage(ex);
			}
		} 
		else if (nodeObject instanceof Test) {
			try {
				StorableObjectPool.refresh(Collections.singleton(((Test)nodeObject).getId()));
				StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
				
				final Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				
				List<Item> toRemove = super.getItemsToRemove(measurements, items);
				List<StorableObject> toAdd = super.getObjectsToAdd(measurements, objects);
				
				for (Item child : toRemove) {
					child.setParent(null);
				}
				Collections.sort(toAdd, new WrapperComparator(MeasurementWrapper.getInstance(), MeasurementWrapper.COLUMN_START_TIME));
				
				for (Iterator<StorableObject> iter = toAdd.iterator(); iter.hasNext();) {
					Measurement measurement = (Measurement)iter.next();
					PopulatableIconedNode item2 = new PopulatableIconedNode();
					item2.setObject(measurement);
					item2.setName(measurement.getName());
					item2.setCanHaveChildren(false);
					item2.setChildrenFactory(this);
					item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_RESULT));
					// XXX add possibility to insert item in arbitrary location
					// item.addChildAt(item2, i);
					item.addChild(item2);
				}
								
				
//				Set<Identifier> measurementIds = new HashSet<Identifier>();
//				for (Iterator it = measurements.iterator(); it.hasNext();) {
//					measurementIds.add(((Measurement)it.next()).getId());
//				}
				

	/*			if (!measurementIds.isEmpty()) {
					StorableObjectCondition condition2 = new LinkedIdsCondition(measurementIds, ObjectEntities.RESULT_CODE);
					Set<StorableObject> resultSet = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
					
					List<Item> toRemove = super.getItemsToRemove(resultSet, items);
					List<StorableObject> toAdd = super.getObjectsToAdd(resultSet, objects);
					for (Item child : toRemove) {
						child.setParent(null);
					}
					
					Collections.sort(toAdd, new WrapperComparator(ResultWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
					
					int i = 0;
					for (Iterator iter = toAdd.iterator(); iter.hasNext();) {
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

								PopulatableIconedNode item2 = new PopulatableIconedNode();
								item2.setObject(result);
								item2.setCanHaveChildren(false);
								item2.setName(((Measurement) result.getAction()).getName());
								item2.setChildrenFactory(this);
								item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_RESULT));
								// XXX add possibility to insert item in arbitrary location
								// item.addChildAt(item2, i);
								item.addChild(item2);
							i++;
						}
					}
				} else { // no measurements
					for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
						it.next();
						it.remove();
					}
				}*/
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		} 
	}
}
