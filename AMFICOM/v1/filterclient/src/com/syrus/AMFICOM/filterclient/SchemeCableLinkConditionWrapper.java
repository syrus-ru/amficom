/*-
 * $Id: SchemeCableLinkConditionWrapper.java,v 1.3 2006/02/28 15:20:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.filterclient;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @author max
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2006/02/28 15:20:00 $
 * @module filterclient
 */

public class SchemeCableLinkConditionWrapper implements ConditionWrapper {
private final static short entityCode = SCHEMECABLELINK_CODE;
	
	private final static String NAME = LangModelFilter.getString("filter.criteria.entityname");
		
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
