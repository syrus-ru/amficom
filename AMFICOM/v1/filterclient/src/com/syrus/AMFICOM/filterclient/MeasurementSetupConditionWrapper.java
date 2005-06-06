/*
 * $Id: MeasurementSetupConditionWrapper.java,v 1.3 2005/06/06 11:52:43 bob Exp $
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

import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.MeasurementSetup;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/06 11:52:43 $
 * @author $Author: bob $
 * @module filterclient_v1
 */
public class MeasurementSetupConditionWrapper {
	
	private static short entityCode = ObjectEntities.MONITOREDELEMENT_ENTITY_CODE;
	
	private ArrayList monitoredElements; 
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String ME = "filter by monitiored elements";
	
	private static String[] keys = {ME};
	private static String[] keyNames = {ME};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public MeasurementSetupConditionWrapper(Collection initialMeasurementSetups,
			Collection monitoredElements) {
		this.monitoredElements = new ArrayList(monitoredElements);
				
		for (Iterator iter = initialMeasurementSetups.iterator(); iter.hasNext();) {
			MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
			this.storableObjectInitialName.put(measurementSetup, measurementSetup.getDescription());
		}
		
		String[] meNames = new String[this.monitoredElements.size()];
		int i=0;
		for (Iterator iter = this.monitoredElements.iterator(); iter.hasNext();i++) {
			MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			meNames[i] = monitoredElement.getName();
		}
		this.keyLinkedNames.put(keys[0], meNames);		
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
			return ((MonitoredElement)this.monitoredElements.get(indexNumber)).getId();
		throw new IllegalDataException("MeasurementSetupConditionWrapper.getLinkedObject | Wrong key");
	}
}
