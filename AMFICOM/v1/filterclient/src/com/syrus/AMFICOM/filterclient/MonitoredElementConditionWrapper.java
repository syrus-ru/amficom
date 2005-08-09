/*-
 * $Id: MonitoredElementConditionWrapper.java,v 1.4 2005/08/09 20:34:31 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/09 20:34:31 $
 * @module filterclient
 */
public class MonitoredElementConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.MONITOREDELEMENT_CODE;
	
	private final static String NAME = "Entity name";
		
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
