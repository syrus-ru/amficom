/*
 * $Id: TestController.java,v 1.1.2.1 2004/10/19 09:58:31 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/19 09:58:31 $
 * @author $Author: bob $
 * @module module
 */
public class TestController implements ObjectResourceController {

	public static final String		KEY_TEMPORAL_TYPE		= "TemporalType";
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

	private TestController() {

		//		 empty private constructor
		String[] keysArray = new String[] { KEY_TEMPORAL_TYPE, KEY_MONITORED_ELEMENT, KEY_TEST_OBJECT,
				KEY_MEASUREMENT_TYPE, KEY_START_TIME, KEY_STATUS};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

		this.statusMap = new HashMap();
		this.statusMap.put("Aborded", TestStatus.TEST_STATUS_ABORTED);
		this.statusMap.put("Completed", TestStatus.TEST_STATUS_COMPLETED);
		this.statusMap.put("New", TestStatus.TEST_STATUS_NEW);
		this.statusMap.put("Processing", TestStatus.TEST_STATUS_PROCESSING);
		this.statusMap.put("Scheduled", TestStatus.TEST_STATUS_SCHEDULED);

		this.temporalTypeMap = new HashMap();
		this.temporalTypeMap.put("OneTime", TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME);
		this.temporalTypeMap.put("Periodical", TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL);
		this.temporalTypeMap.put("Continuos", TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS);
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
			name = LangModelSchedule.getString("MeasurementType"); //$NON-NLS-1$
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
			if (key.equals(KEY_TEMPORAL_TYPE))
				value = test.getTemporalType(); //$NON-NLS-1$
			else if (key.equals(KEY_KIS))
				try {
					MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(mp.getKISId(), true);
					value = kis.getName();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

			else if (key.equals(KEY_MONITORED_ELEMENT))
				value = test.getMonitoredElement().getName();
			else if (key.equals(KEY_TEST_OBJECT)){
				try {
					MeasurementPort mp = (MeasurementPort)MeasurementStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					value = mp.getName();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			else if (key.equals(KEY_MEASUREMENT_TYPE))
				value = test.getMeasurementType().getDescription();
			else if (key.equals(KEY_START_TIME))
				value = test.getStartTime(); //$NON-NLS-1$
			else if (key.equals(KEY_STATUS))
				value = test.getStatus(); //$NON-NLS-1$
		}
		return value;
	}

	public boolean isEditable(String key) {
		boolean editable = false;
		return editable;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Test) {
			Test test = (Test) object;
		}
	}

}
