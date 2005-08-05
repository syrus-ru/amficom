/*-
 * $Id: CableChannelingItemWrapper.java,v 1.10 2005/08/05 11:20:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/05 11:20:03 $
 * @author $Author: bass $
 * @module scheme
 */
public final class CableChannelingItemWrapper extends StorableObjectWrapper<CableChannelingItem> {
	public static final String COLUMN_START_SPARE = "start_spare";
	public static final String COLUMN_END_SPARE = "end_spare";
	public static final String COLUMN_ROW_X = "row_x";
	public static final String COLUMN_PLACE_Y = "place_y";
	public static final String COLUMN_SEQUENTIAL_NUMBER = "sequential_number";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_START_SITE_NODE_ID = "start_site_node_id";
	public static final String COLUMN_END_SITE_NODE_ID = "end_site_node_id";
	public static final String COLUMN_PARENT_SCHEME_CABLE_LINK_ID = "parent_scheme_cable_link_id";

	private static CableChannelingItemWrapper instance;

	public List<String> getKeys() {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public String getName(final String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	@Override
	public Class getPropertyClass(final String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public Object getPropertyValue(final String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	@Override
	public Object getValue(final CableChannelingItem object, final String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public boolean isEditable(final String key) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	@Override
	public void setValue(final CableChannelingItem object, final String key, final Object value) {
		throw new UnsupportedOperationException("CableChannelingItemWrapper | not implemented yet");
	}

	public static CableChannelingItemWrapper getInstance() {
		if (instance == null) {
			instance = new CableChannelingItemWrapper();
		}
		return instance;
	}
}
