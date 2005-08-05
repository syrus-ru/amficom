/*-
 * $Id: SchemeCableThreadWrapper.java,v 1.6 2005/08/05 11:20:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/05 11:20:03 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemeCableThreadWrapper extends StorableObjectWrapper<SchemeCableThread> {
	public static final String COLUMN_CABLE_THREAD_TYPE_ID = "cable_thread_type_id";
	public static final String COLUMN_LINK_ID = "link_id";
	public static final String COLUMN_PARENT_SCHEME_CABLE_LINK_ID = "parent_scheme_cable_link_id";
	public static final String COLUMN_SOURCE_SCHEME_PORT_ID = "source_scheme_port_id";
	public static final String COLUMN_TARGET_SCHEME_PORT_ID = "target_scheme_port_id";

	private static SchemeCableThreadWrapper instance;

	public List<String> getKeys() {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	@Override
	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	@Override
	public Object getValue(SchemeCableThread object, String key) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	@Override
	public void setValue(SchemeCableThread object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeCableThreadWrapper | not implemented yet");
	}

	public static SchemeCableThreadWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCableThreadWrapper();
		return instance;
	}
}
