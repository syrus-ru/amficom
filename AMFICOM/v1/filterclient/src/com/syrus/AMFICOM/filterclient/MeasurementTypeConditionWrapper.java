/*
 * $Id: MeasurementTypeConditionWrapper.java,v 1.2 2005/03/25 11:23:08 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/25 11:23:08 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapper implements ConditionWrapper {
	
	private short entityCode = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
	
	private ArrayList parameterTypes;
	private ArrayList measurementPortTypes;
	
	private Collection initialCollection;
		
	private static final String CODENAME = "search by field \"CODENAME\"";
	private static final String PORTTYPE = "search by MeasurementPortTypes";
	private static final String PARAMTYPE = "search by ParameterTypes";
		
	private String[] keys = {
			StorableObjectWrapper.COLUMN_CODENAME,
			MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
			StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID
			};
	private String[] keyNames = {CODENAME, PORTTYPE, PARAMTYPE};
	private byte[] keyTypes = {ConditionWrapper.STRING, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public MeasurementTypeConditionWrapper(Collection initialMeasurementType,
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
			return ((MeasurementPortType)this.measurementPortTypes.get(indexNumber)).getId();
		} else if (key.equals(StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID)) {
			return ((ParameterType)this.parameterTypes.get(indexNumber)).getId();
		} else {
			throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedObject | Wrong key");
		}
	}
	
	public Collection getInitialEntities() {
		return this.initialCollection;
	}
	
	public String getInitialName(StorableObject storableObject) {
		MeasurementType mt = (MeasurementType) storableObject;	
		return mt.getDescription();		
	}
	
	public String[] getKeys() {return this.keys;}
	public String[] getKeyNames() {return this.keyNames;}
	public short getEntityCode() {return this.entityCode;}
	public byte[] getTypes() {return this.keyTypes;}

	
}
