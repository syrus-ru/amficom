/*
 * $Id: TopologicalNodeWrapper.java,v 1.3 2005/02/01 07:25:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/01 07:25:22 $
 * @author $Author: bob $
 * @module map_v1
 */
public class TopologicalNodeWrapper implements Wrapper {

	public static final String				COLUMN_PHYSICAL_LINK_ID		= "physical_link_id";
	public static final String				COLUMN_X					= "x";
	public static final String				COLUMN_Y					= "y";

	// name VARCHAR2(128),
	public static final String				COLUMN_NAME					= "name";
	// description VARCHAR2(256),
	public static final String				COLUMN_DESCRIPTION			= "description";
	// longitude NUMBER(12,6),
	public static final String				COLUMN_LONGITUDE			= "longitude";
	// latiude NUMBER(12,6),
	public static final String				COLUMN_LATIUDE				= "latiude";
	// active NUMBER(1),
	public static final String				COLUMN_ACTIVE				= "active";

	public static final String				COLUMN_CHARACTERISTIC_ID	= "collector_id";

	protected static TopologicalNodeWrapper	instance;

	protected List							keys;

	private TopologicalNodeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_LONGITUDE, COLUMN_LATIUDE,
				COLUMN_ACTIVE, NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID, COLUMN_CHARACTERISTIC_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static TopologicalNodeWrapper getInstance() {
		if (instance == null)
			instance = new TopologicalNodeWrapper();
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
		if (key.equals(COLUMN_CHARACTERISTIC_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof TopologicalNode) {
			TopologicalNode topologicalNode = (TopologicalNode) object;
			if (key.equals(COLUMN_NAME))
				return topologicalNode.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return topologicalNode.getDescription();
			else if (key.equals(COLUMN_LONGITUDE))
				return new Double(topologicalNode.getLocation().getX());
			else if (key.equals(COLUMN_LATIUDE))
				return new Double(topologicalNode.getLocation().getY());
			else if (key.equals(COLUMN_ACTIVE))
				return Boolean.valueOf(topologicalNode.isActive());
			else if (key.equals(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID))
				return topologicalNode.getPhysicalLink();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return topologicalNode.getCharacteristics();
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
		if (object instanceof TopologicalNode) {
			TopologicalNode topologicalNode = (TopologicalNode) object;
			if (key.equals(COLUMN_NAME))
				topologicalNode.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				topologicalNode.setDescription((String) value);
			else if (key.equals(COLUMN_LONGITUDE))
				topologicalNode.setLongitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_LATIUDE))
				topologicalNode.setLatitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_ACTIVE))
				topologicalNode.setActive(((Boolean) value).booleanValue());
			else if (key.equals(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID))
				topologicalNode.setPhysicalLink((PhysicalLink) value);
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				topologicalNode.setCharacteristics((List) value);
		}
	}

}
