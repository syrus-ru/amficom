/*-
 * $Id: TopologyConditionWrapper.java,v 1.6 2005/10/11 08:46:56 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.6 $, $Date: 2005/10/11 08:46:56 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public class TopologyConditionWrapper implements ConditionWrapper {

	// todo use stub entity code
	private final static short entityCode = ObjectEntities.COLLECTOR_CODE;

	private final static String NAME = 
			I18N.getString(MapEditorResourceKeys.LABEL_NAME);

	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();

	public static final ConditionKey NAME_CONDITION_KEY = new ConditionKey(
			StorableObjectWrapper.COLUMN_NAME,
			NAME,
			ConditionWrapper.STRING);

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
