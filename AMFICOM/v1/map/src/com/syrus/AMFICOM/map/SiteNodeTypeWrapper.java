/*
 * $Id: SiteNodeTypeWrapper.java,v 1.16 2006/03/13 15:54:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.16 $, $Date: 2006/03/13 15:54:26 $
 * @author $Author: bass $
 * @module map
 */
public final class SiteNodeTypeWrapper extends StorableObjectWrapper<SiteNodeType> {
	// sort NUMBER(1)
	public static final String COLUMN_SORT = "sort";
	// codename VARCHAR2(32) NOT NULL,
	// name VARCHAR2(64),
	// description VARCHAR2(256),
	// image_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_IMAGE_ID = "image_id";
	// topological NUMBER(1),
	public static final String COLUMN_TOPOLOGICAL = "topological";
	// mapLibrary 
	public static final String COLUMN_MAP_LIBRARY_ID = "map_library_id";

	private static SiteNodeTypeWrapper instance;

	private List<String> keys;

	private SiteNodeTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME,
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_IMAGE_ID,
				COLUMN_TOPOLOGICAL };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static SiteNodeTypeWrapper getInstance() {
		if (instance == null)
			instance = new SiteNodeTypeWrapper();
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

	@Override
	public Class<?> getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final SiteNodeType siteNodeType, final String key) {
		if (siteNodeType != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return siteNodeType.getCodename();
			}
			else if (key.equals(COLUMN_NAME)) {
				return siteNodeType.getName();
			}
			else if (key.equals(COLUMN_DESCRIPTION)) {
				return siteNodeType.getDescription();
			}
			else if (key.equals(COLUMN_IMAGE_ID)) {
				return siteNodeType.getImageId();
			}
			else if (key.equals(COLUMN_TOPOLOGICAL)) {
				return Boolean.valueOf(siteNodeType.isTopological());
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
	public void setValue(final SiteNodeType siteNodeType, final String key, final Object value) {
		if (siteNodeType != null) {
			if (key.equals(COLUMN_CODENAME)) {
				siteNodeType.setCodename((String) value);
			}
			else if (key.equals(COLUMN_NAME)) {
				siteNodeType.setName((String) value);
			}
			else if (key.equals(COLUMN_DESCRIPTION)) {
				siteNodeType.setDescription((String) value);
			}
			else if (key.equals(COLUMN_IMAGE_ID)) {
				siteNodeType.setImageId((Identifier) value);
			}
			else if (key.equals(COLUMN_TOPOLOGICAL)) {
				siteNodeType.setTopological(((Boolean) value).booleanValue());
			}
		}
	}

}
