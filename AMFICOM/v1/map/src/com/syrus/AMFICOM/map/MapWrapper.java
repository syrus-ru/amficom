/*
 * $Id: MapWrapper.java,v 1.2 2005/01/27 06:23:59 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/27 06:23:59 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MapWrapper implements Wrapper {

	// name VARCHAR2(128),
	public static final String	COLUMN_NAME						= "name";
	// description VARCHAR2(256),
	public static final String	COLUMN_DESCRIPTION				= "description";
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
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DOMAIN_ID,
				LINK_COLUMN_COLLECTOR_ID, LINK_COLUMN_MARK_ID, LINK_COLUMN_NODE_LINK_ID, LINK_COLUMN_PHYSICAL_LINK_ID,
				LINK_COLUMN_SITE_NODE_ID, LINK_COLUMN_TOPOLOGICAL_NODE_ID};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return map.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(map.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(map.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return map.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return map.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				return map.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return map.getDescription();
			else if (key.equals(COLUMN_DOMAIN_ID))
				return map.getDomainId().getIdentifierString();
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

		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	private List castStringListToId(List idStr) {
		List ids = new ArrayList(idStr.size());
		for (Iterator it = idStr.iterator(); it.hasNext();)
			ids.add(new Identifier((String) it.next()));
		return ids;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Map) {
			Map map = (Map) object;
			if (key.equals(COLUMN_NAME))
				map.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				map.setDescription((String) value);
			else if (key.equals(COLUMN_DOMAIN_ID))
				map.setDomainId(new Identifier((String) value));
			else if (key.equals(LINK_COLUMN_COLLECTOR_ID))
				try {
					map
							.setCollectors(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value),
								true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}

			else if (key.equals(LINK_COLUMN_MARK_ID))
				try {
					map.setMarks(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(LINK_COLUMN_NODE_LINK_ID))
				try {
					map.setNodeLinks(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				try {
					map.setPhysicalLinks(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value),
						true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(LINK_COLUMN_SITE_NODE_ID))
				try {
					map.setSiteNodes(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(LINK_COLUMN_TOPOLOGICAL_NODE_ID))
				try {
					map.setTopologicalNodes(MapStorableObjectPool.getStorableObjects(castStringListToId((List) value),
						true));
				} catch (DatabaseException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("MapWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
		}
	}

}
