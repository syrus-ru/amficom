/*-
 * $Id: SchemeDeviceWrapper.java,v 1.1 2005/04/22 16:23:00 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/22 16:23:00 $
 * @author $Author: max $
 * @module scheme_v1
 */
public class SchemeDeviceWrapper extends StorableObjectWrapper {
	
//	schemedevice.sql
//	
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR),
	
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";
	
	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeDeviceWrapper | not implemented yet");
	}
}
