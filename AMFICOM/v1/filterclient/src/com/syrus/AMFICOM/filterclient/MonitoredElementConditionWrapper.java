/*-
 * $Id: MonitoredElementConditionWrapper.java,v 1.6 2005/10/18 07:36:47 max Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
import com.syrus.AMFICOM.newFilter.LangModelFilter;
/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.6 $, $Date: 2005/10/18 07:36:47 $
 * @module filterclient
 */
public class MonitoredElementConditionWrapper implements ConditionWrapper {
	
	private final static short entityCode = ObjectEntities.MONITOREDELEMENT_CODE;
	
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