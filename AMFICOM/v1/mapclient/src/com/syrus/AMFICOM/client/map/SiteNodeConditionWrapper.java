/**
 * $Id: SiteNodeConditionWrapper.java,v 1.2 2005/08/25 11:16:22 max Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

public class SiteNodeConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.SITENODE_CODE;
	
	private final static String NAME = "Entity name";
		
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	public static final ConditionKey NAME_CONDITION_KEY = new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING);
	
	static {
		keys.add(NAME_CONDITION_KEY);
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
