/*
 * $Id: MeasurementSetupConditionWrapper.java,v 1.12 2006/02/28 15:20:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @version $Revision: 1.12 $, $Date: 2006/02/28 15:20:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class MeasurementSetupConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = MEASUREMENTSETUP_CODE;
	
	private final static String NAME = LangModelFilter.getString("filter.criteria.entityname"); 
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_DESCRIPTION, NAME, ConditionWrapper.STRING));
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
