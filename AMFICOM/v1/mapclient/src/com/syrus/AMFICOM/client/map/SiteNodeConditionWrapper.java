/**
 * $Id: SiteNodeConditionWrapper.java,v 1.4 2005/09/05 12:26:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.PhysicalLinkWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

public class SiteNodeConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.SITENODE_CODE;
	
	private final static String NAME = LangModelMap.getString("Name");
	private final static String STREET = LangModelMap.getString("Street");
	
	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();
	
	public static final ConditionKey NAME_CONDITION_KEY = new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING);
	public static final ConditionKey STREET_CONDITION_KEY = new ConditionKey(PhysicalLinkWrapper.COLUMN_STREET, STREET, ConditionWrapper.STRING);
	
	static {
		keys.add(NAME_CONDITION_KEY);
		keys.add(STREET_CONDITION_KEY);
	}
	
	public List<ConditionKey> getKeys() {
		return keys;
	}
	
	public short getEntityCode() {
		return entityCode;
	}
}
