/*
 * $Id: AnalysisConditionWrapper.java,v 1.1 2005/04/01 17:07:37 max Exp $
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
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/01 17:07:37 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class AnalysisConditionWrapper {
	
	private static short entityCode = ObjectEntities.ANALYSIS_ENTITY_CODE;
	
	private ArrayList measurements;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String MEASUREMENT = "filter by measurement";
	
	private static String[] keys = {MEASUREMENT};
	private static String[] keyNames = {MEASUREMENT};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public AnalysisConditionWrapper(Collection initialAnalyses,
			Collection measurements) {
		this.measurements = new ArrayList(measurements);
				
		for (Iterator iter = initialAnalyses.iterator(); iter.hasNext();) {
			Analysis analysis = (Analysis) iter.next();
			this.storableObjectInitialName.put(analysis, "analysis" + analysis.getType());
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
		throw new IllegalDataException("AnalysisConditionWrapper.getLinkedObject | Wrong key");
	}
}
