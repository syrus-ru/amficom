/*-
 * $Id: SchemeWrapper.java,v 1.4 2005/06/15 12:20:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/15 12:20:41 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeWrapper extends StorableObjectWrapper {

//  scheme.sql
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	label VARCHAR2(64 CHAR),
//	width NUMBER(10) DEFAULT 840 NOT NULL,
//	height NUMBER(10) DEFAULT 1190 NOT NULL,
//	domain_id VARCHAR2(32 CHAR) NOT NULL,
//	map_id VARCHAR2(32 CHAR),
//	symbol_id VARCHAR2(32 CHAR),
//	ugo_cell_id VARCHAR2(32 CHAR),
//	scheme_cell_id VARCHAR2(32 CHAR),
//	sort NUMBER(1) NOT NULL,
//	parent_scheme_element_id VARCHAR2(32 CHAR),

	public static final String COLUMN_LABEL = "label";
	public static final int	SIZE_LABEL_COLUMN	= 64;
	public static final String COLUMN_WIDTH = "width";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_DOMAIN_ID = "domain_id";
	public static final String COLUMN_MAP_ID = "map_id";
	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_UGO_CELL_ID = "ugo_cell_id";
	public static final String COLUMN_SCHEME_CELL_ID = "scheme_cell_id";
	public static final String COLUMN_KIND = "kind";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";

	private static SchemeWrapper instance;

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeWrapper | not implemented yet");
	}

	public static SchemeWrapper getInstance() {
		if (instance == null)
			instance = new SchemeWrapper();
		return instance;
	}
}
