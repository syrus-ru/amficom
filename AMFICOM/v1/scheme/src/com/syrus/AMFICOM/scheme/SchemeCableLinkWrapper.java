/*-
 * $Id: SchemeCableLinkWrapper.java,v 1.12 2006/03/15 15:49:10 arseniy Exp $
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
 * @version $Revision: 1.12 $, $Date: 2006/03/15 15:49:10 $
 * @author $Author: arseniy $
 * @module scheme
 */
public final class SchemeCableLinkWrapper extends StorableObjectWrapper<SchemeCableLink> {

//	schemecablelink.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	physical_length BINARY_DOUBLE NOT NULL,
//	optical_length BINARY_DOUBLE NOT NULL,
//	cable_link_type_id VARCHAR2(32 CHAR),
//	cable_link_id VARCHAR2(32 CHAR),
//	source_scheme_cable_port_id VARCHAR2(32 CHAR),
//	target_scheme_cable_port_id VARCHAR2(32 CHAR),
//	parent_scheme_id VARCHAR2(32 CHAR) NOT NULL,

	public static final String COLUMN_PHYSICAL_LENGTH = "physical_length";
	public static final String COLUMN_OPTICAL_LENGTH = "optical_length";
	public static final String COLUMN_CABLE_LINK_TYPE_ID = "cable_link_type_id";
	public static final String COLUMN_CABLE_LINK_ID = "cable_link_id";
	public static final String COLUMN_SOURCE_SCHEME_CABLE_PORT_ID = "source_scheme_cable_port_id";
	public static final String COLUMN_TARGET_SCHEME_CABLE_PORT_ID = "target_scheme_cable_port_id";
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";

	private static SchemeCableLinkWrapper instance;

	private final List<String> keys;
	
	private SchemeCableLinkWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_PHYSICAL_LENGTH,
				COLUMN_OPTICAL_LENGTH,
				COLUMN_CABLE_LINK_TYPE_ID,
				COLUMN_CABLE_LINK_ID,
				COLUMN_SOURCE_SCHEME_CABLE_PORT_ID,
				COLUMN_TARGET_SCHEME_CABLE_PORT_ID,
				COLUMN_PARENT_SCHEME_ID}));
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
		} else if (key.equals(COLUMN_CABLE_LINK_TYPE_ID)
				|| key.equals(COLUMN_CABLE_LINK_ID)
				|| key.equals(COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)
				|| key.equals(COLUMN_TARGET_SCHEME_CABLE_PORT_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		// There is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// There is no property
	}

	@Override
	public Object getValue(SchemeCableLink schemeCableLink, String key) {
		final Object value = super.getValue(schemeCableLink, key);
		if (value != null) {
			return value;
		}
		if (schemeCableLink != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeCableLink.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeCableLink.getDescription();
			} else if (key.equals(COLUMN_PHYSICAL_LENGTH)) {
				return Double.valueOf(schemeCableLink.getPhysicalLength());
			} else if (key.equals(COLUMN_OPTICAL_LENGTH)) {
				return Double.valueOf(schemeCableLink.getOpticalLength());
			} else if (key.equals(COLUMN_CABLE_LINK_TYPE_ID)) {
				return schemeCableLink.getAbstractLinkTypeId();
			} else if (key.equals(COLUMN_CABLE_LINK_ID)) {
				return schemeCableLink.getAbstractLinkId();
			} else if (key.equals(COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)) {
				return schemeCableLink.getSourceAbstractSchemePortId();
			} else if (key.equals(COLUMN_TARGET_SCHEME_CABLE_PORT_ID)) {
				return schemeCableLink.getTargetAbstractSchemePortId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				return schemeCableLink.getParentSchemeId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeCableLink schemeCableLink,
			final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeCableLink != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeCableLink.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeCableLink.setDescription((String) value);
				} else if (key.equals(COLUMN_PHYSICAL_LENGTH)) {
					schemeCableLink.setPhysicalLength(((Double) value).doubleValue());
				} else if (key.equals(COLUMN_OPTICAL_LENGTH)) {
					schemeCableLink.setOpticalLength(((Double) value).doubleValue());
				} else if (key.equals(COLUMN_CABLE_LINK_TYPE_ID)) {
					schemeCableLink.setAbstractLinkTypeId((Identifier) value);
				} else if (key.equals(COLUMN_CABLE_LINK_ID)) {
					schemeCableLink.setAbstractLinkId((Identifier) value);
				} else if (key.equals(COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)) {
					schemeCableLink.setSourceAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_TARGET_SCHEME_CABLE_PORT_ID)) {
					schemeCableLink.setTargetAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
					schemeCableLink.setParentSchemeId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeCableLinkWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCableLinkWrapper();
		return instance;
	}
}
