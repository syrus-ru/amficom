/*
 * $Id: LinkTypeWrapper.java,v 1.13 2005/07/25 20:49:36 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.13 $, $Date: 2005/07/25 20:49:36 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */
public final class LinkTypeWrapper extends StorableObjectWrapper {

	// codename VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// sort NUMBER(2,0),
	public static final String COLUMN_NATURE = "nature";

	public static final String COLUMN_KIND = "kind";

	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER = "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturer_code";

	// image_id VARCHAR2(32),
	public static final String COLUMN_IMAGE_ID = "image_id";

	private static LinkTypeWrapper instance;

	private List<String> keys;

	private LinkTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_KIND,
				COLUMN_MANUFACTURER,
				COLUMN_MANUFACTURER_CODE,
				COLUMN_IMAGE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static LinkTypeWrapper getInstance() {
		if (instance == null)
			instance = new LinkTypeWrapper();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof LinkType) {
			final LinkType type = (LinkType) object;			
			if (key.equals(COLUMN_CODENAME))
				return type.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return type.getDescription();
			if (key.equals(COLUMN_NAME))
				return type.getName();
			if (key.equals(COLUMN_KIND))
				return new Integer(type.getSort().value());
			if (key.equals(COLUMN_MANUFACTURER))
				return type.getManufacturer();
			if (key.equals(COLUMN_MANUFACTURER_CODE))
				return type.getManufacturerCode();
			if (key.equals(COLUMN_IMAGE_ID))
				return type.getImageId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof LinkType) {
			final LinkType type = (LinkType) object;
			if (key.equals(COLUMN_NAME))
				type.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				type.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				type.setCodename((String) value);
			else if (key.equals(COLUMN_KIND))
				type.setSort(LinkTypeSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_MANUFACTURER))
				type.setManufacturer((String) value);
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				type.setManufacturerCode((String) value);
			else if (key.equals(COLUMN_IMAGE_ID))
				type.setImageId((Identifier) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME) 
				|| key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_MANUFACTURER)
				|| key.equals(COLUMN_MANUFACTURER_CODE)) {
			return String.class;
		} else if (key.equals(COLUMN_KIND)) {
			return Integer.class;			
		} else if (key.equals(COLUMN_IMAGE_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
