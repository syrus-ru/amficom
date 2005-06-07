/*-
 * $Id: SchemeCablePortWrapper.java,v 1.4 2005/06/07 16:32:58 bass Exp $
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
public final class SchemeCablePortWrapper extends StorableObjectWrapper {
	
//	schemecableport.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	direction_type NUMBER(1) NOT NULL,
//	cable_port_type_id VARCHAR2(32 CHAR),
//	cable_port_id VARCHAR2(32 CHAR),
//	measurement_port_id VARCHAR2(32 CHAR),
//	parent_device_id VARCHAR2(32 CHAR) NOT NULL,
	
	public static final String COLUMN_DIRECTION_TYPE = "direction_type";
	public static final String COLUMN_CABLE_PORT_TYPE_ID = "cable_port_type_id";
	public static final String COLUMN_CABLE_PORT_ID = "cable_port_id";
	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	public static final String COLUMN_PARENT_DEVICE_ID = "parent_device_id";

	private static SchemeCablePortWrapper instance;

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeCablePortWrapper | not implemented yet");
	}

	public static SchemeCablePortWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCablePortWrapper();
		return instance;
	}
}
