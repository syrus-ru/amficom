/*
 * $Id: MeasurementTypeConditionWrapper.java,v 1.3 2005/03/30 14:24:14 max Exp $
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

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/30 14:24:14 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
	
	private ArrayList parameterTypes;
	private ArrayList measurementPortTypes;
	
	private Map keyLinkedNames;
	private Map storableObjectInitialName;
	
	private Collection initialCollection;
		
	private static final String CODENAME = "search by field \"CODENAME\"";
	private static final String PORTTYPE = "search by MeasurementPortTypes";
	private static final String PARAMTYPE = "search by ParameterTypes";
		
	private static String[] keys = {
			StorableObjectWrapper.COLUMN_CODENAME,
			MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
			StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID
			};
	private static String[] keyNames = {CODENAME, PORTTYPE, PARAMTYPE};
	private static byte[] keyTypes = {ConditionWrapper.STRING, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public MeasurementTypeConditionWrapper(Collection initialMeasurementType,
			Collection measurementPortTypes, Collection parameterTypes) {
		this.initialCollection = initialMeasurementType;
		this.parameterTypes = new ArrayList(parameterTypes);
		this.measurementPortTypes = new ArrayList(measurementPortTypes);
		
		this.keyLinkedNames = new HashMap();
		String[] ptNames = new String[this.parameterTypes.size()];
		int i=0;
		for (Iterator iter = this.parameterTypes.iterator(); iter.hasNext();i++) {
			ParameterType pt = (ParameterType) iter.next();
			ptNames[i] = pt.getName();			
		}
		this.keyLinkedNames.put(keys[2], ptNames);
		
		String[] mtNames = new String[this.measurementPortTypes.size()];
		i=0;
		for (Iterator iter = this.measurementPortTypes.iterator(); iter.hasNext();i++) {
			MeasurementPortType pt = (MeasurementPortType) iter.next();
			mtNames[i] = pt.getName();			
		}
		this.keyLinkedNames.put(keys[1], mtNames);
		
		this.storableObjectInitialName = new HashMap();
		for (Iterator iter = this.initialCollection.iterator(); iter.hasNext();) {
			MeasurementType test = (MeasurementType) iter.next();
			this.storableObjectInitialName.put(test, test.getDescription());
		}
		
	}
	
	public String[] getLinkedNames(String key) throws IllegalDataException {
		return (String[]) this.keyLinkedNames.get(key);
	}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[1])) {
			return ((MeasurementPortType)this.measurementPortTypes.get(indexNumber)).getId();
		} else if (key.equals(keys[2])) {
			return ((ParameterType)this.parameterTypes.get(indexNumber)).getId();
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
