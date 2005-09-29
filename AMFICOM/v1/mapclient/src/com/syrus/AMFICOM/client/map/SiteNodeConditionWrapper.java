/*-
 * $Id: SiteNodeConditionWrapper.java,v 1.6 2005/09/29 10:58:28 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.PhysicalLinkWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/29 10:58:28 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public class SiteNodeConditionWrapper implements ConditionWrapper {

	private final static short entityCode = ObjectEntities.SITENODE_CODE;

	private final static String NAME = 
			LangModelMap.getString(MapEditorResourceKeys.LABEL_NAME);

	private final static String STREET = 
			LangModelMap.getString(MapEditorResourceKeys.LABEL_STREET);

	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();

	public static final ConditionKey NAME_CONDITION_KEY = new ConditionKey(
			StorableObjectWrapper.COLUMN_NAME,
			NAME,
			ConditionWrapper.STRING);
	public static final ConditionKey STREET_CONDITION_KEY = new ConditionKey(
			PhysicalLinkWrapper.COLUMN_STREET,
			STREET,
			ConditionWrapper.STRING);

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
