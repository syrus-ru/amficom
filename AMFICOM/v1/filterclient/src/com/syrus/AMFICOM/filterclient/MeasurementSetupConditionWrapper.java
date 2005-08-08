/*
 * $Id: MeasurementSetupConditionWrapper.java,v 1.7 2005/08/08 11:41:00 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:41:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class MeasurementSetupConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENTSETUP_CODE;
	
	private final static String NAME = "Entity Name"; 
	private static ArrayList keys = new ArrayList();
	
	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING));
	}
	
	public Collection getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
