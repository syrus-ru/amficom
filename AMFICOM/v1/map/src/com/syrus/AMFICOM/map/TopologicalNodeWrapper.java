/*
 * $Id: TopologicalNodeWrapper.java,v 1.10 2005/07/17 05:20:44 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/07/17 05:20:44 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class TopologicalNodeWrapper extends StorableObjectWrapper {

	public static final String				COLUMN_PHYSICAL_LINK_ID		= "physical_link_id";
	public static final String				COLUMN_X					= "x";
	public static final String				COLUMN_Y					= "y";

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// longitude NUMBER(12,6),
	public static final String				COLUMN_LONGITUDE			= "longitude";
	// latiude NUMBER(12,6),
	public static final String				COLUMN_LATIUDE				= "latiude";
	// active NUMBER(1),
	public static final String				COLUMN_ACTIVE				= "active";

	protected static TopologicalNodeWrapper	instance;

	protected List							keys;

	private TopologicalNodeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_LONGITUDE, COLUMN_LATIUDE, COLUMN_ACTIVE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

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

	@Override
	public Class getPropertyClass(String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	@Override
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
			else if (key.equals(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID)) {
				return topologicalNode.getPhysicalLink();
			}
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
		}
	}

}
