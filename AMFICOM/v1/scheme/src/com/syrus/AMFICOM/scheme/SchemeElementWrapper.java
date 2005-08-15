/*-
 * $Id: SchemeElementWrapper.java,v 1.10 2005/08/15 15:15:51 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/15 15:15:51 $
 * @author $Author: max $
 * @module scheme
 */
public final class SchemeElementWrapper extends StorableObjectWrapper<SchemeElement> {

//	schemeelement.sql
//	
//	name VARCHAR2(32 CHAR)NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//  kind NUMBER(1),
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
	
	public static final String COLUMN_KIND = "kind";
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

	private static SchemeElementWrapper instance;
	
	private final List<String> keys;
	
	private SchemeElementWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_KIND,
				COLUMN_LABEL,
				COLUMN_EQUIPMENT_TYPE_ID,
				COLUMN_EQUIPMENT_ID,
				COLUMN_KIS_ID,
				COLUMN_SITE_NODE_ID,
				COLUMN_SYMBOL_ID,
				COLUMN_UGO_CELL_ID,
				COLUMN_SCHEME_CELL_ID,
				COLUMN_PARENT_SCHEME_ID,
				COLUMN_PARENT_SCHEME_ELEMENT_ID}));
	}
	

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_LABEL)) {
			return String.class;
		} else if (key.equals(COLUMN_KIND)) {
			return Integer.class;
		} else if (key.equals(COLUMN_EQUIPMENT_TYPE_ID)
				|| key.equals(COLUMN_EQUIPMENT_ID)
				|| key.equals(COLUMN_KIS_ID)
				|| key.equals(COLUMN_SITE_NODE_ID)
				|| key.equals(COLUMN_SYMBOL_ID)
				|| key.equals(COLUMN_UGO_CELL_ID)
				|| key.equals(COLUMN_SCHEME_CELL_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		//there is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//there is no property
	}

	@Override
	public Object getValue(SchemeElement schemeElement, String key) {
		final Object value = super.getValue(schemeElement, key);
		if (value != null) {
			return value;
		}
		if (schemeElement != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeElement.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeElement.getDescription();
			} else if (key.equals(COLUMN_KIND)) {
				return schemeElement.getKind().value();
			} else if (key.equals(COLUMN_LABEL)) {
				return schemeElement.getLabel();
			} else if (key.equals(COLUMN_EQUIPMENT_TYPE_ID)) {
				return schemeElement.getEquipmentTypeId();
			} else if (key.equals(COLUMN_EQUIPMENT_ID)) {
				return schemeElement.getEquipmentId();
			} else if (key.equals(COLUMN_KIS_ID)) {
				return schemeElement.getKisId();
			} else if (key.equals(COLUMN_SITE_NODE_ID)) {
				return schemeElement.getSiteNodeId();
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				return schemeElement.getSymbolId();
			} else if (key.equals(COLUMN_UGO_CELL_ID)) {
				return schemeElement.getUgoCellId();
			} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
				return schemeElement.getSchemeCellId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				return schemeElement.getParentSchemeId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
				return schemeElement.getParentSchemeElementId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(SchemeElement schemeElement, String key, Object value) {
		if (schemeElement != null) {
			if (key.equals(COLUMN_NAME)) {
				schemeElement.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				schemeElement.setDescription((String) value);
			} else if (key.equals(COLUMN_KIND)) {
				//nothing to do 
			} else if (key.equals(COLUMN_LABEL)) {
				schemeElement.setLabel((String) value);
			} else if (key.equals(COLUMN_EQUIPMENT_TYPE_ID)) {
				schemeElement.setEquipmentTypeId((Identifier) value);
			} else if (key.equals(COLUMN_EQUIPMENT_ID)) {
				schemeElement.setEquipmentId((Identifier) value);
			} else if (key.equals(COLUMN_KIS_ID)) {
				schemeElement.setKisId((Identifier) value);
			} else if (key.equals(COLUMN_SITE_NODE_ID)) {
				schemeElement.setSiteNodeId((Identifier) value);
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				schemeElement.setSymbolId((Identifier) value);
			} else if (key.equals(COLUMN_UGO_CELL_ID)) {
				schemeElement.setUgoCellId((Identifier) value);
			} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
				schemeElement.setSchemeCellId((Identifier) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				schemeElement.setParentSchemeId((Identifier) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
				schemeElement.setParentSchemeElementId((Identifier) value);
			}
		}
	}

	public static SchemeElementWrapper getInstance() {
		if (instance == null)
			instance = new SchemeElementWrapper();
		return instance;
	}
}
