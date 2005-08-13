/*-
 * $Id: CableChannelingItemWrapper.java,v 1.11 2005/08/13 11:19:27 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/13 11:19:27 $
 * @author $Author: max $
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
	
	private final List<String> keys;
	
	private CableChannelingItemWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_START_SPARE,
				COLUMN_END_SPARE,
				COLUMN_ROW_X,
				COLUMN_PLACE_Y,
				COLUMN_SEQUENTIAL_NUMBER,
				COLUMN_PHYSICAL_LINK_ID,
				COLUMN_START_SITE_NODE_ID,
				COLUMN_END_SITE_NODE_ID,
				COLUMN_PARENT_SCHEME_CABLE_LINK_ID}));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_START_SPARE)
				|| key.equals(COLUMN_END_SPARE)) {
			return Double.class;
		} else if (key.equals(COLUMN_ROW_X)
				|| key.equals(COLUMN_PLACE_Y)
				|| key.equals(COLUMN_SEQUENTIAL_NUMBER)) {
			return Integer.class;
		} else if (key.equals(COLUMN_PHYSICAL_LINK_ID) 
				|| key.equals(COLUMN_START_SITE_NODE_ID)
				|| key.equals(COLUMN_END_SITE_NODE_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		// There is no property
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// There is no property
	}

	@Override
	@SuppressWarnings("boxing")
	public Object getValue(final CableChannelingItem cableChannelingItem, final String key) {
		final Object value = super.getValue(cableChannelingItem, key);
		if (value != null) {
			return value;
		}
		if (cableChannelingItem != null) {
			if (key.equals(COLUMN_START_SPARE)) {
				return cableChannelingItem.getStartSpare();
			} else if (key.equals(COLUMN_END_SPARE)) {
				return cableChannelingItem.getEndSpare();
			} else if (key.equals(COLUMN_ROW_X)) {
				return cableChannelingItem.getRowX();
			} else if (key.equals(COLUMN_PLACE_Y)) {
				return cableChannelingItem.getPlaceY();
			} else if (key.equals(COLUMN_PHYSICAL_LINK_ID)) {
				return cableChannelingItem.getPhysicalLinkId();
			} else if (key.equals(COLUMN_START_SITE_NODE_ID)) {
				return cableChannelingItem.getStartSiteNodeId();
			} else if (key.equals(COLUMN_END_SITE_NODE_ID)) {
				return cableChannelingItem.getEndSiteNodeId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)) {
				return cableChannelingItem.getParentSchemeCableLinkId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	@SuppressWarnings("boxing")
	public void setValue(final CableChannelingItem cableChannelingItem, final String key, final Object value) {
		if (cableChannelingItem != null) {
			if (key.equals(COLUMN_START_SPARE)) {
				cableChannelingItem.setStartSpare((Double) value);
			} else if (key.equals(COLUMN_END_SPARE)) {
				cableChannelingItem.setEndSpare((Double) value);
			} else if (key.equals(COLUMN_ROW_X)) {
				cableChannelingItem.setRowX((Integer) value);
			} else if (key.equals(COLUMN_PLACE_Y)) {
				cableChannelingItem.setPlaceY((Integer) value);
			} else if (key.equals(COLUMN_PHYSICAL_LINK_ID)) {
				cableChannelingItem.setPhysicalLinkId((Identifier) value);
			} else if (key.equals(COLUMN_START_SITE_NODE_ID)) {
				cableChannelingItem.setStartSiteNodeId((Identifier) value);
			} else if (key.equals(COLUMN_END_SITE_NODE_ID)) {
				cableChannelingItem.setEndSiteNodeId((Identifier) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)) {
				cableChannelingItem.setParentPathOwnerId((Identifier) value);
			}
		}
	}

	public static CableChannelingItemWrapper getInstance() {
		if (instance == null) {
			instance = new CableChannelingItemWrapper();
		}
		return instance;
	}
}
