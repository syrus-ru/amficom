/*
 * $Id: NodeLinkWrapper.java,v 1.8 2005/07/17 05:20:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/17 05:20:43 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class NodeLinkWrapper extends StorableObjectWrapper {

	// name VARCHAR2(128),
	// physical_link_id VARCHAR2(32),
	public static final String			COLUMN_PHYSICAL_LINK_ID		= "physical_link_id";
	// start_node_id VARCHAR2(32),
	public static final String			COLUMN_START_NODE_ID		= "start_node_id";
	// end_node_id VARCHAR2(32),
	public static final String			COLUMN_END_NODE_ID			= "end_node_id";
	// length NUMBER(12,6),
	public static final String			COLUMN_LENGTH				= "length";

	protected static NodeLinkWrapper	instance;

	protected List						keys;

	private NodeLinkWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_PHYSICAL_LINK_ID,
				COLUMN_START_NODE_ID,
				COLUMN_END_NODE_ID,
				COLUMN_LENGTH };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static NodeLinkWrapper getInstance() {
		if (instance == null)
			instance = new NodeLinkWrapper();
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
		if (object instanceof NodeLink) {
			NodeLink nodeLink = (NodeLink) object;
			if (key.equals(COLUMN_NAME))
				return nodeLink.getName();
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				return nodeLink.getPhysicalLink();
			else if (key.equals(COLUMN_START_NODE_ID))
				return nodeLink.getStartNode();
			else if (key.equals(COLUMN_END_NODE_ID))
				return nodeLink.getEndNode();
			else if (key.equals(COLUMN_LENGTH))
				return new Double(nodeLink.getLength());
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
		if (object instanceof NodeLink) {
			NodeLink nodeLink = (NodeLink) object;
			if (key.equals(COLUMN_NAME))
				nodeLink.setName((String) value);
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				nodeLink.setPhysicalLink((PhysicalLink) value);
			else if (key.equals(COLUMN_START_NODE_ID))
				nodeLink.setStartNode((AbstractNode) value);
			else if (key.equals(COLUMN_END_NODE_ID))
				nodeLink.setEndNode((AbstractNode) value);
			else if (key.equals(COLUMN_LENGTH))
				nodeLink.setLength(((Double) value).doubleValue());
		}
	}
}
