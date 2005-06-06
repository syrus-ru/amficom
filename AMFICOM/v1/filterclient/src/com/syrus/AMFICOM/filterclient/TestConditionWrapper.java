/*
 * $Id: TestConditionWrapper.java,v 1.7 2005/06/06 11:52:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/06 11:52:43 $
 * @author $Author: bob $
 * @module filterclient_v1
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_ENTITY_CODE;
	
	private static final String STATUS = "Status";
	private static final String START_TIME = "Start time";
	private static final String END_TIME = "End time";
	
	private static final String[] testStatusNames = new String[]{"NEW", "SCHEDULED", "PROCESSING", "COMPLETED", "ABORTED" }; 
	
	private static ArrayList keys = new ArrayList();
	
	static {
		keys.add(new ConditionKey(TestWrapper.COLUMN_STATUS, STATUS, testStatusNames));
		keys.add(new ConditionKey(TestWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE));
		keys.add(new ConditionKey(TestWrapper.COLUMN_END_TIME, END_TIME, ConditionWrapper.DATE));
		keys.add(new ConditionKey(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE));
		keys.add(new ConditionKey(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE));
		keys.add(new ConditionKey(ObjectEntities.MCM_ENTITY_CODE));		
	}
	
	public Collection getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}		
}
