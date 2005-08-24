/**
 * $Id: TopologyConditionWrapper.java,v 1.2 2005/08/24 10:09:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

public class TopologyConditionWrapper implements ConditionWrapper {
	
	//todo use stub entity code
	private final static short entityCode = ObjectEntities.COLLECTOR_CODE;
	
	private final static String NAME = LangModelMap.getString("Name");
		
	private static Collection<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING));
	}
	
	public Collection<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
