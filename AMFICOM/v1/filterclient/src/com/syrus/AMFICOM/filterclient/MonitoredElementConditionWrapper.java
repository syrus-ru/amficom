/*-
 * $Id: MonitoredElementConditionWrapper.java,v 1.1 2005/06/15 08:13:39 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/06/15 08:13:39 $
 * @module filterclient_v1
 */
public class MonitoredElementConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.MONITOREDELEMENT_ENTITY_CODE;
	
	private final static String NAME = "Entity name";
		
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
