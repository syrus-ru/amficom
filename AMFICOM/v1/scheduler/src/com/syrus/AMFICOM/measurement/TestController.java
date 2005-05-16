/*
 * $Id: TestController.java,v 1.10 2005/05/16 12:54:51 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/16 12:54:51 $
 * @author $Author: bob $
 * @module module
 */
public class TestController extends ObjectResourceController {

	public static final String		KEY_TEMPORAL_TYPE		= "TemporalType";
	public static final String		KEY_TEMPORAL_TYPE_NAME	= "TemporalTypeName";
	public static final String		KEY_KIS					= "RTU";
	public static final String		KEY_MONITORED_ELEMENT	= "MonitoredElement";
	public static final String		KEY_TEST_OBJECT			= "TestObject";
	public static final String		KEY_MEASUREMENT_TYPE	= "MeasurementType";
	public static final String		KEY_START_TIME			= "TestStartTime";
	public static final String		KEY_STATUS				= "Status";

	private List					keys;

	private static boolean			initialized				= false;
	private static TestController	instance				= null;
	private static Object			lock					= new Object();

	private Map						statusMap;
	private Map						temporalTypeMap;

	private class ComparableLabel extends JLabel implements Comparable {
		
		private static final long	serialVersionUID	= 3546642105390084920L;

		public ComparableLabel(String string) {
			super(string);
		}
		
		public int compareTo(Object o) {
			return ((ComparableLabel)o).getText().compareTo(this.getText());
		}
	}
	
	private TestController() {

		//		 empty private constructor
		String[] keysArray = new String[] { KEY_TEMPORAL_TYPE, KEY_MONITORED_ELEMENT, KEY_TEST_OBJECT,
				KEY_MEASUREMENT_TYPE, KEY_START_TIME, KEY_STATUS};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

		this.statusMap = new HashMap();
		this.addStatusItem(TestStatus.TEST_STATUS_ABORTED, LangModelSchedule.getString("Aborted"));
		this.addStatusItem(TestStatus.TEST_STATUS_COMPLETED, LangModelSchedule.getString("Completed"));
		this.addStatusItem(TestStatus.TEST_STATUS_NEW, LangModelSchedule.getString("New"));
		this.addStatusItem(TestStatus.TEST_STATUS_PROCESSING, LangModelSchedule.getString("Processing"));
		this.addStatusItem(TestStatus.TEST_STATUS_SCHEDULED, LangModelSchedule.getString("Scheduled"));

		this.temporalTypeMap = new HashMap();
		this.temporalTypeMap.put(LangModelSchedule.getString("Onetime"), TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME);
		this.temporalTypeMap.put(LangModelSchedule.getString("Periodical"), TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL);
		this.temporalTypeMap.put(LangModelSchedule.getString("Continual"), TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS);
	}

	private Component getStatusComponent(TestStatus testStatus, String name) {
		ComparableLabel label = new ComparableLabel(name);
		label.setOpaque(true);
		Color color = SchedulerModel.getColor(testStatus);
		label.setBackground(color);
		return label;
	}
	
	private void addStatusItem(TestStatus testStatus, String name) {
		this.statusMap.put(this.getStatusComponent(testStatus, name), testStatus);
	}
	
	public static TestController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new TestController();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	public String getKey(int index) {
		return this.keys.get(index).toString();
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		String name = null;
		if (key.equals(KEY_TEMPORAL_TYPE))
			name = LangModelSchedule.getString("TemporalType"); //$NON-NLS-1$
		else if (key.equals(KEY_KIS))
			name = LangModelSchedule.getString("RTU"); //$NON-NLS-1$
		else if (key.equals(KEY_MONITORED_ELEMENT))
			name = LangModelSchedule.getString("Port"); //$NON-NLS-1$
		else if (key.equals(KEY_TEST_OBJECT))
			name = LangModelSchedule.getString("TestObject"); //$NON-NLS-1$
		else if (key.equals(KEY_MEASUREMENT_TYPE))
			name = LangModelSchedule.getString("Measurement_type"); //$NON-NLS-1$
		else if (key.equals(KEY_START_TIME))
			name = LangModelSchedule.getString("TestStartTime"); //$NON-NLS-1$
		else if (key.equals(KEY_STATUS))
			name = LangModelSchedule.getString("Status"); //$NON-NLS-1$

		return name;
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		if ((key.equals(KEY_TEMPORAL_TYPE)) || (key.equals(KEY_STATUS)))
			clazz = Map.class;
		return clazz;
	}

	public Object getPropertyValue(String key) {
		Object value = null;
		if (key.equals(KEY_TEMPORAL_TYPE))
			value = this.temporalTypeMap;
		else if (key.equals(KEY_STATUS))
			value = this.statusMap;
		return value;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object object, String key) {
		Object value = null;
		if (object instanceof Test) {
			Test test = (Test) object;
			if (key.equals(KEY_TEMPORAL_TYPE)) {
				if (test.getGroupTestId() == null) {
					value = test.getTemporalType(); //$NON-NLS-1$
				} else {
					value = LangModelSchedule.getString("Sectional");
				}
			} else if (key.equals(KEY_TEMPORAL_TYPE_NAME)) {
				if (test.getGroupTestId() == null) {
					TestTemporalType temporalType = test.getTemporalType();
					for (Iterator iterator = this.temporalTypeMap.keySet().iterator(); iterator.hasNext();) {
						String name = (String) iterator.next();
						if ((this.temporalTypeMap.get(name)).equals(temporalType)) {
							value = name;
							break;
						}
					}
				} else {
					value = LangModelSchedule.getString("Sectional");
				}				
			}
			else if (key.equals(KEY_KIS))
				try {
					MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(mp.getKISId(), true);
					value = kis.getName();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			else if (key.equals(KEY_MONITORED_ELEMENT))
				value = test.getMonitoredElement().getName();
			else if (key.equals(KEY_TEST_OBJECT)){
				try {
					MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					value = mp.getName();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			else if (key.equals(KEY_MEASUREMENT_TYPE))
				try {
					value = ((MeasurementType)MeasurementStorableObjectPool.getStorableObject(test.getMeasurementTypeId(), true)).getDescription();
				} catch (ApplicationException e) {
					Log.errorMessage("TestController.getValue | key='" + key + "', cannot get " + test.getMeasurementTypeId() + " -- " + e.getMessage());
					e.printStackTrace();
				}
			else if (key.equals(KEY_START_TIME))
				value = SIMPLE_DATE_FORMAT.format(test.getStartTime()); //$NON-NLS-1$
			else if (key.equals(KEY_STATUS)) {
				return test.getStatus();
			}
		}
		return value;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Test) {
//			Test test = (Test) object;
		}
	}

}
