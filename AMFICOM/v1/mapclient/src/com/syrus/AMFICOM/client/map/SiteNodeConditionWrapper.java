/*-
 * $Id: SiteNodeConditionWrapper.java,v 1.8 2006/02/28 15:20:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.PhysicalLinkWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.8 $, $Date: 2006/02/28 15:20:00 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public class SiteNodeConditionWrapper implements ConditionWrapper {

	private final static short entityCode = SITENODE_CODE;

	private final static String NAME = 
			I18N.getString(MapEditorResourceKeys.LABEL_NAME);

	private final static String STREET = 
			I18N.getString(MapEditorResourceKeys.LABEL_STREET);

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
