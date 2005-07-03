/*-
 * $Id: SchemeCableLinkWrapper.java,v 1.4 2005/06/07 16:32:58 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/07 16:32:58 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeCableLinkWrapper extends StorableObjectWrapper {

//	schemecablelink.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	physical_length BINARY_DOUBLE NOT NULL,
//	optical_length BINARY_DOUBLE NOT NULL,
//	cable_link_type_id VARCHAR2(32 CHAR),
//	cable_link_id VARCHAR2(32 CHAR),
//	source_scheme_cable_port_id VARCHAR2(32 CHAR),
//	target_scheme_cable_port_id VARCHAR2(32 CHAR),
//	parent_scheme_id VARCHAR2(32 CHAR) NOT NULL,

	public static final String COLUMN_PHYSICAL_LENGTH = "physical_length";
	public static final String COLUMN_OPTICAL_LENGTH = "optical_length";
	public static final String COLUMN_CABLE_LINK_TYPE_ID = "cable_link_type_id";
	public static final String COLUMN_CABLE_LINK_ID = "cable_link_id";
	public static final String COLUMN_SOURCE_SCHEME_CABLE_PORT_ID = "source_scheme_cable_port_id";
	public static final String COLUMN_TARGET_SCHEME_CABLE_PORT_ID = "target_scheme_cable_port_id";
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";

	private static SchemeCableLinkWrapper instance;

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeCableLinkWrapper | not implemented yet");
	}

	public static SchemeCableLinkWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCableLinkWrapper();
		return instance;
	}
}
