/*-
 * $Id: SchemeWrapper.java,v 1.11.2.1 2006/03/15 15:47:49 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.11.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @author $Author: arseniy $
 * @module scheme
 */
public final class SchemeWrapper extends StorableObjectWrapper<Scheme> {
	public static final String COLUMN_LABEL = "label";
	public static final int	SIZE_LABEL_COLUMN = 64;
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

	private final List<String> keys;

	private SchemeWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_LABEL,
				COLUMN_WIDTH,
				COLUMN_HEIGHT,
				COLUMN_DOMAIN_ID,
				COLUMN_MAP_ID,
				COLUMN_SYMBOL_ID,
				COLUMN_UGO_CELL_ID,
				COLUMN_SCHEME_CELL_ID,
				COLUMN_KIND,
				COLUMN_PARENT_SCHEME_ELEMENT_ID}));
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
		} else if (key.equals(COLUMN_WIDTH)
				|| key.equals(COLUMN_HEIGHT)
				|| key.equals(COLUMN_KIND)) {
			return Integer.class;
		} else if (key.equals(COLUMN_DOMAIN_ID)
				|| key.equals(COLUMN_MAP_ID)
				|| key.equals(COLUMN_SYMBOL_ID)
				|| key.equals(COLUMN_UGO_CELL_ID)
				|| key.equals(COLUMN_SCHEME_CELL_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
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
	public Object getValue(final Scheme scheme, final String key) {
		final Object value = super.getValue(scheme, key);
		if (value != null) {
			return value;
		}
		if (scheme != null) {
			if (key.equals(COLUMN_NAME)) {
				return scheme.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return scheme.getDescription();
			} else if (key.equals(COLUMN_LABEL)) {
				return scheme.getLabel();
			} else if (key.equals(COLUMN_WIDTH)) {
				return new Integer(scheme.getWidth());
			} else if (key.equals(COLUMN_HEIGHT)) {
				return new Integer(scheme.getHeight());
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				return scheme.getDomainId();
			} else if (key.equals(COLUMN_MAP_ID)) {
				return scheme.getMapId();
			} else if (key.equals(COLUMN_SYMBOL_ID)) {
				return scheme.getSymbolId();
			} else if (key.equals(COLUMN_UGO_CELL_ID)) {
				return scheme.getUgoCellId();
			} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
				return scheme.getSchemeCellId();
			} else if (key.equals(COLUMN_KIND)) {
				return new Integer(scheme.getKind().value());
			} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
				return scheme.getParentSchemeElementId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Scheme scheme, final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (scheme != null) {
				if (key.equals(COLUMN_NAME)) {
					scheme.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					scheme.setDescription((String) value);
				} else if (key.equals(COLUMN_LABEL)) {
					scheme.setLabel((String) value);
				} else if (key.equals(COLUMN_WIDTH)) {
					scheme.setWidth(((Integer) value).intValue());
				} else if (key.equals(COLUMN_HEIGHT)) {
					scheme.setHeight(((Integer) value).intValue());
				} else if (key.equals(COLUMN_DOMAIN_ID)) {
					scheme.setDomainId((Identifier) value);
				} else if (key.equals(COLUMN_MAP_ID)) {
					scheme.setMapId((Identifier) value);
				} else if (key.equals(COLUMN_SYMBOL_ID)) {
					scheme.setSymbolId((Identifier) value);
				} else if (key.equals(COLUMN_UGO_CELL_ID)) {
					scheme.setUgoCellId((Identifier) value);
				} else if (key.equals(COLUMN_SCHEME_CELL_ID)) {
					scheme.setSchemeCellId((Identifier) value);
				} else if (key.equals(COLUMN_KIND)) {
					scheme.setKind(IdlKind.from_int(((Integer) value).intValue()));
				} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
					scheme.setParentSchemeElementId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeWrapper getInstance() {
		if (instance == null) {
			instance = new SchemeWrapper();
		}
		return instance;
	}
}
