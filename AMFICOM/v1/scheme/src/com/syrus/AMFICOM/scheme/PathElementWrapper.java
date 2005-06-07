/*-
 * $Id: PathElementWrapper.java,v 1.4 2005/06/07 16:32:58 bass Exp $
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
public final class PathElementWrapper extends StorableObjectWrapper {
	
//	pathelement.sql
//	
//	parent_scheme_path_id VARCHAR2(32 CHAR),
//	sequential_number NUMBER(10),
//	kind NUMBER(1); NOT NULL,
//	start_abstract_scheme_port_id VARCHAR(32 CHAR),
//        end_abstract_scheme_port_id VARCHAR(32 CHAR),
//	scheme_cable_thread_id VARCHAR(32 CHAR),
//	scheme_link_id VARCHAR(32 CHAR),
	
	public static final String COLUMN_PARENT_SCHEME_PATH_ID = "parent_scheme_path_id";
	public static final String COLUMN_SEQUENTIAL_NUMBER = "sequential_number";
	public static final String COLUMN_KIND = "kind";
	public static final String COLUMN_START_ABSTRACT_SCHEME_PORT_ID = "start_abstract_scheme_port_id";
	public static final String COLUMN_END_ABSTRACT_SCHEME_PORT_ID = "end_abstract_scheme_port_id";
	public static final String COLUMN_SCHEME_CABLE_THREAD_ID = "scheme_cable_thread_id";
	public static final String COLUMN_SCHEME_LINK_ID = "scheme_link_id";

	private static PathElementWrapper instance;

	public List getKeys() {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public static PathElementWrapper getInstance() {
		if (instance == null)
			instance = new PathElementWrapper();
		return instance;
	}
}
