/*
 * $Id: MeasurementTypeConditionWrapper.java,v 1.1 2005/03/16 08:16:21 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/16 08:16:21 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapper implements ConditionWrapper {
	
	private short entityCode = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
	
	private ArrayList parameterTypes;
	private ArrayList measurementPortTypes;
	
	private Collection initialCollection;
	//private Collection keys = new LinkedList();
	
	private Map nameKey = new HashMap();
	private Map keyType = new HashMap();
	//private Map keyLinkedCollection = new HashMap();
	
	private final static String CODENAME = "search by field \"CODENAME\"";
	private final static String PORTTYPE = "search by MeasurementPortTypes";
	private final static String PARAMTYPE = "search by ParameterTypes";
		
	public MeasurementTypeConditionWrapper(Collection initialMeasurementType,
			Collection measurementPortTypes, Collection parameterTypes) {
		
		this.initialCollection = initialMeasurementType;
		this.parameterTypes = new ArrayList(parameterTypes);
		this.measurementPortTypes = new ArrayList(measurementPortTypes);
		
		this.nameKey.put(CODENAME, StorableObjectWrapper.COLUMN_CODENAME);
		this.nameKey.put(PORTTYPE, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
		this.nameKey.put(PARAMTYPE, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
		
		this.keyType.put(StorableObjectWrapper.COLUMN_CODENAME, new Byte(ConditionWrapper.STRING));
		this.keyType.put(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID, new Byte(ConditionWrapper.LIST));		
		this.keyType.put(StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID, new Byte(ConditionWrapper.LIST));		
	}
	
	public Collection getKeyNames() {
		return this.nameKey.keySet();
	}
	
	public String getKey(String keyName) {
		return (String) this.nameKey.get(keyName);
	}
	
	public byte getType(String key) {
		return ((Byte) this.keyType.get(key)).byteValue();
	}
	
	public String[] getLinkedNames(String key) throws IllegalDataException {
		if (key.equals(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
			String[] names = new String[this.measurementPortTypes.size()];
			for (int i = 0; i < this.measurementPortTypes.size(); i++) {
				 MeasurementPortType mpt = (MeasurementPortType) this.measurementPortTypes.get(i);
				 names[i] = mpt.getName();
			}
			return names;
		} else if (key.equals(StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID)) {
			String[] names = new String[this.parameterTypes.size()];
			for (int i = 0; i < this.parameterTypes.size(); i++) {
				 ParameterType pt = (ParameterType) this.parameterTypes.get(i);
				 names[i] = pt.getName();
			}
			return names;
		} else {
			throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedNames | Wrong key");
		}
	}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
			return this.measurementPortTypes.get(indexNumber);
		} else if (key.equals(StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID)) {
			return this.parameterTypes.get(indexNumber);
		} else {
			throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedObject | Wrong key");
		}
	}
	
	public String[] getInitialNames() {
		String[] names = new String[this.initialCollection.size()];
		int i = 0;
		for (Iterator it = this.initialCollection.iterator(); it.hasNext();i++) {
			MeasurementType mt = (MeasurementType) it.next();
			names[i] = mt.getDescription();
		}
		return names;
	}

	public short getEntityCode() {
		return this.entityCode;
	}
	
	public Collection getKeys() {
		return this.nameKey.values();
	}
	
	
}
