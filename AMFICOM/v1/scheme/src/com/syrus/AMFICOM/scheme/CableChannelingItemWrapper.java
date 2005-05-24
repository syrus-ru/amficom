/*-
 * $Id: CableChannelingItemWrapper.java,v 1.4 2005/05/24 13:58:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/24 13:58:41 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class CableChannelingItemWrapper extends StorableObjectWrapper {
	
	//TODO: insert creation sql
	
	public static final String COLUMN_START_SPARE = "start_spare";
	public static final String COLUMN_END_SPARE = "end_spare";
	public static final String COLUMN_ROW_X = "row_x";
	public static final String COLUMN_PLACE_Y = "place_y";
	public static final String COLUMN_SEQUENTIAL_NUMBER = "sequential_number";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_START_SITE_NODE_ID = "start_site_node_id";
	public static final String COLUMN_END_SITE_NODE_ID = "end_site_node_id";
	public static final String COLUMN_PARENT_SCHEME_CABLE_LINK_ID = "parent_scheme_sable_link_id";

	private static CableChannelingItemWrapper instance;

	public String getKey(int index) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public static CableChannelingItemWrapper getInstance() {
		if (instance == null)
			instance = new CableChannelingItemWrapper();
		return instance;
	}
}
