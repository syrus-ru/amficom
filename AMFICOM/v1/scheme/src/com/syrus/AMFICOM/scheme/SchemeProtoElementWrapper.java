/*-
 * $Id: SchemeProtoElementWrapper.java,v 1.14.2.1 2006/03/15 15:47:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.14.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @author $Author: arseniy $
 * @module scheme
 */
public final class SchemeProtoElementWrapper extends StorableObjectWrapper<SchemeProtoElement> {

//  SchemeProtoElement.sql
//
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	label VARCHAR2(64 CHAR),
//	proto_equipment_id VARCHAR2(32 CHAR),
//	symbol_id VARCHAR2(32 CHAR),
//	ugo_cell_id VARCHAR2(32 CHAR),
//	scheme_cell_id VARCHAR2(32 CHAR),
//	parent_scheme_proto_group_id VARCHAR2(32 CHAR),
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),

	public static final String COLUMN_LABEL = "label";
	public static final int	SIZE_LABEL_COLUMN	= 64;
	public static final String COLUMN_PROTO_EQUIPMENT_ID = "proto_equipment_id";
	public static final String COLUMN_SYMBOL_ID = "symbol_id";
	public static final String COLUMN_UGO_CELL_ID = "ugo_cell_id";
	public static final String COLUMN_SCHEME_CELL_ID = "scheme_cell_id";
	public static final String COLUMN_PARENT_SCHEME_PROTO_GROUP_ID = "parent_scheme_proto_group_id";
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";

	private static SchemeProtoElementWrapper instance;

	private List<String> keys;

	private SchemeProtoElementWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_LABEL,
				COLUMN_PROTO_EQUIPMENT_ID,
				COLUMN_SYMBOL_ID,
				COLUMN_UGO_CELL_ID,
				COLUMN_SCHEME_CELL_ID,
				COLUMN_PARENT_SCHEME_PROTO_GROUP_ID,
				COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_LABEL)) {
			return String.class;
		} else if (key.equals(COLUMN_PROTO_EQUIPMENT_ID)
				|| key.equals(COLUMN_SYMBOL_ID)
				|| key.equals(COLUMN_UGO_CELL_ID)
				|| key.equals(COLUMN_SCHEME_CELL_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	@Override
	public Object getValue(final SchemeProtoElement schemeProtoElement, final String key) {
		final Object value = super.getValue(schemeProtoElement, key);
		if (value != null) {
			return value;
		}
		if (schemeProtoElement != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeProtoElement.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeProtoElement.getDescription();
			} else if (key.equals(COLUMN_LABEL)) {
				return schemeProtoElement.getLabel();
			} else if (key.equals(COLUMN_PROTO_EQUIPMENT_ID)) {
				return schemeProtoElement.getProtoEquipmentId();
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				return schemeProtoElement.getSymbolId();
			} else if (key.equals(COLUMN_UGO_CELL_ID)) {
				return schemeProtoElement.getUgoCellId();
			} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
				return schemeProtoElement.getSchemeCellId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)) {
				return schemeProtoElement.getParentSchemeProtoGroupId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
				return schemeProtoElement.getParentSchemeProtoElementId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeProtoElement schemeProtoElement,
			final String key,
			final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeProtoElement != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeProtoElement.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeProtoElement.setDescription((String) value);
				} else if (key.equals(COLUMN_LABEL)) {
					schemeProtoElement.setLabel((String) value);
				} else if (key.equals(COLUMN_PROTO_EQUIPMENT_ID)) {
					schemeProtoElement.setProtoEquipmentId((Identifier) value);
				} else if (key.equals(COLUMN_SYMBOL_ID)) {
					schemeProtoElement.setSymbolId((Identifier) value);
				} else if (key.equals(COLUMN_UGO_CELL_ID)) {
					schemeProtoElement.setUgoCellId((Identifier) value);
				} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
					schemeProtoElement.setSchemeCellId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)) {
					schemeProtoElement.setParentSchemeProtoGroupId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
					schemeProtoElement.setParentSchemeProtoElementId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeProtoElementWrapper getInstance() {
		if (instance == null)
			instance = new SchemeProtoElementWrapper();
		return instance;
	}
}
