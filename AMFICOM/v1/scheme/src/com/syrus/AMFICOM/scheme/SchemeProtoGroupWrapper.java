/*-
 * $Id: SchemeProtoGroupWrapper.java,v 1.3 2005/05/24 13:58:41 bass Exp $
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
public final class SchemeProtoGroupWrapper extends StorableObjectWrapper {
	
//	schemeprotogroup.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	symbol_id VARCHAR2(32 CHAR),
//	parent_scheme_proto_group_id VARCHAR2(32 CHAR),

	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_PARENT_SCHEME_PROTO_GROUP_ID  = "parent_scheme_proto_group_id";

	private static SchemeProtoGroupWrapper instance;

	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public static SchemeProtoGroupWrapper getInstance() {
		if (instance == null)
			instance = new SchemeProtoGroupWrapper();
		return instance;
	}
}
