/*-
 * $Id: SchemeLinkWrapper.java,v 1.3 2005/05/24 13:58:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/24 13:58:41 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeLinkWrapper extends StorableObjectWrapper {
	
//	schemelink.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	physical_length BINARY_DOUBLE NOT NULL,
//	optical_length BINARY_DOUBLE NOT NULL,
//--
//	link_type_id VARCHAR2(32 CHAR),
//	link_id VARCHAR2(32 CHAR),
//--
//	site_node_id VARCHAR2(32 CHAR),
//	source_scheme_port_id VARCHAR2(32 CHAR),
//	target_scheme_port_id VARCHAR2(32 CHAR),
//--
//	parent_scheme_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR),
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),

	public static final String COLUMN_PHYSICAL_LENGTH = "physical_length";
	public static final String COLUMN_OPTICAL_LENGTH = "optical_length";
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";
	public static final String COLUMN_LINK_ID = "link_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";

	private static SchemeLinkWrapper instance;

	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeLinkWrapper | not implemented yet");
	}

	public static SchemeLinkWrapper getInstance() {
		if (instance == null)
			instance = new SchemeLinkWrapper();
		return instance;
	}
}
