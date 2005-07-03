/*
 * $Id: EvaluationConditionWrapper.java,v 1.2 2005/06/17 11:01:05 bass Exp $
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
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/17 11:01:05 $
 * @author $Author: bass $
 * @module filterclient_v1
 */
public class EvaluationConditionWrapper {
	private static short entityCode = ObjectEntities.EVALUATION_CODE;
	
	private ArrayList measurements;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String MEASUREMENT = "filter by measurement";
	
	private static String[] keys = {MEASUREMENT};
	private static String[] keyNames = {MEASUREMENT};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public EvaluationConditionWrapper(Collection initialEvaluations,
			Collection measurements) {
		this.measurements = new ArrayList(measurements);
				
		for (Iterator iter = initialEvaluations.iterator(); iter.hasNext();) {
			Evaluation evaluation = (Evaluation) iter.next();
			this.storableObjectInitialName.put(evaluation, "evaluation " + evaluation.getCreated());
		}
		
		String[] measurementNames = new String[this.measurements.size()];
		int i=0;
		for (Iterator iter = this.measurements.iterator(); iter.hasNext();i++) {
			Measurement measurement = (Measurement) iter.next();
			measurementNames[i] = measurement.getName();
		}
		this.keyLinkedNames.put(keys[0], measurementNames);		
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
			return ((Measurement)this.measurements.get(indexNumber)).getId();
		throw new IllegalDataException("EvaluationConditionWrapper.getLinkedObject | Wrong key");
	}
}
