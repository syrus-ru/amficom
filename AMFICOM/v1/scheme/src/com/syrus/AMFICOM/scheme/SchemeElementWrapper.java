/*-
 * $Id: SchemeElementWrapper.java,v 1.2 2005/04/19 16:05:17 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/19 16:05:17 $
 * @author $Author: max $
 * @module scheme_v1
 */
public class SchemeElementWrapper extends StorableObjectWrapper {

//	schemeelement.sql
//	
//	name VARCHAR2(32 CHAR)NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	label VARCHAR2(64 CHAR),
//	equipment_type_id VARCHAR2(32 CHAR),
//	equipment_id VARCHAR2(32 CHAR),
//	rtu_id VARCHAR2(32 CHAR),
//	site_id VARCHAR2(32 CHAR),
//	symbol_id VARCHAR2(32 CHAR),
//	ugo_cell_id VARCHAR2(32 CHAR),
//	scheme_cell_id VARCHAR2(32 CHAR),
//	parent_scheme_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR)
	
	public static final String COLUMN_LABEL = "label";
	public static final int	SIZE_LABEL_COLUMN	= 64;
	public static final String COLUMN_EQUIPMENT_TYPE_ID = "equipment_type_id";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_KIS_ID = "kis_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_UGO_CELL_ID = "ugo_cell_id";
	public static final String COLUMN_SCHEME_CELL_ID = "scheme_cell_id";
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";

	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeElementWrapper | not implemented yet");
	}
}
