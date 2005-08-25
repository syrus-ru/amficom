/*
 * $Id: TestConditionWrapper.java,v 1.13 2005/08/25 10:56:12 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/25 10:56:12 $
 * @author $Author: max $
 * @module filterclient
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_CODE;
	
	//private static final String STATUS = "Status";
	private static final String START_TIME = "Start time";
	private static final String END_TIME = "End time";
	private static final String BYNAME = "Name";
	//private static final String[] testStatusNames = new String[]{"NEW", "SCHEDULED", "PROCESSING", "COMPLETED", "ABORTED" }; 
		
	public static ConditionKey START_TIME_CONDITION_KEY = new ConditionKey(TestWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE);
	public static ConditionKey END_TIME_CONDITION_KEY = new ConditionKey(TestWrapper.COLUMN_END_TIME, END_TIME, ConditionWrapper.DATE);
	public static ConditionKey NAME_CONDITION_KEY = new ConditionKey(StorableObjectWrapper.COLUMN_NAME, BYNAME, ConditionWrapper.STRING);
	
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	static {
		
		keys.add(START_TIME_CONDITION_KEY);
		keys.add(END_TIME_CONDITION_KEY);
		//keys.add(new ConditionKey(ObjectEntities.MONITOREDELEMENT_CODE));
		//keys.add(new ConditionKey(ObjectEntities.MEASUREMENTPORT_CODE));
		//keys.add(new ConditionKey(ObjectEntities.MCM_CODE));
		keys.add(NAME_CONDITION_KEY);
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}		
}
