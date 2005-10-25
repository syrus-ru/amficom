/*-
 * $Id: MapLibraryWrapper.java,v 1.6 2005/10/25 19:53:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/25 19:53:11 $
 * @module map
 */

public final class MapLibraryWrapper extends StorableObjectWrapper<MapLibrary> {
	
	public static final String COLUMN_PARENT_MAP_LIBRARY_ID = "parent_map_library_id";

	private static MapLibraryWrapper instance;

	private List<String> keys;
	
	protected MapLibraryWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_PARENT_MAP_LIBRARY_ID};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}
	
	public static MapLibraryWrapper getInstance() {
		if (instance == null)
			instance = new MapLibraryWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final MapLibrary mapLibrary, final String key) {
		if (mapLibrary != null) {
			if (key.equals(COLUMN_NAME)) {
				return mapLibrary.getName();
			} else if (key.equals(COLUMN_CODENAME)) {
				return mapLibrary.getCodename();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return mapLibrary.getDescription();
			} else if (key.equals(COLUMN_PARENT_MAP_LIBRARY_ID)) {
				return mapLibrary.getParent();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public void setValue(final MapLibrary mapLibrary, final String key, final Object value) {
		if (mapLibrary != null) {
			if (key.equals(COLUMN_NAME)) {
				mapLibrary.setName((String) value);
			} else if (key.equals(COLUMN_CODENAME)) {
				mapLibrary.setCodename((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				mapLibrary.setDescription(COLUMN_DESCRIPTION);
			} else if (key.equals(COLUMN_PARENT_MAP_LIBRARY_ID)) {
				mapLibrary.setParent((MapLibrary) value);
			}
		}
	}
}
