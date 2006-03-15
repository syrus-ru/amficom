/*-
 * $Id: PathElementWrapper.java,v 1.15 2006/03/15 15:49:10 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2006/03/15 15:49:10 $
 * @author $Author: arseniy $
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
	
	private final List<String> keys;
	
	private PathElementWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_KIND,
				COLUMN_SEQUENTIAL_NUMBER,
				COLUMN_START_ABSTRACT_SCHEME_PORT_ID,
				COLUMN_END_ABSTRACT_SCHEME_PORT_ID,
				COLUMN_SCHEME_CABLE_THREAD_ID,
				COLUMN_SCHEME_LINK_ID,
				COLUMN_PARENT_SCHEME_PATH_ID,}));
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
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_KIND) 
				|| key.equals(COLUMN_SEQUENTIAL_NUMBER)) {
			return Integer.class;
		} else if (key.equals(COLUMN_START_ABSTRACT_SCHEME_PORT_ID)
				|| key.equals(COLUMN_END_ABSTRACT_SCHEME_PORT_ID)
				|| key.equals(COLUMN_SCHEME_CABLE_THREAD_ID)
				|| key.equals(COLUMN_SCHEME_LINK_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_PATH_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		//There is no property
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		//There is no property
	}

	@Override
	public Object getValue(final PathElement pathElement, final String key) {
		final Object value = super.getValue(pathElement, key);
		if (value != null) {
			return value;
		}
		if (pathElement != null) {
			if (key.equals(COLUMN_KIND)) {
				return Integer.valueOf(pathElement.getKind().value());
			} else if (key.equals(COLUMN_SEQUENTIAL_NUMBER)) {
				return Integer.valueOf(pathElement.getSequentialNumber());
			} else if (key.equals(COLUMN_START_ABSTRACT_SCHEME_PORT_ID)) {
				return pathElement.getStartAbstractSchemePortId();
			} else if (key.equals(COLUMN_END_ABSTRACT_SCHEME_PORT_ID)) {
				return pathElement.getEndAbstractSchemePortId();
			} else if (key.equals(COLUMN_SCHEME_CABLE_THREAD_ID)) {
				return pathElement.getSchemeCableThreadId();
			} else if (key.equals(COLUMN_SCHEME_LINK_ID)) {
				return pathElement.getSchemeLinkId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PATH_ID)) {
				return pathElement.getParentSchemePathId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final PathElement pathElement, final String key,
			final Object value)
	throws PropertyChangeException {
		try {
			if (pathElement != null) {
				if (key.equals(COLUMN_START_ABSTRACT_SCHEME_PORT_ID)) {
					pathElement.setStartAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_END_ABSTRACT_SCHEME_PORT_ID)) {
					pathElement.setEndAbstractSchemePortId((Identifier) value);
				} else if (key.equals(COLUMN_SCHEME_CABLE_THREAD_ID)) {
					pathElement.setSchemeCableThreadId((Identifier) value);
				} else if (key.equals(COLUMN_SCHEME_LINK_ID)) {
					pathElement.setSchemeLinkId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_PATH_ID)) {
					pathElement.setParentSchemePathId((Identifier) value);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static PathElementWrapper getInstance() {
		if (instance == null) {
			instance = new PathElementWrapper();
		}
		return instance;
	}
}
