/*-
 * $Id: MonitoredElementConditionWrapper.java,v 1.3 2005/08/08 11:41:00 arseniy Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:41:00 $
 * @module filterclient
 */
public class MonitoredElementConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.MONITOREDELEMENT_CODE;
	
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
