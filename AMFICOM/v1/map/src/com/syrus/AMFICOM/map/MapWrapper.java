/*
 * $Id: MapWrapper.java,v 1.6 2005/03/04 14:25:49 krupenn Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.6 $, $Date: 2005/03/04 14:25:49 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class MapWrapper implements StorableObjectWrapper {

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// domain_id VARCHAR2(32),
	public static final String	COLUMN_DOMAIN_ID				= "domain_id";
	// map_id VARCHAR2(32),
	public static final String	LINK_COLUMN_MAP_ID				= "map_id";
	// collector_id VARCHAR2(32),
	public static final String	LINK_COLUMN_COLLECTOR_ID		= "collector_id";
	// mark_id VARCHAR2(32),
	public static final String	LINK_COLUMN_MARK_ID				= "mark_id";
	// node_link_id VARCHAR2(32),
	public static final String	LINK_COLUMN_NODE_LINK_ID		= "node_link_id";
	// physical_link_id VARCHAR2(32),
	public static final String	LINK_COLUMN_PHYSICAL_LINK_ID	= "physical_link_id";
	// site_node_id VARCHAR2(32),
	public static final String	LINK_COLUMN_SITE_NODE_ID		= "site_node_id";
	// topological_node_id VARCHAR2(32),
	public static final String	LINK_COLUMN_TOPOLOGICAL_NODE_ID	= "topological_node_id";

	protected static MapWrapper	instance;

	protected List				keys;

	private MapWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DOMAIN_ID,
				LINK_COLUMN_COLLECTOR_ID, LINK_COLUMN_MARK_ID, LINK_COLUMN_NODE_LINK_ID, LINK_COLUMN_PHYSICAL_LINK_ID,
				LINK_COLUMN_SITE_NODE_ID, LINK_COLUMN_TOPOLOGICAL_NODE_ID, LINK_COLUMN_MAP_ID };

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static MapWrapper getInstance() {
		if (instance == null)
			instance = new MapWrapper();
		return instance;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(LINK_COLUMN_COLLECTOR_ID) || key.equals(LINK_COLUMN_MARK_ID)
				|| key.equals(LINK_COLUMN_NODE_LINK_ID) || key.equals(LINK_COLUMN_PHYSICAL_LINK_ID)
				|| key.equals(LINK_COLUMN_SITE_NODE_ID) || key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof Map) {
			Map map = (Map) object;
			if (key.equals(COLUMN_NAME))
				return map.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return map.getDescription();
			else if (key.equals(COLUMN_DOMAIN_ID))
				return map.getDomainId();
			else if (key.equals(LINK_COLUMN_COLLECTOR_ID))
				return map.getCollectors();
			else if (key.equals(LINK_COLUMN_MARK_ID))
				return map.getMarks();
			else if (key.equals(LINK_COLUMN_NODE_LINK_ID))
				return map.getNodeLinks();
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				return map.getPhysicalLinks();
			else if (key.equals(LINK_COLUMN_SITE_NODE_ID))
				return map.getSiteNodes();
			else if (key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID))
				return map.getTopologicalNodes();
			else if (key.equals(LINK_COLUMN_MAP_ID))
				return map.getMaps();

		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Map) {
			Map map = (Map) object;
			if (key.equals(COLUMN_NAME))
				map.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				map.setDescription((String) value);
			else if (key.equals(COLUMN_DOMAIN_ID))
				map.setDomainId((Identifier) value);
			else if (key.equals(LINK_COLUMN_COLLECTOR_ID))
				map.setCollectors((List) value);
			else if (key.equals(LINK_COLUMN_MARK_ID))
				map.setMarks((List) value);
			else if (key.equals(LINK_COLUMN_NODE_LINK_ID))
				map.setNodeLinks((List) value);
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				map.setPhysicalLinks((List) value);
			else if (key.equals(LINK_COLUMN_SITE_NODE_ID))
				map.setSiteNodes((List) value);
			else if (key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID))
				map.setTopologicalNodes((List) value);
			else if (key.equals(LINK_COLUMN_MAP_ID))
				map.setMaps((List) value);
		}
	}

}
