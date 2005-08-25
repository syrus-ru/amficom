/*-
 * $Id: MonitoredElementConditionWrapper.java,v 1.5 2005/08/25 10:56:12 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.5 $, $Date: 2005/08/25 10:56:12 $
 * @module filterclient
 */
public class MonitoredElementConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.MONITOREDELEMENT_CODE;
	
	private final static String NAME = "Entity name";
		
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
