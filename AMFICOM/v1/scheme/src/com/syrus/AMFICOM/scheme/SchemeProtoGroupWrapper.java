/*-
 * $Id: SchemeProtoGroupWrapper.java,v 1.5 2005/07/03 19:16:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/03 19:16:20 $
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

	private List<String> keys;

	private SchemeProtoGroupWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	@Override
	public Object getValue(final Object object, final String key) {
		throw new UnsupportedOperationException("SchemeProtoGroupWrapper | not implemented yet");
	}

	public boolean isEditable(final String key) {
		return false;
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
