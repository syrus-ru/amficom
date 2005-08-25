/*
 * $Id: MeasurementSetupConditionWrapper.java,v 1.9 2005/08/25 10:56:12 max Exp $
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
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.9 $, $Date: 2005/08/25 10:56:12 $
 * @author $Author: max $
 * @module filterclient
 */
public class MeasurementSetupConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENTSETUP_CODE;
	
	private final static String NAME = "Entity Name"; 
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING));
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
