/*-
 * $Id: SchemeProtoGroupWrapper.java,v 1.9 2005/07/28 17:42:35 bass Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/07/28 17:42:35 $
 * @author $Author: bass $
 * @module scheme
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

	// @PMD:REVIEWED:BeanMembersShouldSerialize: by bass on 28.07.05 20:19
	// @PMD:REVIEWED:BeanMembersShouldSerialize: by bass on 28.07.05 20:19
	private final List<String> keys;

	private SchemeProtoGroupWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_SYMBOL_ID,
				COLUMN_PARENT_SCHEME_PROTO_GROUP_ID}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_SYMBOL_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)) {
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
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value != null) {
			return value;
		}
		if (object instanceof SchemeProtoGroup) {
			final SchemeProtoGroup schemeProtoGroup = (SchemeProtoGroup) object;
			if (key.equals(COLUMN_NAME)) {
				return schemeProtoGroup.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeProtoGroup.getDescription();
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				return schemeProtoGroup.getSymbolId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)) {
				return schemeProtoGroup.getParentSchemeProtoGroupId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof SchemeProtoGroup) {
			final SchemeProtoGroup schemeProtoGroup = (SchemeProtoGroup) object;
			if (key.equals(COLUMN_NAME)) {
				schemeProtoGroup.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				schemeProtoGroup.setDescription((String) value);
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				schemeProtoGroup.setSymbolId((Identifier) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_GROUP_ID)) {
				schemeProtoGroup.setParentSchemeProtoGroupId((Identifier) value);
			}
		}
	}

	public static SchemeProtoGroupWrapper getInstance() {
		if (instance == null)
			instance = new SchemeProtoGroupWrapper();
		return instance;
	}
}
