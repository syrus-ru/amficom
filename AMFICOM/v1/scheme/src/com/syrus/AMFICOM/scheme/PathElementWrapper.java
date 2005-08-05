/*-
 * $Id: PathElementWrapper.java,v 1.8 2005/08/05 11:20:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/05 11:20:03 $
 * @author $Author: bass $
 * @module scheme
 */
public final class PathElementWrapper extends StorableObjectWrapper<PathElement> {
	
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

	public List<String> getKeys() {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public String getName(final String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	@Override
	public Class getPropertyClass(final String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public Object getPropertyValue(final String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	@Override
	public Object getValue(final PathElement object, final String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public boolean isEditable(final String key) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	@Override
	public void setValue(final PathElement object, final String key, final Object value) {
		throw new UnsupportedOperationException("PathElementWrapper | not implemented yet");
	}

	public static PathElementWrapper getInstance() {
		if (instance == null) {
			instance = new PathElementWrapper();
		}
		return instance;
	}
}
