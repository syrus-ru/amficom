/*
 * $Id: MeasurementTypeConditionWrapperTemp.java,v 1.1 2005/03/17 10:08:06 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ConditionWrapperTemp;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/17 10:08:06 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapperTemp implements ConditionWrapperTemp {
	
	private short entityCode = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
	
	private ArrayList parameterTypes;
	private ArrayList measurementPortTypes;
	
	private Collection initialCollection;
		
	private static final String CODENAME = "search by field \"CODENAME\"";
	private static final String PORTTYPE = "search by MeasurementPortTypes";
	private static final String PARAMTYPE = "search by ParameterTypes";
		
	private String[] keys = {StorableObjectWrapper.COLUMN_CODENAME,
			MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
			StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID};
	private String[] keyNames = {CODENAME, PORTTYPE, PARAMTYPE};
	private byte[] keyTypes = {ConditionWrapper.STRING, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public MeasurementTypeConditionWrapperTemp(Collection initialMeasurementType,
			Collection measurementPortTypes, Collection parameterTypes) {
		this.initialCollection = initialMeasurementType;
		this.parameterTypes = new ArrayList(parameterTypes);
		this.measurementPortTypes = new ArrayList(measurementPortTypes);				
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
	
	public Collection getInitialEntities() {
		return initialCollection;
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
	
	public String getKeyName(int index) {
		return this.keyNames[index];
	}
	
	public String getKeyName(String key) {
		return this.keyNames[getIndex(key)];
	}
	
	private int getIndex(String key) {
		for (int i = 0; i < this.keys.length; i++) {
			String tempKey = this.keys[i];
			if(tempKey.equals(key))
				return i;
		}
		return -1;
	}
	
	public String getKey(int index) {return this.keys[index];}
	public String[] getKeys() {return this.keys;}
	public String[] getKeyNames() {return this.keyNames;}
	public short getEntityCode() {return this.entityCode;}
	public byte getType(int index) {return this.keyTypes[index];}
	
}
