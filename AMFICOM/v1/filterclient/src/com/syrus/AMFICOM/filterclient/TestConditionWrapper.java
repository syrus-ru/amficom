/*
 * $Id: TestConditionWrapper.java,v 1.11 2005/08/08 11:41:00 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/08 11:41:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_CODE;
	
	//private static final String STATUS = "Status";
	private static final String START_TIME = "Start time";
	private static final String END_TIME = "End time";
	private static final String BYNAME = "Name";
	//private static final String[] testStatusNames = new String[]{"NEW", "SCHEDULED", "PROCESSING", "COMPLETED", "ABORTED" }; 
		
	private static ArrayList keys = new ArrayList();
	
	static {
		//keys.add(new ConditionKey(TestWrapper.COLUMN_STATUS, STATUS, testStatusNames));
		keys.add(new ConditionKey(TestWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE));
		keys.add(new ConditionKey(TestWrapper.COLUMN_END_TIME, END_TIME, ConditionWrapper.DATE));
		//keys.add(new ConditionKey(ObjectEntities.MONITOREDELEMENT_CODE));
		//keys.add(new ConditionKey(ObjectEntities.MEASUREMENTPORT_CODE));
		//keys.add(new ConditionKey(ObjectEntities.MCM_CODE));
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, BYNAME, ConditionWrapper.STRING));
	}
	
	public Collection getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}		
}
