/*
 * $Id: PhysicalLinkWrapper.java,v 1.10 2005/08/08 11:35:11 arseniy Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/08/08 11:35:11 $
 * @author $Author: arseniy $
 * @module map
 */
public class PhysicalLinkWrapper extends StorableObjectWrapper<PhysicalLink> {

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// physical_link_type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_PHYSICAL_LINK_TYPE_ID = "physical_link_type_id";
	// city VARCHAR2(128),
	public static final String COLUMN_CITY = "city";
	// street VARCHAR2(128),
	public static final String COLUMN_STREET = "street";
	// building VARCHAR2(128),
	public static final String COLUMN_BUILDING = "building";
	// dimension_x NUMBER(12),
	public static final String COLUMN_DIMENSION_X = "dimension_x";
	// dimension_y NUMBER(12),
	public static final String COLUMN_DIMENSION_Y = "dimension_y";
	// topLeft NUMBER(1),
	public static final String COLUMN_TOPLEFT = "topLeft";
	// start_node_id VARCHAR2(32),
	public static final String COLUMN_START_NODE_ID = "start_node_id";
	// end_node_id VARCHAR2(32),
	public static final String COLUMN_END_NODE_ID = "end_node_id";

	protected static PhysicalLinkWrapper instance;

	protected List<String> keys;

	private PhysicalLinkWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_PHYSICAL_LINK_TYPE_ID,
				COLUMN_CITY,
				COLUMN_STREET,
				COLUMN_BUILDING,
				COLUMN_DIMENSION_X,
				COLUMN_DIMENSION_Y,
				COLUMN_TOPLEFT,
				COLUMN_START_NODE_ID,
				COLUMN_END_NODE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static PhysicalLinkWrapper getInstance() {
		if (instance == null)
			instance = new PhysicalLinkWrapper();
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
	public Class getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final PhysicalLink physicalLink, final String key) {
		if (physicalLink != null) {
			if (key.equals(COLUMN_NAME))
				return physicalLink.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return physicalLink.getDescription();
			else if (key.equals(COLUMN_PHYSICAL_LINK_TYPE_ID))
				return physicalLink.getType();
			else if (key.equals(COLUMN_CITY))
				return physicalLink.getCity();
			else if (key.equals(COLUMN_STREET))
				return physicalLink.getStreet();
			else if (key.equals(COLUMN_BUILDING))
				return physicalLink.getBuilding();
			else if (key.equals(COLUMN_START_NODE_ID))
				return physicalLink.getStartNode();
			else if (key.equals(COLUMN_END_NODE_ID))
				return physicalLink.getEndNode();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	public void setValue(final PhysicalLink physicalLink, final String key, final Object value) {
		if (physicalLink != null) {
			if (key.equals(COLUMN_NAME))
				physicalLink.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				physicalLink.setDescription((String) value);
			else if (key.equals(COLUMN_PHYSICAL_LINK_TYPE_ID))
				physicalLink.setPhysicalLinkType((PhysicalLinkType) value);
			else if (key.equals(COLUMN_CITY))
				physicalLink.setCity((String) value);
			else if (key.equals(COLUMN_STREET))
				physicalLink.setStreet((String) value);
			else if (key.equals(COLUMN_BUILDING))
				physicalLink.setBuilding((String) value);
			else if (key.equals(COLUMN_START_NODE_ID))
				physicalLink.setStartNode((AbstractNode) value);
			else if (key.equals(COLUMN_END_NODE_ID))
				physicalLink.setEndNode((AbstractNode) value);
		}
	}

}
