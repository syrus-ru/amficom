/**
 * $Id: CollectorConditionWrapper.java,v 1.4 2005/09/25 15:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

public class CollectorConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.COLLECTOR_CODE;
	
	private final static String NAME = LangModelMap.getString(MapEditorResourceKeys.LABEL_NAME);
		
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
