/*
 * $Id: MeasurementConditionWrapper.java,v 1.1 2005/04/01 17:07:37 max Exp $
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

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/01 17:07:37 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class MeasurementConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENT_ENTITY_CODE;
	
	private ArrayList tests;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String TESTS = "filter by tests";
	
	private static String[] keys = {TESTS};
	private static String[] keyNames = {TESTS};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public MeasurementConditionWrapper(Collection initialMeasurements,
			Collection tests) {
		this.tests = new ArrayList(tests);
				
		for (Iterator iter = initialMeasurements.iterator(); iter.hasNext();) {
			Measurement measurement = (Measurement) iter.next();
			this.storableObjectInitialName.put(measurement, measurement.getName());
		}
		
		String[] testNames = new String[this.tests.size()];
		int i=0;
		for (Iterator iter = tests.iterator(); iter.hasNext();i++) {
			Test test = (Test) iter.next();
			testNames[i] = test.getDescription();
		}
		this.keyLinkedNames.put(keys[0], testNames);		
	}
	
	public String[] getLinkedNames(String key) {
		return (String[]) this.keyLinkedNames.get(key);
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
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[0]))
			return ((Test)this.tests.get(indexNumber)).getId();
		throw new IllegalDataException("TestConditionWrapper.getLinkedObject | Wrong key");
	}
}
