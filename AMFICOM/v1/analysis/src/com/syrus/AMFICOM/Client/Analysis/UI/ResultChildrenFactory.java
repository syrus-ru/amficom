/*-
 * $Id: ResultChildrenFactory.java,v 1.5 2005/07/28 09:15:39 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
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
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementWrapper;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.filterclient.MeasurementConditionWrapper;
import com.syrus.AMFICOM.filterclient.MeasurementSetupConditionWrapper;
import com.syrus.AMFICOM.filterclient.MonitoredElementConditionWrapper;
import com.syrus.AMFICOM.filterclient.TestConditionWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
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
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultWrapper;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.newFilter.ConditionKey;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/28 09:15:39 $
 * @module analysis_v1
 */

public class ResultChildrenFactory implements ChildrenFactory {

	private static final String	ROOT	= "result.root";
	private static final String	MONITOREDELEMENTS	= "monitoredelements";
	private static final String	MEASUREMENTSETUPS	= "measurementsetups";
	private static final String	DATES	= "dates";
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
	
	private FiltrableIconedNode createDateItem(Date startDate, Date endDate, String title, StorableObjectCondition addCondition) throws CreateObjectException{
		TypicalCondition timeCondition = new TypicalCondition(startDate,
				endDate, OperationSort.OPERATION_IN_RANGE,
				ObjectEntities.TEST_CODE, TestWrapper.COLUMN_START_TIME);
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
		Collection<Object> objects = CommonUIUtilities.getChildObjects(item);
		
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
					item2.setFilter(new Filter(new MonitoredElementConditionWrapper(), null));
					item.addChild(item2);
				}
			} else if (s.equals(MONITOREDELEMENTS)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					Set<StorableObject> meSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
										
					List<Item> toRemove = CommonUIUtilities.getItemsToRemove(meSet, items);
					List<Object> toAdd = CommonUIUtilities.getObjectsToAdd(meSet, objects);
					for (Iterator it = toRemove.iterator(); it.hasNext();) {
						Item child = (Item)it.next();
						child.setParent(null);
					}

					Collections.sort(toAdd, new WrapperComparator(MonitoredElementWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
					
					int i = 0;
					for (Iterator it = toAdd.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						PopulatableIconedNode item2 = new PopulatableIconedNode();
						item2.setObject(me);
						item2.setName(me.getName());
						item2.setChildrenFactory(this);
						item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));
						//XXX add possibility to insert item in arbitrary location
//							item.addChildAt(item2, i);
						item.addChild(item2);
						i++;
					}
				} catch (IllegalObjectEntityException ex) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), ex.getMessage());
					Log.errorException(ex);
				} catch (ApplicationException ex) {
					Log.errorException(ex);
				}
			} else if (s.equals(DATES)) {
				/*if (item.getChildren().isEmpty()) { // add only if no children as they are constant
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					
					FiltrableIconedNode item2 = new FiltrableIconedNode();
					item2.setObject(TODAY);
					item2.setName("Сегодня");
					item2.setChildrenFactory(this);
					item2.setDefaultCondition(condition);
					item.addChild(item2);

					FiltrableIconedNode item3 = new FiltrableIconedNode();
					item3.setObject(YESTERDAY);
					item3.setName("Вчера");
					item3.setChildrenFactory(this);
					item3.setDefaultCondition(condition);
					item.addChild(item2);
					
					FiltrableIconedNode item4 = new FiltrableIconedNode();
					item4.setObject(LASTWEEK);
					item4.setName("За неделю");
					item4.setChildrenFactory(this);
					item4.setDefaultCondition(condition);
					item.addChild(item2);
					
					FiltrableIconedNode item5 = new FiltrableIconedNode();
					item5.setObject(LASTMONTH);
					item5.setName("За месяц");
					item5.setChildrenFactory(this);
					item5.setDefaultCondition(condition);
					item.addChild(item2);
					
					FiltrableIconedNode item6 = new FiltrableIconedNode();
					item6.setObject(ARBITRARYDATE);
					item6.setName("По фильтру");
					item6.setChildrenFactory(this);
					item6.setDefaultCondition(condition);
					item.addChild(item2);

				}*/
				
				List<Item> children = new LinkedList<Item>(item.getChildren());
				for (Iterator it = children.iterator(); it.hasNext();) {
					Item child = (Item)it.next();
					child.setParent(null);
				}
			
				
				Date startDate;
				Date endDate;
								
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
					Filter f = new Filter(new TestConditionWrapper(), null);
					f.addCondition(timeCondition, new ConditionKey(TestWrapper.COLUMN_START_TIME, "Start time", ConditionWrapper.DATE));
					item6.setFilter(f);
					item.addChild(item6);
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
			} else if (s.equals(MEASUREMENTSETUPS)) {
				try {
					StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
					Set<StorableObject> meSetups = StorableObjectPool.getStorableObjectsByCondition(condition, true);

					List<Item> toRemove = CommonUIUtilities.getItemsToRemove(meSetups, items);
					List<Object> toAdd = CommonUIUtilities.getObjectsToAdd(meSetups, objects);
					for (Iterator it = toRemove.iterator(); it.hasNext();) {
						Item child = (Item)it.next();
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
						item2.setFilter(new Filter(new TestConditionWrapper(), null));
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
				
				FiltrableIconedNode item2 = new FiltrableIconedNode();
				item2.setObject(DATES);
//				item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				item2.setName(LangModelAnalyse.getString("on_date"));
				item2.setChildrenFactory(this);
				item2.setDefaultCondition(new LinkedIdsCondition(me.getId(), ObjectEntities.TEST_CODE));
				item.addChild(item2);
				
				FiltrableIconedNode item3 = new FiltrableIconedNode();
				item3.setObject(MEASUREMENTSETUPS);
//				item3.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				item3.setName(LangModelAnalyse.getString("on_setup"));
				item3.setChildrenFactory(this);
				item3.setDefaultCondition(new LinkedIdsCondition(me.getId(), ObjectEntities.MEASUREMENTSETUP_CODE));
				item3.setFilter(new Filter(new MeasurementSetupConditionWrapper(), null));
				item.addChild(item3);
				
			}
		} else if (nodeObject instanceof Date || 
				nodeObject instanceof MeasurementSetup) {
			try {
				StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
				Set<StorableObject> testSet = StorableObjectPool.getStorableObjectsByCondition(condition, true);
									
				List<Item> toRemove = CommonUIUtilities.getItemsToRemove(testSet, items);
				List<Object> toAdd = CommonUIUtilities.getObjectsToAdd(testSet, objects);
				for (Iterator it = toRemove.iterator(); it.hasNext();) {
					Item child = (Item)it.next();
					child.setParent(null);
				}

				Collections.sort(toAdd, new WrapperComparator(TestWrapper.getInstance(), TestWrapper.COLUMN_START_TIME));
				int i = 0;
				SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
				for (Iterator it = toAdd.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					FiltrableIconedNode item2 = new FiltrableIconedNode();
					item2.setObject(test);
					item2.setName(sdf.format(test.getStartTime()));
					item2.setChildrenFactory(this);
					item2.setDefaultCondition(new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_CODE));
					item2.setFilter(new Filter(new MeasurementConditionWrapper(), null));
					item2.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_TESTING));
						//XXX add possibility to insert item in arbitrary location
//						item.addChildAt(item2, i);
					item.addChild(item2);
					i++;
				}
			} catch (ApplicationException ex) {
				Log.errorException(ex);
			}
		} 
		else if (nodeObject instanceof Test) {
			try {
				StorableObjectCondition condition = ((FiltrableIconedNode)item).getResultingCondition();
				Set measurements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				Set<Identifier> measurementIds = new HashSet<Identifier>();
				for (Iterator it = measurements.iterator(); it.hasNext();) {
					measurementIds.add(((Measurement)it.next()).getId());
				}
				if (!measurementIds.isEmpty()) {
					StorableObjectCondition condition2 = new LinkedIdsCondition(measurementIds, ObjectEntities.RESULT_CODE);
					Set<StorableObject> resultSet = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
					
					List<Item> toRemove = CommonUIUtilities.getItemsToRemove(resultSet, items);
					List<Object> toAdd = CommonUIUtilities.getObjectsToAdd(resultSet, objects);
					for (Iterator it = toRemove.iterator(); it.hasNext();) {
						Item child = (Item)it.next();
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
				}
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		} 
	}
}
