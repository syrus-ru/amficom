/*-
 * $Id: SchemeDeviceWrapper.java,v 1.6 2005/08/05 11:20:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/05 11:20:03 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemeDeviceWrapper extends StorableObjectWrapper<SchemeDevice> {
	
//	schemedevice.sql
//	
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR),
	
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";

	private static SchemeDeviceWrapper instance;

	public List<String> getKeys() {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	@Override
	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	@Override
	public Object getValue(SchemeDevice object, String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	@Override
	public void setValue(SchemeDevice object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public static SchemeDeviceWrapper getInstance() {
		if (instance == null)
			instance = new SchemeDeviceWrapper();
		return instance;
	}
}
