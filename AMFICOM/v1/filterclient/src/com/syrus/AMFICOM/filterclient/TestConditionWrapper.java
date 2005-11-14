/*
 * $Id: TestConditionWrapper.java,v 1.14 2005/11/14 15:03:27 max Exp $
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
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @version $Revision: 1.14 $, $Date: 2005/11/14 15:03:27 $
 * @author $Author: max $
 * @module filterclient
 */
public class TestConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.TEST_CODE;
	
	private static final String START_TIME = LangModelFilter.getString("filter.criteria.starttime");
	private static final String END_TIME = LangModelFilter.getString("filter.criteria.endtime");
	private static final String NAME = LangModelFilter.getString("filter.criteria.entityname");
			
	private static ConditionKey START_TIME_CONDITION_KEY = new ConditionKey(TestWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE);
	private static ConditionKey END_TIME_CONDITION_KEY = new ConditionKey(TestWrapper.COLUMN_END_TIME, END_TIME, ConditionWrapper.DATE);
	private static ConditionKey NAME_CONDITION_KEY = new ConditionKey(StorableObjectWrapper.COLUMN_DESCRIPTION, NAME, ConditionWrapper.STRING);
	
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	static {
		keys.add(START_TIME_CONDITION_KEY);
		keys.add(END_TIME_CONDITION_KEY);
		keys.add(NAME_CONDITION_KEY);
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}		
}
