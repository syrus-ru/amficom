/*
 * $Id: MeasurementTypeConditionWrapper.java,v 1.7 2005/05/24 13:45:42 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/24 13:45:42 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
	
	private ArrayList measurementPortTypes;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String CODENAME = "filter by codename";
	private static final String PORTTYPE = "filter by MeasurementPortTypes";
			
	private static String[] keys = {
			StorableObjectWrapper.COLUMN_CODENAME, PORTTYPE};
	private static String[] keyNames = {CODENAME, PORTTYPE};
	private static byte[] keyTypes = {ConditionWrapper.STRING, ConditionWrapper.LIST};
	
	public MeasurementTypeConditionWrapper(Collection initialMeasurementType,
			Collection measurementPortTypes) {
		
		this.measurementPortTypes = new ArrayList(measurementPortTypes);
		
		for (Iterator iter = initialMeasurementType.iterator(); iter.hasNext();) {
			MeasurementType measurementType = (MeasurementType) iter.next();
			this.storableObjectInitialName.put(measurementType, measurementType.getDescription());
		}
		
		String[] mtNames = new String[this.measurementPortTypes.size()];
		int i=0;
		for (Iterator iter = this.measurementPortTypes.iterator(); iter.hasNext();i++) {
			MeasurementPortType pt = (MeasurementPortType) iter.next();
			mtNames[i] = pt.getName();			
		}
		this.keyLinkedNames.put(keys[1], mtNames);
	}
	
	public String[] getLinkedNames(String key) throws IllegalDataException {
		return (String[]) this.keyLinkedNames.get(key);
	}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[1]))
			return ((MeasurementPortType)this.measurementPortTypes.get(indexNumber)).getId();
		throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedObject | Wrong key");
	}
	
	public Collection getInitialEntities() {
		return this.storableObjectInitialName.keySet();
	}
	
	public String getInitialName(StorableObject storableObject) {
		return (String) this.storableObjectInitialName.get(storableObject);		
	}
	
	public Collection getKeys() {return Collections.EMPTY_LIST;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
