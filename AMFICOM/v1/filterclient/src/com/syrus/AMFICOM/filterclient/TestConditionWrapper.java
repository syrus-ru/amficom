/*
 * $Id: TestConditionWrapper.java,v 1.3 2005/04/01 12:50:37 max Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/04/01 12:50:37 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_ENTITY_CODE;
	
	private ArrayList monitoredElements;
	private ArrayList measurementPorts;
	private ArrayList mcms;
	
	private Map keyLinkedNames;
	private Map storableObjectInitialName;
	
	private Collection initialCollection;
		
	
	private static final String STATUS = "search by status";
	private static final String ME = "search by monitored elements";
	private static final String MT = "search by measurementTypes";
	private static final String MCM = "search by MCMs";
		
	private static String[] keys = {TestWrapper.COLUMN_STATUS, ME, MT, MCM};
	private static String[] keyNames = {STATUS, ME, MT, MCM};
	private static byte[] keyTypes = {ConditionWrapper.CONSTRAINT, ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public TestConditionWrapper(Collection initialTests,
			Collection monitoredElements, Collection measurementPorts, Collection mcms) {
		this.initialCollection = initialTests;
		this.monitoredElements = new ArrayList(monitoredElements);
		this.measurementPorts = new ArrayList(measurementPorts);
		this.mcms = new ArrayList(mcms);
		
		
		this.keyLinkedNames = new HashMap();
		String[] testStatusNames = new String[]{"NEW", "SCHEDULED", "PROCESSING", "COMPLETED", "ABORTED" }; 
		this.keyLinkedNames.put(keys[0], testStatusNames);
		
		String[] meNames = new String[this.monitoredElements.size()];
		int i=0;
		for (Iterator iter = monitoredElements.iterator(); iter.hasNext();i++) {
			MonitoredElement me = (MonitoredElement) iter.next();
			meNames[i] = me.getName();			
		}
		this.keyLinkedNames.put(keys[1], meNames);
				
		String[] mpNames = new String[this.measurementPorts.size()];
		i=0;
		for (Iterator iter = this.measurementPorts.iterator(); iter.hasNext();) {
			MeasurementPort mp = (MeasurementPort) iter.next();
			mpNames[i] = mp.getName();
		}
		this.keyLinkedNames.put(keys[2], mpNames);
		
		String[] mcmNames = new String[this.mcms.size()];
		i=0;
		for (Iterator iter = this.mcms.iterator(); iter.hasNext();) {
			MCM mcm = (MCM) iter.next();
			mcmNames[i] = mcm.getName();			
		}
		this.keyLinkedNames.put(keys[3], mcmNames);
		
		this.storableObjectInitialName = new HashMap();
		for (Iterator iter = this.initialCollection.iterator(); iter.hasNext();) {
			Test test = (Test) iter.next();
			this.storableObjectInitialName.put(test, test.getDescription());
		}
	}

	public String[] getLinkedNames(String key) throws IllegalDataException {
		return (String[]) this.keyLinkedNames.get(key);
	}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[1])) {
			return ((MonitoredElement)this.monitoredElements.get(indexNumber)).getId();
		} else if (key.equals(keys[2])) {
			return ((MeasurementPort)this.measurementPorts.get(indexNumber)).getId();
		} else if (key.equals(keys[3])) {
			return ((MCM)this.mcms.get(indexNumber)).getId();
		} else {
			throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedObject | Wrong key");
		}
	}
	
	public Collection getInitialEntities() {
		return this.initialCollection;
	}
	
	public String getInitialName(StorableObject storableObject) {
		return (String) this.storableObjectInitialName.get(storableObject);		
	}
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
