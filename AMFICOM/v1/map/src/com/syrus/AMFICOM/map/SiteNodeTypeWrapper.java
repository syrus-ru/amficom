/*
 * $Id: SiteNodeTypeWrapper.java,v 1.4 2005/02/03 08:38:02 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/03 08:38:02 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeTypeWrapper implements StorableObjectWrapper {

	// codename VARCHAR2(32) NOT NULL,
	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// image_id VARCHAR2(32) NOT NULL,
	public static final String				COLUMN_IMAGE_ID				= "image_id";
	// topological NUMBER(1),
	public static final String				COLUMN_TOPOLOGICAL			= "topological";

	protected static SiteNodeTypeWrapper	instance;

	protected List							keys;

	private SiteNodeTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IMAGE_ID,
				COLUMN_TOPOLOGICAL, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static SiteNodeTypeWrapper getInstance() {
		if (instance == null)
			instance = new SiteNodeTypeWrapper();
		return instance;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof SiteNodeType) {
			SiteNodeType siteNodeType = (SiteNodeType) object;
			if (key.equals(COLUMN_CODENAME))
				return siteNodeType.getCodename();
			else if (key.equals(COLUMN_NAME))
				return siteNodeType.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return siteNodeType.getDescription();
			else if (key.equals(COLUMN_IMAGE_ID))
				return siteNodeType.getImageId();
			else if (key.equals(COLUMN_TOPOLOGICAL))
				return Boolean.valueOf(siteNodeType.isTopological());
			else if (key.equals(COLUMN_CHARACTERISTICS))
				return siteNodeType.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof SiteNodeType) {
			SiteNodeType siteNodeType = (SiteNodeType) object;
			if (key.equals(COLUMN_CODENAME))
				siteNodeType.setCodename((String) value);
			else if (key.equals(COLUMN_NAME))
				siteNodeType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				siteNodeType.setDescription((String) value);
			else if (key.equals(COLUMN_IMAGE_ID))
				siteNodeType.setImageId((Identifier) value);
			else if (key.equals(COLUMN_TOPOLOGICAL))
				siteNodeType.setTopological(((Boolean) value).booleanValue());
			else if (key.equals(COLUMN_CHARACTERISTICS))
				siteNodeType.setCharacteristics((List) value);
		}
	}

}
