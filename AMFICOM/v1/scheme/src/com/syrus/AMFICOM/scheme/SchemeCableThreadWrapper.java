/*-
 * $Id: SchemeCableThreadWrapper.java,v 1.12 2006/03/15 15:49:10 arseniy Exp $
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
public final class SchemeCableThreadWrapper extends StorableObjectWrapper<SchemeCableThread> {
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";
	public static final String COLUMN_LINK_ID = "link_id";
	public static final String COLUMN_PARENT_SCHEME_CABLE_LINK_ID = "parent_scheme_cable_link_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";

	private static SchemeCableThreadWrapper instance;
	
	private final List<String> keys;
	
	private SchemeCableThreadWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_LINK_TYPE_ID,
				COLUMN_LINK_ID,
				COLUMN_PARENT_SCHEME_CABLE_LINK_ID,
				COLUMN_SOURCE_SCHEME_PORT_ID,
				COLUMN_TARGET_SCHEME_PORT_ID}));
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
		} else if (key.equals(COLUMN_LINK_TYPE_ID)
				|| key.equals(COLUMN_LINK_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)
				|| key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)
				|| key.equals(COLUMN_TARGET_SCHEME_PORT_ID)) {
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
	public Object getValue(SchemeCableThread schemeCableThread, String key) {
		final Object value = super.getValue(schemeCableThread, key);
		if (value != null) {
			return value;
		}
		if (schemeCableThread != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeCableThread.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeCableThread.getDescription();
			} else if (key.equals(COLUMN_LINK_TYPE_ID)) {
				return schemeCableThread.getLinkTypeId();
			} else if (key.equals(COLUMN_LINK_ID)) {
				return schemeCableThread.getLinkId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)) {
				return schemeCableThread.getParentSchemeCableLinkId();
			} else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)) {
				return schemeCableThread.getSourceSchemePortId();
			} else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID)) {
				return schemeCableThread.getTargetSchemePortId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeCableThread schemeCableThread,
			final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeCableThread != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeCableThread.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeCableThread.setDescription((String) value);
				} else if (key.equals(COLUMN_LINK_TYPE_ID)) {
					schemeCableThread.setLinkTypeId((Identifier) value);
				} else if (key.equals(COLUMN_LINK_ID)) {
					schemeCableThread.setLinkId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_CABLE_LINK_ID)) {
					schemeCableThread.setParentSchemeCableLinkId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_SOURCE_SCHEME_PORT_ID)) {
					schemeCableThread.setSourceSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_TARGET_SCHEME_PORT_ID)) {
					schemeCableThread.setTargetSchemePortId((Identifier) value);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeCableThreadWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCableThreadWrapper();
		return instance;
	}
}
