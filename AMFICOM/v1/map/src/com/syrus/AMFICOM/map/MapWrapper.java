/*
 * $Id: MapWrapper.java,v 1.22 2006/03/13 15:54:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.22 $, $Date: 2006/03/13 15:54:26 $
 * @author $Author: bass $
 * @module map
 */
public final class MapWrapper extends StorableObjectWrapper<Map> {

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// domain_id VARCHAR2(32),
	public static final String COLUMN_DOMAIN_ID = "domain_id";
	// map_id VARCHAR2(32),
	public static final String LINK_COLUMN_MAP_ID = "map_id";
	// child_map_id VARCHAR2(32),
	public static final String LINK_COLUMN_CHILD_MAP_ID = "child_map_id";
	// collector_id VARCHAR2(32),
	public static final String LINK_COLUMN_COLLECTOR_ID = "collector_id";
	// mark_id VARCHAR2(32),
	public static final String LINK_COLUMN_MARK_ID = "mark_id";
	// node_link_id VARCHAR2(32),
	public static final String LINK_COLUMN_NODE_LINK_ID = "node_link_id";
	// physical_link_id VARCHAR2(32),
	public static final String LINK_COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	// site_node_id VARCHAR2(32),
	public static final String LINK_COLUMN_SITE_NODE_ID = "site_node_id";
	// topological_node_id VARCHAR2(32),
	public static final String LINK_COLUMN_TOPOLOGICAL_NODE_ID = "topological_node_id";
	// external_node_id VARCHAR2(32),
	public static final String LINK_COLUMN_EXTERNAL_NODE_ID = "ext_site_node_id";
	// external_node_id VARCHAR2(32),
	public static final String LINK_COLUMN_MAP_LIBRARY_ID	= "map_library_id";

	private static MapWrapper instance;

	private List<String> keys;

	protected MapWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DOMAIN_ID,
				LINK_COLUMN_COLLECTOR_ID,
				LINK_COLUMN_MARK_ID,
				LINK_COLUMN_NODE_LINK_ID,
				LINK_COLUMN_PHYSICAL_LINK_ID,
				LINK_COLUMN_SITE_NODE_ID,
				LINK_COLUMN_TOPOLOGICAL_NODE_ID,
				LINK_COLUMN_MAP_ID,
				LINK_COLUMN_EXTERNAL_NODE_ID,
				LINK_COLUMN_MAP_LIBRARY_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static MapWrapper getInstance() {
		if (instance == null)
			instance = new MapWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		if (key.equals(LINK_COLUMN_COLLECTOR_ID)
				|| key.equals(LINK_COLUMN_MARK_ID)
				|| key.equals(LINK_COLUMN_NODE_LINK_ID)
				|| key.equals(LINK_COLUMN_PHYSICAL_LINK_ID)
				|| key.equals(LINK_COLUMN_SITE_NODE_ID)
				|| key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID)
				|| key.equals(LINK_COLUMN_CHILD_MAP_ID)
				|| key.equals(LINK_COLUMN_EXTERNAL_NODE_ID)
				|| key.equals(LINK_COLUMN_MAP_LIBRARY_ID)) {
			return Set.class;
		}
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final Map map, final String key) {
		if (map != null) {
			if (key.equals(COLUMN_NAME)) {
				return map.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return map.getDescription();
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				return map.getDomainId();
			} else if (key.equals(LINK_COLUMN_COLLECTOR_ID)) {
				return map.getCollectors();
			} else if (key.equals(LINK_COLUMN_MARK_ID)) {
				return map.getMarks();
			} else if (key.equals(LINK_COLUMN_NODE_LINK_ID)) {
				return map.getNodeLinks();
			} else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID)) {
				return map.getPhysicalLinks();
			} else if (key.equals(LINK_COLUMN_SITE_NODE_ID)) {
				return map.getSiteNodes();
			} else if (key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID)) {
				return map.getTopologicalNodes();
			} else if (key.equals(LINK_COLUMN_MAP_ID)) {
				return map.getMaps();
			} else if (key.equals(LINK_COLUMN_EXTERNAL_NODE_ID)) {
				return map.getExternalNodes();
			} else if (key.equals(LINK_COLUMN_MAP_LIBRARY_ID)) {
				return map.getMapLibraries();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public void setValue(final Map map, final String key, final Object value) {
		if (map != null) {
			if (key.equals(COLUMN_NAME)) {
				map.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				map.setDescription((String) value);
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				map.setDomainId((Identifier) value);
			} else if (key.equals(LINK_COLUMN_COLLECTOR_ID)) {
				map.setCollectors((Set) value);
			} else if (key.equals(LINK_COLUMN_MARK_ID)) {
				map.setMarks((Set) value);
			} else if (key.equals(LINK_COLUMN_NODE_LINK_ID)) {
				map.setNodeLinks((Set) value);
			} else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID)) {
				map.setPhysicalLinks((Set) value);
			} else if (key.equals(LINK_COLUMN_SITE_NODE_ID)) {
				map.setSiteNodes((Set) value);
			} else if (key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID)) {
				map.setTopologicalNodes((Set) value);
			} else if (key.equals(LINK_COLUMN_MAP_ID)) {
				map.setMaps((Set) value);
			} else if (key.equals(LINK_COLUMN_EXTERNAL_NODE_ID)) {
				map.setExternalNodes((Set) value);
			} else if (key.equals(LINK_COLUMN_MAP_LIBRARY_ID)) {
				map.setMapLibraries((Set) value);
			}
		}
	}

}
