package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/01 12:51:14 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class AnalysisTypeConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.ANALYSISTYPE_ENTITY_CODE;
	
	private ArrayList parameterTypes;
	private ArrayList measurementTypes;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private Collection initialCollection;
	
	private static final String PARAM = "filter by parameter types";
	private static final String MT = "filter by measurement types";
	
	private static String[] keys = {PARAM, MT};
	private static String[] keyNames = {PARAM, MT};
	private static byte[] keyTypes = {ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public AnalysisTypeConditionWrapper(Collection initialTests,
			Collection parameterTypes, Collection measurementTypes) {
		this.initialCollection = initialTests;
		this.parameterTypes = new ArrayList(parameterTypes);
		this.measurementTypes = new ArrayList(measurementTypes);
		
		for (Iterator iter = this.initialCollection.iterator(); iter.hasNext();) {
			AnalysisType test = (AnalysisType) iter.next();
			this.storableObjectInitialName.put(test, test.getDescription());
		}
		
		String[] patamNames = new String[this.parameterTypes.size()];
		int i=0;
		for (Iterator iter = parameterTypes.iterator(); iter.hasNext();i++) {
			ParameterType pt = (ParameterType) iter.next();
			patamNames[i] = pt.getName();
		}
		this.keyLinkedNames.put(keys[0], patamNames);
		
		String[] mtNames = new String[this.measurementTypes.size()];
		i=0;
		for (Iterator iter = measurementTypes.iterator(); iter.hasNext();i++) {
			MeasurementType mt = (MeasurementType) iter.next();
			mtNames[i] = mt.getDescription();			
		}
		this.keyLinkedNames.put(keys[1], patamNames);
	}
	
	public String[] getLinkedNames(String key) {
		return (String[]) this.keyLinkedNames.get(key);
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
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[0])) {
			return ((ParameterType)this.parameterTypes.get(indexNumber)).getId();
		} else if (key.equals(keys[1])) {
			return ((MeasurementType)this.measurementTypes.get(indexNumber)).getId();
		} else { 
			throw new IllegalDataException("AnalysisTypeConditionWrapper.getLinkedObject | Wrong key");
		}
	}
}
