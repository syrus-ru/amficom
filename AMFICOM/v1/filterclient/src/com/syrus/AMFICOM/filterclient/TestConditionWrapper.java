/*
 * $Id: TestConditionWrapper.java,v 1.5 2005/04/07 14:49:50 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/07 14:49:50 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_ENTITY_CODE;
	
	private ArrayList monitoredElements;
	private ArrayList measurementPorts;
	private ArrayList mcms;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String STATUS = "filter by status";
	private static final String NUMBER = "filter by number";
	private static final String STRING = "filter by string";
	private static final String START_TIME = "filter by start time";
	private static final String END_TIME = "filter by end time";
	private static final String ME = "filter by monitored elements";
	private static final String MP = "filter by measurement ports";
	private static final String MCM = "filter by MCMs";
		
	private static String[] keys = {TestWrapper.COLUMN_STATUS, NUMBER , STRING, TestWrapper.COLUMN_START_TIME, TestWrapper.COLUMN_END_TIME, ME, MP, MCM};
	private static String[] keyNames = {STATUS, NUMBER, STRING, START_TIME, END_TIME, ME, MP, MCM};
	private static byte[] keyTypes = {ConditionWrapper.CONSTRAINT, ConditionWrapper.INT, ConditionWrapper.STRING, ConditionWrapper.DATE, ConditionWrapper.DATE,ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public TestConditionWrapper(Collection initialTests,
			Collection monitoredElements, Collection measurementPorts, Collection mcms) {
		this.monitoredElements = new ArrayList(monitoredElements);
		this.measurementPorts = new ArrayList(measurementPorts);
		this.mcms = new ArrayList(mcms);
		
		
		this.keyLinkedNames = new HashMap();
		String[] testStatusNames = new String[]{"NEW", "SCHEDULED", "PROCESSING", "COMPLETED", "ABORTED" }; 
		this.keyLinkedNames.put(TestWrapper.COLUMN_STATUS, testStatusNames);
		
		String[] meNames = new String[this.monitoredElements.size()];
		int i=0;
		for (Iterator iter = this.monitoredElements.iterator(); iter.hasNext();i++) {
			MonitoredElement me = (MonitoredElement) iter.next();
			meNames[i] = me.getName();			
		}
		this.keyLinkedNames.put(ME, meNames);
				
		String[] mpNames = new String[this.measurementPorts.size()];
		i=0;
		for (Iterator iter = this.measurementPorts.iterator(); iter.hasNext();i++) {
			MeasurementPort mp = (MeasurementPort) iter.next();
			mpNames[i] = mp.getName();
		}
		this.keyLinkedNames.put(MP, mpNames);
		
		String[] mcmNames = new String[this.mcms.size()];
		i=0;
		for (Iterator iter = this.mcms.iterator(); iter.hasNext();i++) {
			MCM mcm = (MCM) iter.next();
			mcmNames[i] = mcm.getName();			
		}
		this.keyLinkedNames.put(MCM, mcmNames);
		
		this.storableObjectInitialName = new HashMap();
		for (Iterator iter = initialTests.iterator(); iter.hasNext();) {
			Test test = (Test) iter.next();
			this.storableObjectInitialName.put(test, test.getDescription());
		}
	}

	public String[] getLinkedNames(String key) throws IllegalDataException {
		return (String[]) this.keyLinkedNames.get(key);
	}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(TestWrapper.COLUMN_STATUS)) {
			return ((MonitoredElement)this.monitoredElements.get(indexNumber)).getId();
		} else if (key.equals(ME)) {
			return ((MonitoredElement)this.monitoredElements.get(indexNumber)).getId();
		} else if (key.equals(MP)) {
			return ((MeasurementPort)this.measurementPorts.get(indexNumber)).getId();
		} else if (key.equals(MCM)) {
			return ((MCM)this.mcms.get(indexNumber)).getId();
		} else {
			throw new IllegalDataException("TestConditionWrapper.getLinkedObject | Wrong key");
		}
	}
	
	public Collection getInitialEntities() {
		return this.storableObjectInitialName.keySet();
	}
	
	public String getInitialName(StorableObject storableObject) {
		return (String) this.storableObjectInitialName.get(storableObject);		
	}
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
