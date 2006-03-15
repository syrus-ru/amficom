/*-
 * $Id: SchemeLinkWrapper.java,v 1.15 2006/03/13 15:54:26 bass Exp $
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
 * @version $Revision: 1.15 $, $Date: 2006/03/13 15:54:26 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemeLinkWrapper extends StorableObjectWrapper<SchemeLink> {
	
//	schemelink.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	physical_length BINARY_DOUBLE NOT NULL,
//	optical_length BINARY_DOUBLE NOT NULL,
//--
//	link_type_id VARCHAR2(32 CHAR),
//	link_id VARCHAR2(32 CHAR),
//--
//	site_node_id VARCHAR2(32 CHAR),
//	source_scheme_port_id VARCHAR2(32 CHAR),
//	target_scheme_port_id VARCHAR2(32 CHAR),
//--
//	parent_scheme_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR),
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),

	public static final String COLUMN_PHYSICAL_LENGTH = "physical_length";
	public static final String COLUMN_OPTICAL_LENGTH = "optical_length";
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";
	public static final String COLUMN_LINK_ID = "link_id";
	public static final String COLUMN_SITE_NODE_ID = "site_node_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";

	private static SchemeLinkWrapper instance;
	
	private final List<String> keys;
	
	private SchemeLinkWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_PHYSICAL_LENGTH,
				COLUMN_OPTICAL_LENGTH,
				COLUMN_LINK_TYPE_ID,
				COLUMN_LINK_ID,
				COLUMN_SITE_NODE_ID,
				COLUMN_SOURCE_SCHEME_PORT_ID,
				COLUMN_TARGET_SCHEME_PORT_ID,
				COLUMN_PARENT_SCHEME_ID,
				COLUMN_PARENT_SCHEME_ELEMENT_ID,
				COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_PHYSICAL_LENGTH)
				|| key.equals(COLUMN_OPTICAL_LENGTH)) {
			return Double.class;
		} else if (key.equals(COLUMN_LINK_TYPE_ID)
				|| key.equals(COLUMN_LINK_ID)
				|| key.equals(COLUMN_SITE_NODE_ID)
				|| key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)
				|| key.equals(COLUMN_TARGET_SCHEME_PORT_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
//		there is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
//		there is no property
	}

	@Override
	public Object getValue(SchemeLink schemeLink, String key) {
		final Object value = super.getValue(schemeLink, key);
		if (value != null) {
			return value;
		}
		if (schemeLink != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeLink.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeLink.getDescription();
			} else if (key.equals(COLUMN_PHYSICAL_LENGTH)) {
				return Double.valueOf(schemeLink.getPhysicalLength());
			} else if (key.equals(COLUMN_OPTICAL_LENGTH)) {
				return Double.valueOf(schemeLink.getOpticalLength());
			} else if (key.equals(COLUMN_LINK_TYPE_ID)) {
				return schemeLink.getAbstractLinkTypeId();
			} else if (key.equals(COLUMN_LINK_ID)) {
				return schemeLink.getAbstractLinkId();
			} else if (key.equals(COLUMN_SITE_NODE_ID)) {
				return schemeLink.getSiteNodeId();
			} else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)) {
				return schemeLink.getSourceAbstractSchemePortId();
			} else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID)) {
				return schemeLink.getTargetAbstractSchemePortId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				return schemeLink.getParentSchemeId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
				return schemeLink.getParentSchemeElementId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
				return schemeLink.getParentSchemeProtoElementId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeLink schemeLink,
			final String key,
			final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeLink != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeLink.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeLink.setDescription((String) value);
				} else if (key.equals(COLUMN_PHYSICAL_LENGTH)) {
					schemeLink.setPhysicalLength(((Double) value).doubleValue());
				} else if (key.equals(COLUMN_OPTICAL_LENGTH)) {
					schemeLink.setOpticalLength(((Double) value).doubleValue());
				} else if (key.equals(COLUMN_LINK_TYPE_ID)) {
					schemeLink.setAbstractLinkTypeId((Identifier) value);
				} else if (key.equals(COLUMN_LINK_ID)) {
					schemeLink.setAbstractLinkId((Identifier) value);
				} else if (key.equals(COLUMN_SITE_NODE_ID)) {
					schemeLink.setSiteNodeId((Identifier) value);
				} else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)) {
					schemeLink.setSourceAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID)) {
					schemeLink.setTargetAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
					schemeLink.setParentSchemeId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
					schemeLink.setParentSchemeElementId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
					schemeLink.setParentSchemeProtoElementId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeLinkWrapper getInstance() {
		if (instance == null)
			instance = new SchemeLinkWrapper();
		return instance;
	}
}
